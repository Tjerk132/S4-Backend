package nl.fhict.s4.restserver.config;

import nl.fhict.s4.restserver.security.jwt.JWTAuthenticationFilter;
import nl.fhict.s4.restserver.security.jwt.JWTLoginFilter;
import nl.fhict.s4.restserver.security.jwt.authentication.CustomAuthenticationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(/*debug = true*/)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public WebSecurityConfig() {
        this.customAuthenticationProvider = new CustomAuthenticationProvider();
    }

    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

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

            .antMatchers(HttpMethod.POST, "/login")
            .permitAll()

            .and()
                // We filter the api/login requests
            .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
            // And filter other requests to check the presence of JWT in header
            .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

            .authorizeRequests()
            .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web)  {
        web
            .ignoring().antMatchers(
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**");
        web
            .ignoring().antMatchers(HttpMethod.GET);
    }
}
