package com.github.paulosalonso.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.*;
import java.util.TimeZone;

@NoArgsConstructor
@Getter
@Setter
public class JwtPayload {
    String[] scope;
    long exp;
    String jti;
    String client_id;

    //{"scope":["*"],"exp":1600400486,"jti":"69719aed-e9df-4178-a843-0c7e0c2e1f50","client_id":"legacy-consumer"}

    LocalDateTime getExpiration() {
        return LocalDateTime.ofEpochSecond(exp, 0, OffsetDateTime.now().getOffset());
    }
}
