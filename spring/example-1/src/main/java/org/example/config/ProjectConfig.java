package org.example.config;

import org.example.main.Parrot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
// Using the basePackages attribute of the annotation, we tell Spring where to look
//for classes annotated with stereotype annotations.
@ComponentScan(basePackages = "org.example.main")
public class ProjectConfig {
}
