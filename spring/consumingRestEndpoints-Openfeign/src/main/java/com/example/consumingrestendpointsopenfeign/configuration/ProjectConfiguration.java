package com.example.consumingrestendpointsopenfeign.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
// We enable the OpenFeign clients and tell the OpenFeign dependency where to search for the proxy contracts.
@EnableFeignClients(basePackages = {"com.example.consumingrestendpointsopenfeign"})
public class ProjectConfiguration {
}
