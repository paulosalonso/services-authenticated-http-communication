package com.github.paulosalonso.client;

import lombok.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Map;

public class JwtTokenProvider {

    private final Encoder encoder;
    private final Decoder decoder;
    private final ObjectMapper mapper;
    private final String authorizationServerUrl;
    private final ClientCredentials credentials;

    private String token;

    public JwtTokenProvider(String authorizationServerUrl, ClientCredentials credentials) {
        this.authorizationServerUrl = authorizationServerUrl;
        this.credentials = credentials;
        encoder = Base64.getEncoder();
        decoder = Base64.getDecoder();
        mapper = new ObjectMapper();
    }

    public String getToken() {
        if (isTokenExpired()) {
            Map<String, String> response = new RestTemplate()
                    .postForObject(authorizationServerUrl, buildRequest(credentials), Map.class);

            token = response.get("access_token");
        }

        return token;
    }

    private HttpEntity<MultiValueMap<String, String>> buildRequest(ClientCredentials credentials) {
        return new HttpEntity<>(buildRequestParameters(credentials), buildRequestHeaders(credentials));
    }

    private MultiValueMap<String, String> buildRequestParameters(ClientCredentials credentials) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("client_id", credentials.getClientId());
        parameters.add("client_secret", credentials.getClientSecret());
        parameters.add("grant_type", "client_credentials");
        return parameters;
    }

    private HttpHeaders buildRequestHeaders(ClientCredentials credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + generateEncodedBasicAuth(credentials));
        return headers;
    }

    private String generateEncodedBasicAuth(ClientCredentials credentials) {
        String basic = String.format("%s:%s", credentials.getClientId(), credentials.getClientSecret());
        byte[] encodedByteArray = encoder.encode(basic.getBytes());
        return new String(encodedByteArray);
    }

    private boolean isTokenExpired() {
        if (token == null) {
            return true;
        }

        LocalDateTime tokenExpiration = getTokenPayload().getExpiration();

        return LocalDateTime.now(ZoneId.systemDefault()).isAfter(tokenExpiration);
    }

    private JwtPayload getTokenPayload() {
        String payload = token.split("\\.")[1];
        String decodedPayload = new String(decoder.decode(payload));

        try {
            return mapper.readValue(decodedPayload, JwtPayload.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Getter
    @Builder
    public static class ClientCredentials {
        private String clientId;
        private String clientSecret;
    }

}
