package nl.fhict.s4.restserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Controller
//@EnableAutoConfiguration
//@ComponentScan(basePackageClasses = UserController.class)
@PropertySources({
    @PropertySource("classpath:application.yml")
})

@ComponentScan(basePackages = {
    "com.auth0.spring.security.api",
    "nl.fhict.s4.restserver.config",
    "nl.fhict.s4.restserver.controllers"
})

//Intellij 14.0.3 (and most likely, earlier versions too)
//is not yet configured to recognise the @SpringBootApplication annotation.
//@SpringBootApplication is the same as
// @Configuration @EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class }) @ComponentScan

//To discard the security auto-configuration and add your own configuration, you need to exclude the SecurityAutoConfiguration class.
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class DemoApplication {

    @GetMapping(value = "/")
    public String index() {

        return "index.html";
    }

    public static void main(String[] args)   {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean simpleCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Allow the Vue client URL and port
        config.setAllowedOrigins(Arrays.asList("http://localhost:8005","http://192.168.178.213:8005"));
        // Allow only thew necessary methods
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        // Allow all headers for now
        config.setAllowedHeaders(Collections.singletonList("*"));
        // Expose Authentication header for client to retrieve jwt-token
        config.setExposedHeaders(Collections.singletonList("Authorization"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
