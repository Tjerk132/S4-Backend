package nl.fhict.s4.restserver.config;

import nl.fhict.s4.restserver.security.jwt.JWTAuthenticationFilter;
import nl.fhict.s4.restserver.security.jwt.JWTLoginFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // disable caching
        http
            .headers()
            .cacheControl();

        // disable csrf for our requests.
        http
            .csrf()
            .disable();

        //only permit secure requests
        http
            .requiresChannel()
            .anyRequest()
            .requiresSecure();

        http
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/*")
            .permitAll()

            .anyRequest().authenticated()

            .and()
            // We filter the api/login requests
            .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
            // And filter other requests to check the presence of JWT in header
            .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        // Create a default account

        // Prior to Spring Security 5.0, the default PasswordEncoder
        // was NoOpPasswordEncoder which required plain text passwords
        // but is insecure. Spring Security 5.x onwards, the default
        // PasswordEncoder is DelegatingPasswordEncoder,
        // which requires a Password Storage Format.

        // Most commonly used PasswordEncoder with their id’s is "noop",
        // uses plain text NoOpPasswordEncoder

//        PasswordEncoder encoder =
//                PasswordEncoderFactories.createDelegatingPasswordEncoder();

        auth.inMemoryAuthentication()

             //(encoder.encode("{noop}ts321"))

            .withUser("user")
            .password("{noop}ts321")
            .roles("USER")

            .and()

            .withUser("admin")
            .password("{noop}ts321")
            .roles("USER", "ADMIN");
    }
}
