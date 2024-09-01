package org.example.basicauth;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Key;
import java.util.Base64;

@SpringBootApplication
public class BasicAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicAuthApplication.class, args);
    }

}
