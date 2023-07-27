package com.example.Anh123.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class MVCConfig implements WebMvcConfigurer {
	public MVCConfig() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path bookImageDir = Paths.get("./imgs");
		String bookImagePath = bookImageDir.toFile().getAbsolutePath();

		registry.addResourceHandler("/imgs/**").addResourceLocations("file:/" + bookImagePath + "/");
	}
}
