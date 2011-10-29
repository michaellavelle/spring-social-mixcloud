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

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.mixcloud.api.Mixcloud;
import org.springframework.social.mixcloud.api.MixcloudProfile;

/**
 * Mixcloud ApiAdapter implementation.
 * 
 * @author Michael Lavelle
 */
public class MixcloudAdapter implements ApiAdapter<Mixcloud> {

	@Override
	public UserProfile fetchUserProfile(Mixcloud mixcloud) {
		MixcloudProfile profile = mixcloud.meOperations().getUserProfile();
		return new UserProfileBuilder().setName(profile.getName())
				.setUsername(profile.getUsername()).build();

	}

	@Override
	public void setConnectionValues(Mixcloud mixcloud, ConnectionValues values) {
		MixcloudProfile profile = mixcloud.meOperations().getUserProfile();
		values.setProviderUserId(profile.getUsername());
		values.setDisplayName(profile.getName());
		values.setProfileUrl(profile.getUrl());
	}

	@Override
	public boolean test(Mixcloud mixcloud) {
		try {
			mixcloud.meOperations().getUserProfile();
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

	@Override
	public void updateStatus(Mixcloud mixcloud, String arg1) {

	}

}
