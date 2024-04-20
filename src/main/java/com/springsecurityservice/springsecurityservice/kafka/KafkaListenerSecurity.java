package com.springsecurityservice.springsecurityservice.kafka;

import com.springsecurityservice.springsecurityservice.securityservices.JwtTokenUtil;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaListenerSecurity {
    private final KafkaSender kafkaSender;
    private final JwtTokenUtil jwtTokenUtil;

    @KafkaListener(topics = "#{'${custom.kafka.topic.request}'.split(',')}")
    public void listener(@Payload String data) {
        //Let request contain uuid + ' ' + cookie value
        //Response - same uuid + ' ' + user's email
        String[] parsed = data.split(" ");
        String check = "";
            try {
                if (parsed.length > 1)
                    check = jwtTokenUtil.validateJWT(parsed[1]);
            } catch (JwtException ignored) {}

        kafkaSender.sendResponse(parsed[0] + check);
    }
}
