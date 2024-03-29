package com.StudyCafe_R.StudyCafe_R.infra.config;

import com.StudyCafe_R.StudyCafe_R.modules.account.service.AccountUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;
    private final HandlerMappingIntrospector handlerMappingIntrospector;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/login")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/sign-up")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/check-email-token")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/email-login")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/login-by-email")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/search/study")).permitAll()
                                .requestMatchers(new MvcRequestMatcher(handlerMappingIntrospector,"/profile/*")).permitAll()
                                .anyRequest().authenticated());

        http.formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer.loginPage("/login").permitAll());

        http.logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer.logoutSuccessUrl("/"));

        http.rememberMe(httpSecurityRememberMeConfigurer ->
                        httpSecurityRememberMeConfigurer.userDetailsService(userDetailsService)
                                .tokenRepository(tokenRepository()));
        return http.build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/node_modules/**"))
                .requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
                .requestMatchers(new AntPathRequestMatcher("/resources/**"))
                .requestMatchers(new AntPathRequestMatcher("/error"))
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }
}
