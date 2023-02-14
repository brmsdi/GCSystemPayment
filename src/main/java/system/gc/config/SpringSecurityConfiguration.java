package system.gc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import system.gc.security.UserModelDetailsService;
import system.gc.security.filters.AuthenticationFilter;
import system.gc.security.filters.JWTAuthenticationEntryPoint;
import system.gc.security.filters.JWTAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ProviderManager providerManager;

    protected SpringSecurityConfiguration(UserModelDetailsService userModelDetailsService)
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userModelDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        this.providerManager = new ProviderManager(daoAuthenticationProvider);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter2(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity
                .cors()
                .disable()
                .csrf()
                .disable()
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                });
        httpSecurity.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        return httpSecurity.build();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter()
    {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setFilterProcessesUrl("/login");
        authenticationFilter.setAuthenticationManager(providerManager);
        return authenticationFilter;
    }
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter2() {
        return new JWTAuthenticationFilter();
    }
}
