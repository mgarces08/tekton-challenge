package com.tekton.challenge.service.client;

import com.tekton.challenge.config.AppProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ExternalPercentageClientMock implements ExternalPercentageClient {

  private final AppProperties appProperties;

  public ExternalPercentageClientMock(AppProperties appProperties){
      this.appProperties = appProperties;
  }

  @Override
  public BigDecimal fetchPercentage() {
    if (appProperties.getPercentage().getFail()) {
      throw new IllegalStateException("External percentage service is unavailable");
    }
    return appProperties.getPercentage().getMock();
  }
}
