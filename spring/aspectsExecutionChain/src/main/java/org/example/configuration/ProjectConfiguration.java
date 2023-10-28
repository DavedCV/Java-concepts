package org.example.configuration;

import org.example.aspects.LoggingAspect;
import org.example.aspects.SecurityAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan (basePackages = "org.example.services")
@EnableAspectJAutoProxy
public class ProjectConfiguration {

    // Both aspects need to be added as
    // beans in the Spring context.

    @Bean
    public SecurityAspect securityAspect() {
        return new SecurityAspect();
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
