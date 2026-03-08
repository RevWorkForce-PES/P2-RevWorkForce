package com.revature.revworkforce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Spring MVC.
 * 
 * Configures view controllers and other web-related settings.
 * 
 * @author RevWorkForce Team
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Add view controllers for simple page mappings.
     * These pages don't require any controller logic.
     * 
     * @param registry ViewControllerRegistry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Map root URL to login page
        registry.addViewController("/").setViewName("redirect:/login");

        // Map login URL
        registry.addViewController("/login").setViewName("auth/login");

        // Map error pages
        registry.addViewController("/error/404").setViewName("error/404");
        registry.addViewController("/error/500").setViewName("error/500");
    }

    @Override
    public void addResourceHandlers(
            org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/templates/frontend/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/templates/frontend/js/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/templates/frontend/images/");
    }
}