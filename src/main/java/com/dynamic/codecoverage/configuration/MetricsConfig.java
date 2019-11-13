package com.dynamic.codecoverage.configuration;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class MetricsConfig {

	@Bean
	public MeterRegistryCustomizer<MeterRegistry> commonTags() {
		return r -> r.config().commonTags("app", "code-coverage", "env", "dev");
	}

}
