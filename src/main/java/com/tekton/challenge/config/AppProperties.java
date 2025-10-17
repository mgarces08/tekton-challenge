package com.tekton.challenge.config;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {

    @NotNull
    private final Percentage percentage = new Percentage();
    @NotNull
    private final Cors cors = new Cors();
    @NotNull
    private final Async async = new Async();

    public Percentage getPercentage() {
        return percentage;
    }

    public Cors getCors() {
        return cors;
    }

    public Async getAsync() {
        return async;
    }

    public static class Percentage {

        private boolean fail;
        @NotNull @DecimalMin("0") @DecimalMax("100")
        private BigDecimal mock;

        public boolean getFail() {
            return fail;
        }

        public void setFail(boolean fail) {
            this.fail = fail;
        }

        public BigDecimal getMock() {
            return mock;
        }

        public void setMock(BigDecimal mock) {
            this.mock = mock;
        }
    }

    public static class Cors {

        @NotNull
        private String allowedOrigins;

        public String getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }

    public static class Async {

        @NotNull
        private String threadNamePrefix;
        @NotNull
        private int corePoolSize;
        @NotNull
        private int maxPoolSize;
        @NotNull
        private int queueCapacity;

        public String getThreadNamePrefix() {
            return threadNamePrefix;
        }

        public void setThreadNamePrefix(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }
}
