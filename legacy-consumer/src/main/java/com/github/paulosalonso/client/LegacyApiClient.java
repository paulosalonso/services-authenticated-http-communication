package com.github.paulosalonso.client;

import com.github.paulosalonso.client.JwtTokenProvider.ClientCredentials;
import com.github.paulosalonso.core.Properties;
import com.github.paulosalonso.domain.model.Estado;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class LegacyApiClient {

    private final Properties properties;
    private final JwtTokenProvider tokenProvider;

    public LegacyApiClient(Properties properties) {
        this.properties = properties;

        String authorizationServerUrl = properties.legacyApi.url + properties.legacyApi.oauthPath;
        ClientCredentials credentials = ClientCredentials.builder()
                .clientId(properties.legacyApi.clientId)
                .clientSecret(properties.legacyApi.clientSecret)
                .build();

        tokenProvider = new JwtTokenProvider(authorizationServerUrl, credentials);
    }

    public List<Estado> getEstados() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.getToken());

        HttpEntity<Estado> request = new HttpEntity<Estado>(headers);

        String url = String.format("%s/estados", properties.legacyApi.url);

        ResponseEntity<Estado[]> estados = new RestTemplate()
                .exchange(url, HttpMethod.GET, request, Estado[].class);

        return List.of(estados.getBody());
    }
}
