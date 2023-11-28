package com.example.springbootbigquery.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.*;

@Configuration
public class BigQueryConfiguration {


    @Value("${gcp.credentials.location}")
    private Resource googleCredentialsPath;

    @Bean
    public GoogleCredentials googleCredentials() {
        try (InputStream serviceAccountStream = googleCredentialsPath.getInputStream()) {
            return GoogleCredentials.fromStream(serviceAccountStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load BigQuery credentials", e);
        }
    }

    @Bean
    public BigQuery bigQuery(GoogleCredentials googleCredentials) {
        return BigQueryOptions.newBuilder()
                              .setCredentials(googleCredentials)
                              .build()
                              .getService();
    }
}
