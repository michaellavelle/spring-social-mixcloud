/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.mixcloud.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.InternalServerErrorException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.social.ServerDownException;
import org.springframework.social.SocialException;
import org.springframework.social.UncategorizedApiException;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 * Subclass of {@link DefaultResponseErrorHandler} that handles errors from
 * Mixcloud's API, interpreting them into appropriate exceptions.
 * 
 * @author Michael Lavelle
 */
public class MixcloudErrorHandler extends DefaultResponseErrorHandler {

	private final static String INCORRECT_CLIENT_CREDENTIALS = "incorrect_client_credentials";
	private final static String BAD_VERIFICATION_CODE = "bad_verification_code";
	private final static String REDIRECT_URI_MISMATCH = "redirect_uri_mismatch";

	private final static List<String> AUTHORIZATION_FAILURE_MESSAGES = Arrays
			.asList(INCORRECT_CLIENT_CREDENTIALS, BAD_VERIFICATION_CODE,
					REDIRECT_URI_MISMATCH);

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		SocialException exception = extractExceptionFromResponse(response);

		if (exception == null) {
			handleUncategorizedError(response, exception);
		}

		handleMixcloudError(response.getStatusCode(), exception);

		// if not otherwise handled, do default handling and wrap with
		// UncategorizedApiException
		handleUncategorizedError(response, exception);
	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		if (super.hasError(response)) {
			return true;
		}
		// only bother checking the body for errors if we get past the default
		// error check
		String content = readFully(response.getBody());
		return content.startsWith("{\"error\":");
	}

	/**
	 * Examines the error data returned from Mixcloud and throws the most
	 * applicable exception.
	 * 
	 * @param errorDetails
	 *            a Map containing an "error"
	 */
	void handleMixcloudError(HttpStatus statusCode, SocialException errorDetails) {
		if (statusCode == HttpStatus.OK) {

		} else if (statusCode == HttpStatus.BAD_REQUEST) {
			if (errorDetails instanceof UncategorizedApiException) {
				String message = errorDetails.getMessage();
				if (AUTHORIZATION_FAILURE_MESSAGES.contains(message)) {
					throw new NotAuthorizedException(message);
				} else {
					throw errorDetails;

				}
			} else {
				throw errorDetails;
			}

		} else if (statusCode == HttpStatus.UNAUTHORIZED) {
			throw new NotAuthorizedException(errorDetails.getMessage());
		} else if (statusCode == HttpStatus.FORBIDDEN) {

			throw new OperationNotPermittedException(errorDetails.getMessage());
		} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new InternalServerErrorException(errorDetails.getMessage());
		} else if (statusCode == HttpStatus.SERVICE_UNAVAILABLE) {
			throw new ServerDownException(errorDetails.getMessage());
		}
	}



	private void handleUncategorizedError(ClientHttpResponse response,
			SocialException errorDetails) {
		try {
			super.handleError(response);
		} catch (Exception e) {
			if (errorDetails != null) {
				throw errorDetails;
			} else {
				throw new UncategorizedApiException(
						"No error details from Mixcloud", e);
			}
		}
	}

	/*
	 * Attempts to extract Mixcloud error details from the response. Returns
	 * null if the response doesn't match the expected JSON error response.
	 */
	@SuppressWarnings("rawtypes")
	private SocialException extractExceptionFromResponse(
			ClientHttpResponse response) throws IOException {

		ObjectMapper mapper = new ObjectMapper(new JsonFactory());

		try {
			String json = readFully(response.getBody());
			Map<String, String> responseMap = mapper
					.<Map<String, String>> readValue(json,
							new TypeReference<Map<String, Object>>() {
							});
			if (responseMap.containsKey("error")) {
				Object error = responseMap.get("error");
				if (error instanceof String) {
					return new UncategorizedApiException(
							responseMap.get("error"), new RuntimeException(
									responseMap.get("error")));
				} else if (error instanceof Map) {
					Map errorMap = (Map) error;
					String type = (String) ((Map) error).get("type");
					if ("OAuthException".equals(type)) {
						return new InvalidAuthorizationException(
								(String) errorMap.get("message"));
					}
					if ("ResourceNotFoundException".equals(type)) {
						return new ResourceNotFoundException(
								(String) errorMap.get("message"));
					}
					return new UncategorizedApiException(
							responseMap.get("error"), new RuntimeException(
									responseMap.get("error")));
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (JsonParseException e) {
			return null;
		}
	}

	private String readFully(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		while (reader.ready()) {
			sb.append(reader.readLine());
		}
		return sb.toString();
	}
}
