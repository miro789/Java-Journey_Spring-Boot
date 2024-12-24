package vn.miro.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;



// 1.
// @Configuration
//public class AppConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowCredentials(true)
//                .allowedOrigins("http://localhost:9090")
//                .allowedMethods("*")
//                .allowedHeaders("*");
//    }
//}


// 2
// @Configuration
//public class AppConfig {
//
//    @Bean
//    public WebMvcConfigurer corsFilter() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedOrigins("http://localhost:9090");
//            }
//        };
//    }
//}

// @Configuration
//public class AppConfig {
//    @Bean
//    public FilterRegistrationBean<CorsFilter> corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(List.of("http://localhost:9090", "http://google.com"));
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
//        config.setAllowedHeaders(List.of("*"));
//        source.registerCorsConfiguration("/**",config);
//        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return bean;
//    }
//}

// 3.
@Component
public class AppConfig extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9090");
        filterChain.doFilter(request, response);
    }
}