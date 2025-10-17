package com.tekton.challenge.config;

import com.tekton.challenge.service.async.PersistingAsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

  private final PersistingAsyncExceptionHandler exceptionHandler;
  private final AppProperties appProperties;

  public AsyncConfig(PersistingAsyncExceptionHandler exceptionHandler, AppProperties appProperties) {
    this.exceptionHandler = exceptionHandler;
    this.appProperties = appProperties;
  }

  @Bean(name = "asyncExecutor")
  public Executor asyncExecutor() {
    ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
    ex.setThreadNamePrefix(appProperties.getAsync().getThreadNamePrefix());
    ex.setCorePoolSize(appProperties.getAsync().getCorePoolSize());
    ex.setMaxPoolSize(appProperties.getAsync().getMaxPoolSize());
    ex.setQueueCapacity(appProperties.getAsync().getQueueCapacity());
    ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    ex.initialize();
    return ex;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return exceptionHandler;
  }
}
