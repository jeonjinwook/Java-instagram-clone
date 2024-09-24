package com.Java_instagram_clone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploadFile/**")
        .addResourceLocations("classpath:/static/uploadFile/")
        .setCachePeriod(60 * 60 * 24 * 365);
  }
}
