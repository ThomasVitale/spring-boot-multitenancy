package com.thomasvitale.instrumentservice.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class PathConfig implements WebMvcConfigurer {

	/**
	 * This is for making the demo work locally when calling the app
	 * from a browser. Without the trailing slash, browsers would not
	 * call the local application. Instead, they would try to
	 * reach it on the internet.
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseTrailingSlashMatch(true);
	}

}
