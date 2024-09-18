package org.example.basicauth.Config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.basicauth.Component.CustomLogoutSuccessHandler;
import org.example.basicauth.Exception.CustomAuthenticationEntryPoint;
import org.example.basicauth.Jwt.JwtAuthenticationFilter;
import org.example.basicauth.Service.CombinedUserDetailsService;
import org.example.basicauth.Service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CombinedUserDetailsService combinedUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    http
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception-> exception
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
            )
            .logout(logout->logout
                    .logoutUrl("/logout")
                    .addLogoutHandler(customLogoutSuccessHandler)
                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .permitAll()
            )

            .authorizeHttpRequests(auth->auth
                    .requestMatchers("/admin/**").hasAuthority("ADMIN")
                    .requestMatchers("/user/**").hasAuthority("USER")
                    .requestMatchers("/customer/**").hasAuthority("CUSTOMER")
                    .requestMatchers("/account/**").hasAuthority("CUSTOMER")
                    .requestMatchers("/transaction/**").hasAuthority("CUSTOMER")
                    .requestMatchers("/creditCard/**").hasAuthority("CUSTOMER")
                    .requestMatchers("/loanApplication/**").hasAuthority("CUSTOMER")
                    .requestMatchers("/auth/**", "/login", "/login.html").permitAll()
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());

            return http.build();
}
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(combinedUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
    }



}
