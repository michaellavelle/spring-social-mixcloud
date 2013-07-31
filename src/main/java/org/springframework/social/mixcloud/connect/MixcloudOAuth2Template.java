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
package org.springframework.social.mixcloud.connect;

import java.util.Map;

import org.springframework.social.mixcloud.api.impl.MixcloudErrorHandler;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.util.MultiValueMap;

/**
 * Mixcloud-specific extension of OAuth2Template
 * 
 * @author Michael Lavelle
 */
public class MixcloudOAuth2Template extends OAuth2Template {

	public MixcloudOAuth2Template(String clientId, String clientSecret) {
		super(clientId, clientSecret,
				"https://www.mixcloud.com/oauth/authorize",
				"https://www.mixcloud.com/oauth/access_token");
		// Wrap the request factory with a BufferingClientHttpRequestFactory so
		// that the error handler can do repeat reads on the response.getBody()
		setRequestFactory(ClientHttpRequestFactorySelector
				.bufferRequests(getRestTemplate().getRequestFactory()));
		// Set our mixcloud-specific error handler for OAuth error handling
		getRestTemplate().setErrorHandler(new MixcloudErrorHandler());
		setUseParametersForClientAuthentication(true);
	}
	
	

	/**
	 * OAuth2Template assumes the request for an access token is a POST request
	 * - hence the method name Actually mixcloud's access grant request is a GET
	 * request
	 */
	@SuppressWarnings("unchecked")
	protected AccessGrant postForAccessGrant(String accessTokenBaseUrl,
			MultiValueMap<String, String> parameters) {

		Map<String, Object> response = getRestTemplate().getForObject(
				getAccessTokenUrlAsTemplate(accessTokenBaseUrl), Map.class,
				parameters.toSingleValueMap());
		return createAccessGrant((String) response.get("access_token"), null,
				null, null, response);
	}

	private String getAccessTokenUrlAsTemplate(String accessTokenBaseUrl) {
		return accessTokenBaseUrl
				+ "?client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}&code={code}";

	}
}
