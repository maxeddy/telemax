package nl.maxbevelander.telemax.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
class UserDetailsConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsService(passwordEncoder: PasswordEncoder): UserDetailsService {
        val max = User.builder()
            .username("max")
            .password(passwordEncoder.encode("password123"))
            .roles("ADMIN")
            .build()

        val emily = User.builder()
            .username("emily")
            .password(passwordEncoder.encode("password123"))
            .roles("ADMIN")
            .build()

        return InMemoryUserDetailsManager(max, emily)
    }
}
