package com.toyota.cvqsfinal.config;

import com.toyota.cvqsfinal.jwt.JWTAccessDeniedHandler;
import com.toyota.cvqsfinal.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JWTAccessDeniedHandler jwtAccessDeniedHandler;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        return httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/listing/**").hasAuthority("ROLE_TEAMLEADER")
                .requestMatchers("/api/v1/vehicle/**").hasAuthority  ("ROLE_OPERATOR")
                .requestMatchers("/api/v1/defect/**").hasAuthority("ROLE_OPERATOR")
                .requestMatchers("/api/v1/vehicledefect/**").hasAuthority("ROLE_OPERATOR")
                .anyRequest().permitAll()
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
