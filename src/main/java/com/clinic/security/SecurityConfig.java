package com.clinic.security;

import com.clinic.service.implementation.ClinicUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    @Lazy
    private ClinicUserServiceImpl userService;

    public static final String[] AUTH_WHITELIST = {
            "/authenticate",
            "/signup",
            "/api/user/adduser",
            "/swagger-ui/**",
            "/api-docs/**",
            "/v3/**",
            "/swagger-ui.html"
    };

    @Bean
    public JwtAuthenticationFilter authFilter(JwtUtil jwtUtil,
                                              ClinicUserServiceImpl userService,
                                              RequestMappingHandlerMapping mapping) {
        return new JwtAuthenticationFilter(jwtUtil, userService, mapping);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            JwtAuthenticationFilter authFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:8779", "https://c1hg99fq-8779.uks1.devtunnels.ms"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }


    @Bean
    AuthenticationSuccessHandler successHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        // TODO Auto-generated method stub
        return new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();

                UserDetails userDetails = userService.loadUserByUsername(username);
                if (userDetails != null && passwordEncoder().matches(password, userDetails.getPassword())) {
                    return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
                } else {
                    throw new BadCredentialsException("Invalid username or password");
                }
            }

            public boolean supports(Class<?> authentication) {
                return authentication.equals(UsernamePasswordAuthenticationToken.class);
            }
        };
    }
}