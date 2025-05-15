package com.soft.gatewayservice;

import com.soft.gatewayservice.filter.AuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}


	//configuration dynamique
//	@Bean
//	DiscoveryClientRouteDefinitionLocator dynamicRoute(ReactiveDiscoveryClient rdc, DiscoveryLocatorProperties dlp){
//		return new DiscoveryClientRouteDefinitionLocator(rdc,dlp);
//	}

	@Bean
	RouteLocator routes(RouteLocatorBuilder builder, AuthenticationFilter authenticationFilter){
		RouteLocator routes;
		routes = builder.routes()
				.route(r->r.path("/recip/enable/**").uri("lb://RECIP-SERVICE"))
				.route(r->r.path("/recip/name/**").uri("lb://RECIP-SERVICE"))
				.route(r->r.path("/step/recip/0**").uri("lb://STEP-SERVICE"))
				.route(r->r.path("/step/video/**").uri("lb://STEP-SERVICE"))
				.route(r->r.path("/recip/unique/**").uri("lb://RECIP-SERVICE"))
				.route(r->r.path("/recip/subcategory/**").uri("lb://RECIP-SERVICE"))
				.route(r->r.path("/category/all/**").uri("lb://CATEGORY-SERVICE"))
				.route(r->r.path("/category/search/**").uri("lb://CATEGORY-SERVICE"))
				.route(r->r.path("/category/subcategory/search/**").uri("lb://CATEGORY-SERVICE"))
				.route(r->r.path("/category/subcategory/**").uri("lb://CATEGORY-SERVICE"))
				.route(r->r.path("/category/**").filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))).uri("lb://CATEGORY-SERVICE"))
				.route(r->r.path("/recip/**").filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))).uri("lb://RECIP-SERVICE"))
				.route(r->r.path("/step/**").filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))).uri("lb://STEP-SERVICE"))
				.route(r->r.path("/authentication/**").uri("lb://AUTHENTICATION-SERVICE"))
				.build();

		return routes;
	}

}
