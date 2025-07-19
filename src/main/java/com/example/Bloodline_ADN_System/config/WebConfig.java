package com.example.Bloodline_ADN_System.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map tất cả đường dẫn /upload/** tới thư mục gốc trên ổ cứng
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:upload/");
        // nếu thư mục là ./upload trong thư mục project
    }
}

