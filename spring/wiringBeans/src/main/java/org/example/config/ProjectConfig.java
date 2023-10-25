package org.example.config;

import org.example.main.Parrot;
import org.example.main.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan (basePackages = "org.example.main")
public class ProjectConfig {
}
