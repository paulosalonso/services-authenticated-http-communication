package com.github.paulosalonso.core.security.resourceserver.jwtconverter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Primary
@Component
public class JwtGrantedAuthoritiesConverterComposite implements Converter<Jwt, Collection<GrantedAuthority>> {

	private List<Converter<Jwt,Collection<GrantedAuthority>>> converters;
	
	public JwtGrantedAuthoritiesConverterComposite(List<Converter<Jwt,Collection<GrantedAuthority>>> converters) {
		this.converters = converters;
	}	
	
	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {	
		return converters.stream()
				.map(converter -> converter.convert(jwt))
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}
}
