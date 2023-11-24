package com.sidepj.ithurts;

import com.sidepj.ithurts.controller.interceptor.LoginInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories
public class IthurtsApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(IthurtsApplication.class, args);
	}

//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new LoginInterceptor())
//				.order(1)
//				.addPathPatterns("/**", "/map/report/**")
//				.excludePathPatterns("/json/**");
//	}
}
