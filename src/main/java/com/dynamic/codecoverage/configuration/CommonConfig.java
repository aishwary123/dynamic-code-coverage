package com.dynamic.codecoverage.configuration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.dynamic.codecoverage.utils.SystemUtils;

/**
 * All the beans which are common across this application are defined here.
 * 
 * @author aishwaryt
 */
@Configuration
public class CommonConfig {

    private static final String REST_TEMPLATE_CONNECTION_TIMEOUT = "REST_TEMPLATE_CONNECTION_TIMEOUT";
    private static final String REST_TEMPLATE_READ_TIMEOUT = "REST_TEMPLATE_READ_TIMEOUT";

    @Value("${REST_TEMPLATE_CONNECTION_TIMEOUT}")
    private String defaultRestTemplateConnectionTimeoutDefault;

    @Value("${REST_TEMPLATE_READ_TIMEOUT}")
    private String defaultRestTemplateReadTimeoutDefault;

    @Bean
    public RestTemplate restTemplate()
        throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        
        final int connectionTimeoutInMillis = Integer.parseInt(
                    SystemUtils.getSystemVariableOrDefault(
                                REST_TEMPLATE_CONNECTION_TIMEOUT,
                                defaultRestTemplateConnectionTimeoutDefault));
        final int readTimeoutInMillis = Integer.parseInt(
                    SystemUtils.getSystemVariableOrDefault(
                                REST_TEMPLATE_READ_TIMEOUT,
                                defaultRestTemplateReadTimeoutDefault));
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain,
                                                String authType) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(
                    null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(
                    sslContext);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(
                    csf).setSSLHostnameVerifier(
                                new NoopHostnameVerifier()).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectionRequestTimeout(connectionTimeoutInMillis);
        requestFactory.setReadTimeout(readTimeoutInMillis);
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
        
    }

}
