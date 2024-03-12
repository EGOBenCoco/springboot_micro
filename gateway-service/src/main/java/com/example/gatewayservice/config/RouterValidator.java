/*
package com.example.gatewayservice.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouterValidator {

	public static final List<String> openApiEndpoints = List.of(
			"/auth/register",
			"/auth/login"
	);

	public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints.stream()
			.noneMatch(uri -> request.getURI().getPath().contains(uri));

	public static final Map<String, List<String>> roleBaseApiEndpoints = 
			Map.of(
					//Roles.ADMIN.label, List.of(Urls.GET_CUS.label),
					Roles.USER.label, List.of(Urls.GET_DATA.label,Urls.GET_TEST.label,Urls.GET_CUS.label));

*/
/*	public MapCheck<ServerHttpRequest, String> roleBaseApi = (request, role) -> {
		if (roleBaseApiEndpoints.containsKey(role))
			return !roleBaseApiEndpoints.get(role).stream().noneMatch(uri -> request.getURI().getPath().contains(uri));

		return false;
	};*//*


public MapCheck<ServerHttpRequest, List<String>> roleBaseApi = (request, roles) -> {
	if (roles != null && !roles.isEmpty()) {
		for (String role : roles) {
			if (roleBaseApiEndpoints.containsKey(role)) {
				if (roleBaseApiEndpoints.get(role).stream().anyMatch(uri -> request.getURI().getPath().contains(uri))) {
					return true;
				}
			}
		}
	}
	return false;
};
	@FunctionalInterface
	public interface MapCheck<T, U> {
		 Boolean check(T t, U u);
	}
}
*/
