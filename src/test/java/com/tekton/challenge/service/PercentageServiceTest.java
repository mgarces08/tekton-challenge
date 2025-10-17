package com.tekton.challenge.service;

import com.tekton.challenge.service.client.ExternalPercentageClient;
import com.tekton.challenge.service.exception.PercentageUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PercentageServiceTest {

  CacheManager cacheManager;
  Cache cache;
  ExternalPercentageClient client;

  @BeforeEach
  void setup() {
    cacheManager = mock(CacheManager.class);
    cache = mock(Cache.class);
    when(cacheManager.getCache("percentage")).thenReturn(cache);
    client = mock(ExternalPercentageClient.class);
  }

  private PercentageService newService() {
    PercentageService s = new PercentageService(client, cacheManager);
    s.init();
    return s;
  }

  @Test
  void testGetPercentageSuccessPutsCache() {
    when(client.fetchPercentage()).thenReturn(new BigDecimal("15"));
    PercentageService s = newService();

    BigDecimal p = s.getPercentage();
    assertThat(p).isEqualByComparingTo("15");
    verify(cache).put(eq("current"), eq(new BigDecimal("15")));
  }

  @Test
  void testGetPercentageFallbackToCacheWhenClientFailsAndCachePresent() {
    when(client.fetchPercentage()).thenThrow(new IllegalStateException("down"));
    when(cache.get("current", BigDecimal.class)).thenReturn(new BigDecimal("12"));

    PercentageService s = newService();

    BigDecimal p = s.getPercentage();
    assertThat(p).isEqualByComparingTo("12");
  }

  @Test
  void testGetPercentageThrowsWhenClientFailsAndCacheEmpty() {
    when(client.fetchPercentage()).thenThrow(new RuntimeException("down"));
    when(cache.get("current", BigDecimal.class)).thenReturn(null);

    PercentageService s = newService();

    assertThatThrownBy(s::getPercentage)
        .isInstanceOf(PercentageUnavailableException.class)
        .hasMessageContaining("empty");
  }

  @Test
  void testInitThrowsIfCacheMissing() {
    when(cacheManager.getCache("percentage")).thenReturn(null);
    PercentageService s = new PercentageService(client, cacheManager);
    assertThatThrownBy(s::init).isInstanceOf(PercentageUnavailableException.class)
        .hasMessageContaining("not found");
  }
}
