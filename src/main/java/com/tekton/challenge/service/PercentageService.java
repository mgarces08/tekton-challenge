package com.tekton.challenge.service;

import com.tekton.challenge.service.client.ExternalPercentageClient;
import com.tekton.challenge.service.exception.PercentageUnavailableException;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class PercentageService {

  private final ExternalPercentageClient client;
  private final CacheManager cacheManager;
  private Cache percentageCache;

  public PercentageService(ExternalPercentageClient client, CacheManager cacheManager) {
    this.client = client;
    this.cacheManager = cacheManager;
  }

  @PostConstruct
  void init() {
    this.percentageCache = cacheManager.getCache("percentage");
    if (this.percentageCache == null) {
      throw new PercentageUnavailableException("Cache 'percentage' not found.");
    }
  }

  public BigDecimal getPercentage() {
    try {
      BigDecimal percentage = Objects.requireNonNull(client.fetchPercentage(), "null percentage");
      percentageCache.put("current", percentage);
      return percentage;

    } catch (Exception ex) {
      BigDecimal cached = percentageCache.get("current", BigDecimal.class);
      if (cached != null) return cached;
      throw new PercentageUnavailableException("Percentage is empty");
    }
  }
}
