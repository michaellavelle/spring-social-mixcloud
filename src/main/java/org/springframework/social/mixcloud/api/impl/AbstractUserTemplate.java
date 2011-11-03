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

import java.util.List;

import org.springframework.social.mixcloud.api.MixcloudProfile;
import org.springframework.social.mixcloud.api.UserOperations;
import org.springframework.social.mixcloud.api.impl.json.MixcloudFavoritesResponse;
import org.springframework.web.client.RestTemplate;

/**
 * Base implementation for mixcloud api user-specific operations
 * 
 * @author Michael Lavelle
 */
public abstract class AbstractUserTemplate extends
		AbstractMixcloudResourceOperations implements UserOperations {

	private boolean authorizationRequiredForAllMethods;

	public AbstractUserTemplate(String accessToken, RestTemplate restTemplate,
			boolean isAuthorizedForUser,
			boolean authorizationRequiredForAllMethods) {
		super(accessToken, restTemplate, isAuthorizedForUser);
		this.authorizationRequiredForAllMethods = authorizationRequiredForAllMethods;
	}

	protected abstract String getUsersResourcePrefix();

	@Override
	protected String getApiResourceBaseUrl() {
		return getApiBaseUrl() + getUsersResourcePrefix();
	}

	@Override
	public MixcloudProfile getUserProfile() {
		if (authorizationRequiredForAllMethods) {
			requireAuthorization();
		}
		return restTemplate.getForObject(getApiResourceUrl(""),
				MixcloudProfile.class);
	}
	
	@Override
	public List<MixcloudItem> getFavorites() {
		if (authorizationRequiredForAllMethods) {
			requireAuthorization();
		}
		return restTemplate.getForObject(getApiResourceUrl("/favorites"),
				MixcloudFavoritesResponse.class).getItems();
	}
	

}
