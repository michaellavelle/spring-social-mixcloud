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

import org.springframework.web.client.RestTemplate;

/**
 * Base implementation for mixcloud api resource-specific operations
 * 
 * @author Michael Lavelle
 */
public abstract class AbstractMixcloudResourceOperations extends
		AbstractMixcloudOperations {

	public AbstractMixcloudResourceOperations(String accessToken,
			RestTemplate restTemplate, boolean isAuthorizedForUser) {
		super(accessToken, restTemplate, isAuthorizedForUser);
	}

	protected abstract String getApiResourceBaseUrl();

	protected String getApiResourceUrl(String resourcePath) {
		String resourceUri = getApiResourceBaseUrl() + resourcePath;
		String querySeperator = resourceUri.indexOf("?") == -1 ? "?" : "&";
		String accessTokenQuery = accessToken == null ? "" : (querySeperator
				+ "access_token=" + accessToken);
		return resourceUri + accessTokenQuery;
	}

}
