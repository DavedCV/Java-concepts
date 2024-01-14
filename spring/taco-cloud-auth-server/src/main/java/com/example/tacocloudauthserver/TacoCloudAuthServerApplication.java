package com.example.tacocloudauthserver;

import com.example.tacocloudauthserver.domain.User;
import com.example.tacocloudauthserver.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TacoCloudAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TacoCloudAuthServerApplication.class, args);
    }

    public CommandLineRunner dataLoader(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            repo.save(new User("habuma", encoder.encode("password"), "ROLE_ADMIN"));
            repo.save(new User("tacochef", encoder.encode("password"), "ROLE_ADMIN"));
        };
    }

}
