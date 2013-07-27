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

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.mixcloud.api.MeOperations;
import org.springframework.social.mixcloud.api.Mixcloud;
import org.springframework.social.mixcloud.api.UsersOperations;
import org.springframework.social.mixcloud.api.impl.json.MixcloudModule;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Template for interacting with mixcloud api
 * 
 * @author Michael Lavelle
 */
public class MixcloudTemplate extends AbstractOAuth2ApiBinding implements
		Mixcloud {

	private MeOperations meOperations;
	private UsersOperations usersOperations;

	private ObjectMapper objectMapper;

	/**
	 * Create a new instance of MixcloudTemplate. This constructor creates a
	 * new MixcloudTemplate able to perform unauthenticated operations against
	 * Mixcloud's API. Some operations do not require OAuth authentication.
	 * For example, retrieving a specified user's profile or feed does not
	 * require authentication . A MixcloudTemplate created with this
	 * constructor will support those operations. Those operations requiring
	 * authentication will throw {@link NotAuthorizedException}.
	 */
	public MixcloudTemplate() {
		initialize(null);
	}

	/**
	 * Create a new instance of MixcloudTemplate. This constructor creates the
	 * MixcloudTemplate using a given access token.
	 * 
	 * @param accessToken
	 *            An access token given by Mixcloud after a successful OAuth 2
	 *            authentication
	 */
	public MixcloudTemplate(String accessToken) {
		super(accessToken);
		initialize(accessToken);
	}

	@Override
	protected List<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> messageConverters = super
				.getMessageConverters();
		messageConverters.add(new ByteArrayHttpMessageConverter());
		return messageConverters;
	}

	@Override
	public MeOperations meOperations() {

		return meOperations;
	}

	@Override
	public UsersOperations usersOperations() {
		return usersOperations;
	}

	private void initSubApis(String accessToken) {
		meOperations = new MeTemplate(accessToken, getRestTemplate(),
				isAuthorized());
		usersOperations = new UsersTemplate(accessToken, getRestTemplate(),
				isAuthorized());

	}

	// private helpers
	private void initialize(String accessToken) {
		// Wrap the request factory with a BufferingClientHttpRequestFactory so
		// that the error handler can do repeat reads on the response.getBody()

		super.setRequestFactory(ClientHttpRequestFactorySelector
				.bufferRequests(getRestTemplate().getRequestFactory()));
		initSubApis(accessToken);

	}
	
	

	@Override
	protected void configureRestTemplate(RestTemplate restTemplate) {
		restTemplate.setErrorHandler(new MixcloudErrorHandler());
	}

	@Override
	protected MappingJackson2HttpMessageConverter getJsonMessageConverter() {
		MappingJackson2HttpMessageConverter converter = super
				.getJsonMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(new MediaType("text","javascript")));
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new MixcloudModule());
		converter.setObjectMapper(objectMapper);
		return converter;
	}


}
