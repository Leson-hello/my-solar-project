package com.s.solar_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/css/**")
                                .addResourceLocations("classpath:/static/css/");

                registry.addResourceHandler("/js/**")
                                .addResourceLocations("classpath:/static/js/");

                // Serve photos from both file system (for uploaded files) and classpath (for
                // bundled files)
                registry.addResourceHandler("/photo/**")
                                .addResourceLocations(
                                                "file:src/main/resources/static/photo/",
                                                "classpath:/static/photo/");

                registry.addResourceHandler("/images/**")
                                .addResourceLocations("classpath:/static/images/");

                // Add handler for uploaded files from uploads directory
                registry.addResourceHandler("/uploads/**")
                                .addResourceLocations("file:uploads/");
        }
}
