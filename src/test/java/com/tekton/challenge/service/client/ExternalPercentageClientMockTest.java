package com.tekton.challenge.service.client;

import com.tekton.challenge.config.AppProperties;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExternalPercentageClientMockTest {

  @Test
  void testFetchPercentageReturnsMockValueWhenFailIsFalse() {
    AppProperties props = new AppProperties();
    props.getPercentage().setFail(false);
    props.getPercentage().setMock(new BigDecimal("0.1234"));

    ExternalPercentageClientMock client = new ExternalPercentageClientMock(props);

    assertThat(client.fetchPercentage()).isEqualByComparingTo("0.1234");
  }

  @Test
  void testFetchPercentageThrowsWhenFailIsTrue() {
    AppProperties props = new AppProperties();
    props.getPercentage().setFail(true);
    props.getPercentage().setMock(new BigDecimal("0.50"));

    ExternalPercentageClientMock client = new ExternalPercentageClientMock(props);

    assertThatThrownBy(client::fetchPercentage)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("unavailable");
  }
}
