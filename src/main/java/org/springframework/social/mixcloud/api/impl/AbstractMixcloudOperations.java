package org.springframework.social.mixcloud.api.impl;

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
import org.springframework.social.MissingAuthorizationException;
import org.springframework.web.client.RestTemplate;

/**
 * Base implementation for mixcloud api operations
 * 
 * @author Michael Lavelle
 */
public abstract class AbstractMixcloudOperations {

	protected final RestTemplate restTemplate;
	protected final boolean isAuthorizedForUser;
	protected final String accessToken;

	public AbstractMixcloudOperations(String accessToken,
			RestTemplate restTemplate, boolean isAuthorizedForUser) {
		this.restTemplate = restTemplate;
		this.isAuthorizedForUser = isAuthorizedForUser;
		this.accessToken = accessToken;
	}

	protected void requireAuthorization() {
		if (!isAuthorizedForUser) {
			throw new MissingAuthorizationException("mixcloud");
		}
	}

	protected String getApiBaseUrl() {
		return "https://api.mixcloud.com";
	}

}
