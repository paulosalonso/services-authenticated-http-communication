package com.github.paulosalonso.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "com.github.paulosalonso")
public class Properties {

    public Security security = new Security();

    @Getter
    @Setter
    public class Security {
        public String signingKey;
        public Map<String, String> clients;
    }

}
