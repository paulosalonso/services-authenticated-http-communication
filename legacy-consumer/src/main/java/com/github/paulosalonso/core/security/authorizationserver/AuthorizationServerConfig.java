package com.github.paulosalonso.core.security.authorizationserver;

import com.github.paulosalonso.core.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	private static final int ONE_DAY_IN_SECONDS = 60 * 60 * 24;
	
	private static final int ONE_WEEK_IN_SECONDS = ONE_DAY_IN_SECONDS * 7;

	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private Properties properties;

	public AuthorizationServerConfig(PasswordEncoder passwordEncoder,
		 	AuthenticationManager authenticationManager, Properties properties) {
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.properties = properties;
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		security.checkTokenAccess("isAuthenticated()");
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		JwtAccessTokenConverter tokenConverter = jwtAccessTokenConverter();

		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(List.of(tokenConverter));

		endpoints.authenticationManager(authenticationManager)
				.tokenEnhancer(enhancerChain)
				.accessTokenConverter(tokenConverter);
	}
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey(properties.security.signingKey);
		return jwtAccessTokenConverter;
	}
}