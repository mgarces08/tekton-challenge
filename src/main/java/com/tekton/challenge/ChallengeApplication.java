package com.tekton.challenge;

import com.tekton.challenge.config.AppProperties;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
public class ChallengeApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ChallengeApplication.class);
	public static final Marker CRLF_SAFE_MARKER = MarkerFactory.getMarker("CRLF_SAFE");
	private final Environment env;

	public ChallengeApplication(Environment env) {
		this.env = env;
	}

	@PostConstruct
	public void initApplication() {
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (activeProfiles.contains("dev")) {
			LOG.error(
					"You have misconfigured your application! It should not run " + "with the 'dev' profile."
			);
		}
		if (activeProfiles.contains("prod")) {
			LOG.error(
					"You have misconfigured your application! It should not " + "run with the 'prod' profile."
			);
		}
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ChallengeApplication.class);
		app.setDefaultProperties(Map.of("spring.profiles.default", "dev"));
		Environment env = app.run(args).getEnvironment();
		logApplicationStartup(env);
	}

	private static void logApplicationStartup(Environment env) {
		String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
		String applicationName = env.getProperty("spring.application.name");
		String serverPort = env.getProperty("server.port");
		String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path"))
				.filter(StringUtils::isNotBlank)
				.orElse("/");
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LOG.warn("The host name could not be determined, using `localhost` as fallback");
		}
		LOG.info(CRLF_SAFE_MARKER,
				"""
                ----------------------------------------------------------
                \tApplication '{}' is running! Access URLs:
                \tLocal: \t\t{}://localhost:{}{}
                \tExternal: \t{}://{}:{}{}
                \tProfile(s): \t{}
                ----------------------------------------------------------""",
				applicationName,
				protocol,
				serverPort,
				contextPath,
				protocol,
				hostAddress,
				serverPort,
				contextPath,
				env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
		);
	}
}
