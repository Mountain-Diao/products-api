package com.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "api.orders")
@Component
public class OrdersApiConfig {
    @NonNull
    private String url = "localhost:8081";

    @NonNull
    private String checkExistEndpoint = "/has-ordered?id=";

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    @NonNull
    public String getSumOfEndpoint() {
        return checkExistEndpoint;
    }

    public void setSumOfEndpoint(@NonNull String sumOfEndpoint) {
        this.checkExistEndpoint = sumOfEndpoint;
    }
}
