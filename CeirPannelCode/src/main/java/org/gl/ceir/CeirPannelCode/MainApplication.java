package org.gl.ceir.CeirPannelCode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@EnableFeignClients
@EnableAutoConfiguration
@SpringBootConfiguration
@ComponentScan(basePackages = "org.gl.ceir")
@PropertySource({ "file:${GUI_CONFIG_PATH}/application.properties", "file:${GUI_CONFIG_PATH}/messages.properties",
		"file:${GUI_CONFIG_PATH}/messages_km.properties" })
public class MainApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MainApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);

	}
}