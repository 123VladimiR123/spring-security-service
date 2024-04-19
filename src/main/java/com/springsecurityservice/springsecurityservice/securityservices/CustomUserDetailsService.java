package com.springsecurityservice.springsecurityservice.securityservices;

import com.springsecurityservice.springsecurityservice.entities.CustomUser;
import com.springsecurityservice.springsecurityservice.entities.CustomUserDTO;
import com.springsecurityservice.springsecurityservice.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import javax.security.auth.login.CredentialException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomUser userDetails = usersRepository.findUserByEmail(email);
        if (userDetails == null) throw new UsernameNotFoundException("Email not found");

        return userDetails;
    }

    public Optional<UserDetails> verifyByIdAndEmail(String id, String email) {
        return Optional.of(usersRepository.findCustomUserByIdAndEmail(UUID.fromString(id), email));
    }

    public CustomUser save(CustomUserDTO user) throws InstanceAlreadyExistsException, CredentialException {
        if (!user.getPassword().equals(user.getConfirm())) throw new CredentialException("Password must be the same");
        if (usersRepository.findUserByEmail(user.getEmail()) != null) throw new InstanceAlreadyExistsException("Username already exists");
        return usersRepository.save(new CustomUser(UUID.randomUUID(), user.getEmail(), passwordEncoder.encode(user.getPassword())));
    }
}
