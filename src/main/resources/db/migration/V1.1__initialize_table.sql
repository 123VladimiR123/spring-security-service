CREATE SCHEMA IF NOT EXISTS users;

CREATE TABLE users.info(
    id varchar(255) primary key,
    email varchar(255) unique,
    password varchar(255)
)