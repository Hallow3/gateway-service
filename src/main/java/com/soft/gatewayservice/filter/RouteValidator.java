package com.soft.gatewayservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

//composant
@Component
public class RouteValidator {

    //liste des routes à ignorer pour le filtre
    public static final List<String> openApiEndPoints = List.of(
            "/authentication/client/register",
            "/authentication/admin/register",
            "/authentication/validate",
            "/authentication/generate"
    );
//ignorer
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndPoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
