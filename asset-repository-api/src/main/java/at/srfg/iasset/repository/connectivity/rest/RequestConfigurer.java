/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package at.srfg.iasset.repository.connectivity.rest;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.MatrixParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import org.glassfish.jersey.message.internal.MediaTypes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.srfg.iasset.repository.api.annotation.Base64Encoded;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

public class RequestConfigurer {

	private final String baseUrl;
	private final Client client;
	private final Method method;
	private final Object[] parameter;

	public RequestConfigurer(Client client, String baseUrl, Method method, Object[] parameter) {
		this.client = client;
		this.baseUrl = baseUrl;
		this.method = method;
		this.parameter = parameter;
	}

	public Builder configure() {
		WebTarget target = computeTarget();
		target = addQueryParameters(target);
		target = addMatrixParameters(target);
		Builder request = target.request();
		request = addHeaders(request);
		return request;
	}

	public String getRequestUrl() {
		WebTarget target = computeTarget();
		return target.getUri().toString();
	}

	private WebTarget computeTarget() {
		String serviceUrl = baseUrl;
		if (method.isAnnotationPresent(Path.class)) {
			String path = method.getAnnotation(Path.class).value();
			serviceUrl = computeUrl(serviceUrl, path);
		}
		else if (method.isAnnotationPresent(RequestMapping.class)) {
			String[] path = method.getAnnotation(RequestMapping.class).path();
			if ( path != null && path.length > 0) { 
				serviceUrl = computeUrl(serviceUrl, path[0]);
			}
		}
		return client.target(replacePathParams(serviceUrl, method, parameter));
	}

	private String computeUrl(String serviceUrl, String path) {
		if (serviceUrl.endsWith("/") && path.startsWith("/")) {
			return serviceUrl + path.substring(1, path.length());
		} else if (!serviceUrl.endsWith("/") && !path.startsWith("/")) {
			return serviceUrl + "/" + path;
		}
		return serviceUrl + path;
	}

	private String replacePathParams(String serviceUrl, Method method, Object[] parameter) {
		String result = serviceUrl;
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterAnnotations.length; i++) {
			Annotation[] annotations = parameterAnnotations[i];
			String paramName = extractPathParam(annotations);
			if (paramName != null) {
				if ( isBase64Encoded(annotations)) {
					result = result.replace(getPathSegment(result, paramName), base64Encode(parameter[i]));
				} else {
					result = result.replace(getPathSegment(result, paramName), encode(parameter[i]));
				}
			}
		}
		validatePath(result);
		return result;
	}
	private boolean isBase64Encoded(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType() == Base64Encoded.class) {
				return true;
			}
		}
		return false;
	}
	private String base64Encode(Object obj) {
		return Base64.getEncoder().encodeToString(obj.toString().getBytes());
	}
	private String encode(Object obj) {
		try {
			return URLEncoder.encode(obj.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return obj.toString();
		}
		
	}
	private String getPathSegment(String fullPath, String pathParameter) {
		Pattern pattern = Pattern.compile(".*?(\\{" + pathParameter + ":*?.*?\\}).*?");
		Matcher matcher = pattern.matcher(fullPath);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return pathParameter;
	}

	private String extractPathParam(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			
			if (annotation.annotationType() == PathParam.class) {   		// jersey
				return ((PathParam) annotation).value();
			} else if (annotation.annotationType() == PathVariable.class) { // open api 
				return ((PathVariable) annotation).value();
			} else if ( annotation.annotationType() == Parameter.class) {
				if (  ((Parameter) annotation).in() == ParameterIn.PATH ) { // open api
					return ((Parameter) annotation).name();
				}
//			} else if ( annotation.annotationType() == RequestParam.class) { // spring web
//				return ((RequestParam)annotation).name();
			}
		}
		return null;
	}

	private void validatePath(String path) {
		Pattern pattern = Pattern.compile(".*?\\{(.+)\\}.*?");
		Matcher matcher = pattern.matcher(path);
		if (matcher.matches()) {
			String message = "Path " + path + " has undefined path parameters: ";
			for (int i = 1; i <= matcher.groupCount(); i++) {
				message += matcher.group(i) + " ";
			}
			throw new IllegalStateException(message);
		}
	}

	private WebTarget addQueryParameters(WebTarget target) {
		WebTarget result = target;
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterAnnotations.length; i++) {
			Annotation[] annotations = parameterAnnotations[i];
			String paramName = extractQueryParam(annotations);
			if (paramName != null) {
				result = result.queryParam(paramName, parameter[i]);
			}
		}
		return result;
	}

	private String extractQueryParam(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			// Jersey Annotation
			if (annotation.annotationType() == QueryParam.class) {
				return ((QueryParam) annotation).value();
			}
			// Spring Annotation
			else if (annotation.annotationType() == RequestParam.class) {
				return ((RequestParam) annotation).value();
			}
			// 
			else if ( annotation.annotationType() == Parameter.class)  {
				if (  ((Parameter) annotation).in() == ParameterIn.QUERY ) { // open api
					if ( ((Parameter)annotation).name() != null && ((Parameter)annotation).name().length()>0) {
						return ((Parameter) annotation).name();
					}
				}
			}
		}
		return null;
	}

	private WebTarget addMatrixParameters(WebTarget target) {
		WebTarget result = target;
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterAnnotations.length; i++) {
			Annotation[] annotations = parameterAnnotations[i];
			String paramName = extractMatrixParam(annotations);
			if (paramName != null) {
				result = result.matrixParam(paramName, parameter[i]);
			}
		}
		return result;
	}

	private String extractMatrixParam(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType() == MatrixParam.class) {
				return ((MatrixParam) annotation).value();
			}
		}
		return null;
	}

	private Builder addHeaders(Builder request) {
		Builder result = request;
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterAnnotations.length; i++) {
			Annotation[] annotations = parameterAnnotations[i];
			String paramName = extractHeaderParam(annotations);
			if (paramName != null) {
				result = result.header(paramName, parameter[i]);
			}
		}
		result = addAcceptHeader(request);
		return result;
	}

	private Builder addAcceptHeader(Builder request) {
		Builder result = request;
		MediaType accept = MediaType.WILDCARD_TYPE;
		if (method.isAnnotationPresent(Produces.class)) {
			accept = MediaTypes.createFrom(method.getAnnotation(Produces.class)).get(0);
			result = result.header(HttpHeaders.ACCEPT, accept);
		}
		else if (method.isAnnotationPresent(RequestMapping.class)) {
			RequestMapping mapping = method.getAnnotation(RequestMapping.class);
			String[] produces = mapping.produces();
			if (produces.length == 0 ) {
				produces = new String[] {MediaType.APPLICATION_JSON};
			}
			accept = MediaTypes.createFrom((produces)).get(0);
			result = result.header(HttpHeaders.ACCEPT, accept);
		}
		return result;
	}

	private String extractHeaderParam(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			// Jersey Annotation
			if (annotation.annotationType() == HeaderParam.class) {
				return ((HeaderParam) annotation).value();
			}
			// Sprint Annotation
			else if (annotation.annotationType() == RequestHeader.class) {
				return ((RequestHeader) annotation).value();
			}
			else if ( annotation.annotationType() == Parameter.class) {
				if (  ((Parameter) annotation).in() == ParameterIn.HEADER ) { // open api
					return ((Parameter) annotation).name();
				}
			}
		}
		return null;
	}
}
