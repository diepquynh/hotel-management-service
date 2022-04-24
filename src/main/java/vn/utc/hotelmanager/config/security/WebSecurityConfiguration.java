package vn.utc.hotelmanager.config.security;

import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.utc.hotelmanager.auth.user.data.UserRepository;
import vn.utc.hotelmanager.auth.user.model.OAuth2UserImpl;
import vn.utc.hotelmanager.auth.user.service.OAuth2UserServiceImpl;
import vn.utc.hotelmanager.auth.user.service.UserDetailsServiceImpl;
import vn.utc.hotelmanager.jwt.JwtAuthenticationFilter;
import vn.utc.hotelmanager.jwt.JwtTokenFilter;
import vn.utc.hotelmanager.jwt.JwtUtils;
import vn.utc.hotelmanager.jwt.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final OAuth2UserServiceImpl oauth2UserService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final JwtUtils jwtUtils;

    @Autowired
    public WebSecurityConfiguration(OAuth2UserServiceImpl oauth2UserService, UserDetailsServiceImpl userDetailsService,
                                    UserRepository userRepository, PasswordEncoder passwordEncoder, JwtConfig jwtConfig, JwtUtils jwtUtils) {
        this.oauth2UserService = oauth2UserService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfig, jwtUtils, userRepository))
                .addFilterAfter(new JwtTokenFilter(jwtConfig, jwtUtils, userDetailsService), JwtAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/users/register",
                        "/oauth2/**",
                        "/users/login/oauth").permitAll()
                .antMatchers(HttpMethod.GET,"/hotel-services",
                        "/hotel-services/**").permitAll()
                .antMatchers(HttpMethod.POST, "/hotel-services/filter").permitAll()
                .antMatchers(HttpMethod.GET, "/rooms/**").permitAll()
                .antMatchers(HttpMethod.POST, "/rooms/filter",
                        "/rooms/available").permitAll()
                .antMatchers(HttpMethod.GET, "/responses").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                    .loginPage("/oauth2/authorization/google")
                    .userInfoEndpoint()
                        .userService(oauth2UserService)
                .and()
                .successHandler((request, response, authentication) -> {
                    OAuth2UserImpl oauthUser = (OAuth2UserImpl) authentication.getPrincipal();

                    oauth2UserService.postLogin(oauthUser.getEmail());
                });
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
        corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
