package com.gignac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gignac.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {
	
	private AuthenticationProvider authenticationProvider;

	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	private AuthenticationEntryPoint authenticationEntryPoint;

	public SecurityFilterChainConfig(
			AuthenticationProvider authenticationProvider,
			JwtAuthenticationFilter jwtAuthenticationFilter,
			AuthenticationEntryPoint authenticationEntryPoint
	) {
		super();
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf -> csrf.disable())
		            .cors(cors -> Customizer.withDefaults())
				    .authorizeHttpRequests(requests -> requests.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
				    		                                   .anyRequest().authenticated()
				    		              )
				    .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				    .authenticationProvider(authenticationProvider)
					.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
					.exceptionHandling(handling -> handling.authenticationEntryPoint(authenticationEntryPoint));
		return httpSecurity.build();
	}

}
