package system.gc.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import system.gc.security.UserModelDetailsService;
import system.gc.security.filters.JWTAuthenticationEntryPoint;
import system.gc.security.filters.JWTAuthenticationFilter;
import system.gc.security.filters.JWTValidate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Log4j2
@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {
    @Autowired
    private Environment environment;

    private final UserModelDetailsService userModelDetailsService;

    private final ProviderManager providerManager;

    protected SpringSecurityConfiguration(UserModelDetailsService userModelDetailsService)
    {
        this.userModelDetailsService = userModelDetailsService;
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userModelDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A));
        this.providerManager = new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Configuration");
        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .anyRequest()
                        .authenticated())
                .exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint())
                        .and()
                        .addFilterBefore(new JWTValidate(userModelDetailsService), JWTAuthenticationFilter.class)
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return httpSecurity.build();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter()
    {
        return new JWTAuthenticationFilter(providerManager);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        log.info("Cors configuration");
        String[] originsSplit = Objects.requireNonNull(environment.getProperty("ORIGINS")).split(",");
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "UPDATE", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedOrigins(Arrays.stream(originsSplit).toList());
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}
