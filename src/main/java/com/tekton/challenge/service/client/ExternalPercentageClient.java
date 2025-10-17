package com.tekton.challenge.service.client;

import java.math.BigDecimal;

public interface ExternalPercentageClient {
  BigDecimal fetchPercentage();
}