package ca.mta.COMP4721.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Web socket config.
 */
@Configuration
public class WebSocketConfig implements WebMvcConfigurer { 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedOrigins("*").allowedHeaders("*");
    }
}
