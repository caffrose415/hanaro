package com.hana7.hanaro.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${upload.path}") // /resources/static/upload
	private String uploadBase;

	@Value("${spring.servlet.multipart.location}") // /resources/static/origin
	private String originBase;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry reg) {
		String up = Paths.get(uploadBase).toAbsolutePath().toString().replace("\\","/");
		String org = Paths.get(originBase).toAbsolutePath().toString().replace("\\","/");
		if (!up.endsWith("/")) up += "/";
		if (!org.endsWith("/")) org += "/";

		reg.addResourceHandler("/upload/**")
			.addResourceLocations("file:" + up);
		reg.addResourceHandler("/origin/**")
			.addResourceLocations("file:" + org);
	}
}
