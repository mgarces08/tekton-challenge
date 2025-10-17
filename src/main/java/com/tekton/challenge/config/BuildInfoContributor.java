package com.tekton.challenge.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BuildInfoContributor implements InfoContributor {
  private final Environment env;

  public BuildInfoContributor(Environment env) {
    this.env = env;
  }

  @Override
  public void contribute(Info.Builder builder) {
    builder.withDetail("app", Map.of(
            "description", "Challenge Backend",
            "env", String.join(",", env.getActiveProfiles())
    ));
  }
}
