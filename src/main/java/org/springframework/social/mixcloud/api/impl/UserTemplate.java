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

import org.springframework.social.mixcloud.api.UserOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Implementation for mixcloud api currently authenticated user-specific
 * operations
 * 
 * @author Michael Lavelle
 */
public class UserTemplate extends AbstractUserTemplate implements
		UserOperations {

	private String username;

	public UserTemplate(String accessToken, String username,
			RestTemplate restTemplate, boolean isAuthorizedForUser) {
		super(accessToken, restTemplate, isAuthorizedForUser, false);
		this.username = username;
	}

	@Override
	protected String getUsersResourcePrefix() {
		return "/" + username;
	}

}
