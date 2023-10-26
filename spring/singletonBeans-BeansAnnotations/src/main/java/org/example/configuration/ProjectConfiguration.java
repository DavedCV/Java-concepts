package org.example.configuration;

import org.example.services.CommentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfiguration {

    @Bean
    public CommentService commentService() {
        return new CommentService();
    }
}
