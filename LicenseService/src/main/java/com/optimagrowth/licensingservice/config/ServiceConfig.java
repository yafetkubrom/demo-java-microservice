package com.optimagrowth.licensingservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

// injects the configuration data for the database into a database connection object
@Configuration
@ConfigurationProperties(prefix = "example")
@Getter @Setter
public class ServiceConfig{

    private String property;

}