package com.thomasvitale.instrumentservice.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.UrlHandlerFilter;

@Configuration(proxyBeanMethods = false)
class WebConfig {

	/**
	 * This is for making the demo work locally when calling the app
	 * from a browser. Without the trailing slash, browsers would not
	 * call the local application. Instead, they would try to
	 * reach it on the internet.
	 */
	@Bean
	UrlHandlerFilter urlHandlerFilter() {
		return UrlHandlerFilter
				.trailingSlashHandler("/**").wrapRequest()
				.build();
	}

}
