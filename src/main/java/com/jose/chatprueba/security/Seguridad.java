package com.jose.chatprueba.security;

//import com.jose.chatprueba.security.httpFilters.JwtAuthorizationFilter;
import com.jose.chatprueba.services.UsuarioServices;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * <h1>Clase de configuración de la seguridad</h1>
 * <p>Gestiona la config. de las conexiones http y del CORS</p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Seguridad extends KeycloakWebSecurityConfigurerAdapter/* implements WebMvcConfigurer*/ {
    private final UsuarioServices userDetailsService;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final PasswordEncoder passwordEncoder;
    //private final JwtAuthorizationFilter jwtAuthorizationFilter;

//    @Bean
//    protected ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
//        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
//    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * <b>Bean de configuración del cors</b>
     *
     * @return Configuración del cors
     */
//    @Bean
//    CorsConfigurationSource corsConfigurationSource(){
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedOriginPatterns(
//                Arrays.asList("http://localhost:[*]","chrome-extension://**","file://**"));
//        configuration.setAllowedMethods(
//                Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
//        configuration.setAllowedHeaders(
//                Arrays.asList(
//                        "Authorization",
//                        "Cache-Control",
//                        "Content-Type",
//                        "Access-Control-Allow-Origin",
//                        "Access-Control-Allow-Credentials"
//                ));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .anyRequest()
                .permitAll();
        http.csrf().disable();
//        http
//                .cors()
//                .and()
//                .csrf().disable() //Desactivar y testear en versiones finales
//                .exceptionHandling()
//                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .and()
//                .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                        .antMatchers(HttpMethod.GET, "/usuario").permitAll()
////                    .antMatchers(HttpMethod.GET, "/chat").permitAll()
////                    .antMatchers(HttpMethod.GET, "/chat/**").permitAll()
////                    .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
////                    .antMatchers(HttpMethod.GET, "/usuario/**").permitAll()//hasRole("USER")
////                    .antMatchers(HttpMethod.POST, "/usuario/**").hasRole("ADMIN")
////                    .antMatchers(HttpMethod.PUT, "/usuario/**").hasRole("ADMIN")
////                    .antMatchers(HttpMethod.DELETE, "/usuario/**").hasRole("ADMIN")
////                    .antMatchers(HttpMethod.GET, "/user/**").hasRole("USER")
////                    .antMatchers(HttpMethod.GET, "/user/**").authenticated()
////                    .antMatchers( "/tienda","/static/**","/css/**","/assets/**","/js/**").permitAll() //Config provisional web thymeleaf
//                    .anyRequest().permitAll();//.authenticated();
////                .and()
////                .oauth2ResourceServer()
////                    .jwt();
//                //.and()
//                //.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(
                new SessionRegistryImpl());
    }

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth) throws Exception {

        KeycloakAuthenticationProvider keycloakAuthenticationProvider
                = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
                new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}
