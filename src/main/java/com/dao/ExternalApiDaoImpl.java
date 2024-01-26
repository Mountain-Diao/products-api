package com.dao;

import com.config.OrdersApiConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExternalApiDaoImpl implements ExternalApiDao{
    private final OrdersApiConfig config;

    @Autowired
    public ExternalApiDaoImpl(OrdersApiConfig config){
        this.config = config;
    }

    @Override
    public boolean callOrdersApi(long id) throws IOException{
        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
            HttpGet httpGet = new HttpGet(config.getUrl() + config.getSumOfEndpoint() + id);

            var response = httpClient.execute(httpGet);

            String responseBody = EntityUtils.toString(response.getEntity());

            return Boolean.parseBoolean(responseBody);
        }
    }
}
