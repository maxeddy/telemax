package nl.maxbevelander.telemax.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun webSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/**")
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                    .requestMatchers("/viewer/login", "/", "/{pageNumber:\\d+}").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/admin/pages", true)
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            }
        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}
