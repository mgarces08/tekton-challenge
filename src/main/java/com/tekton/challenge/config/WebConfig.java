package com.tekton.challenge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final AppProperties appProperties;

  public WebConfig(AppProperties appProperties) {
    this.appProperties = appProperties;
  }

  @Bean
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
    builder.modules(new JavaTimeModule());
    var om = builder.build();
    om.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
    return om;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedOrigins(appProperties.getCors().getAllowedOrigins())
      .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
      .allowedHeaders("*");
  }
}
