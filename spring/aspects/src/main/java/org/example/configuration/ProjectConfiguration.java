package org.example.configuration;

import org.example.aspects.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "org.example.services")
// Enables the aspects mechanism in our Spring app
@EnableAspectJAutoProxy
public class ProjectConfiguration {
    @Bean
    // Adds an instance of the LoggingAspect class to the Spring context
    public LoggingAspect aspect() {
        return new LoggingAspect();
    }
}
