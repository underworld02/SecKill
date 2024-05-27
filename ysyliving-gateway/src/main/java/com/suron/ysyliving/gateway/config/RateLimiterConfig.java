package com.suron.ysyliving.gateway.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
/**
 * @author Suron
 * @version 1.0
 */



@Configuration
@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimiterConfig {

    private Map<String, LimitConfig> routes;
    private LimitConfig defaultConfig;

    public Map<String, LimitConfig> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, LimitConfig> routes) {
        this.routes = routes;
    }

    public LimitConfig getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(LimitConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public static class LimitConfig {
        private int maxVisits;
        private long seconds;

        public int getMaxVisits() {
            return maxVisits;
        }

        public void setMaxVisits(int maxVisits) {
            this.maxVisits = maxVisits;
        }

        public long getSeconds() {
            return seconds;
        }

        public void setSeconds(long seconds) {
            this.seconds = seconds;
        }
    }
}
