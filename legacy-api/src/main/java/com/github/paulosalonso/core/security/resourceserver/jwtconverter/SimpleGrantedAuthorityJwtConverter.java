package com.github.paulosalonso.core.security.resourceserver.jwtconverter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SimpleGrantedAuthorityJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		if (jwt.containsClaim("authorities")) {
			return jwt.getClaimAsStringList("authorities").stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet());
		}

		return Collections.emptyList();
	}

}
