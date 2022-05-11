package vn.utc.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class GatewaySecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.cors().and().csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/hotel-user-service/api/v1/user-service/register").permitAll()
                .pathMatchers(HttpMethod.GET, "/hotel-management/api/v1/bookings").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.PUT, "/hotel-management/api/v1/bookings/admin/arrived").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/hotel-management/api/v1/admin").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.GET, "/hotel-management/api/v1/hotel-services").permitAll()
                .pathMatchers(HttpMethod.GET, "/hotel-management/api/v1/hotel-services/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/hotel-management/api/v1/hotel-services/filter").permitAll()
                .pathMatchers(HttpMethod.POST, "/hotel-management/api/v1/hotel-services").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.PUT, "/hotel-management/api/v1/hotel-services").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/hotel-management/api/v1/hotel-services").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.PUT, "/hotel-management/api/v1/hotel-services/request-for-room").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.GET, "/hotel-management/api/v1/rooms/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/hotel-management/api/v1/rooms/filter").permitAll()
                .pathMatchers(HttpMethod.POST, "/hotel-management/api/v1/rooms/available").permitAll()
                .pathMatchers(HttpMethod.POST, "/hotel-management/api/v1/rooms").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.PUT, "/hotel-management/api/v1/rooms/**").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/hotel-management/api/v1/rooms/**").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.GET, "/hotel-management/api/v1/reviews").permitAll()
                .pathMatchers(HttpMethod.DELETE, "/hotel-management/api/v1/reviews/**").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.POST, "/hotel-management/api/v1/room-swaps/begin-swap").hasRole("ROLE_ADMIN")
                .anyExchange().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.FORBIDDEN))
                .and()
                .oauth2Login()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}
