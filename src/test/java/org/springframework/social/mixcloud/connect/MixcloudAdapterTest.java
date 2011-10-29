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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.mixcloud.api.MeOperations;
import org.springframework.social.mixcloud.api.Mixcloud;
import org.springframework.social.mixcloud.api.MixcloudProfile;

public class MixcloudAdapterTest {

	private MixcloudAdapter apiAdapter = new MixcloudAdapter();

	@SuppressWarnings("unchecked")
	private Mixcloud mixcloud = Mockito.mock(Mixcloud.class);

	@Test
	public void fetchProfile() {
		MeOperations meOperations = Mockito.mock(MeOperations.class);
		Mockito.when(mixcloud.meOperations()).thenReturn(meOperations);
		Mockito.when(meOperations.getUserProfile()).thenReturn(
				new MixcloudProfile("cloudplaylists", "Cloud Playlists",
						"http://www.mixcloud.com/cloudplaylists"));
		UserProfile profile = apiAdapter.fetchUserProfile(mixcloud);
		assertEquals("Cloud Playlists", profile.getName());
		assertEquals("Cloud", profile.getFirstName());
		assertEquals("Playlists", profile.getLastName());
		assertNull(profile.getEmail());
		assertEquals("cloudplaylists", profile.getUsername());
	}

	@Test
	public void setConnectionValues() {
		MeOperations meOperations = Mockito.mock(MeOperations.class);
		Mockito.when(mixcloud.meOperations()).thenReturn(meOperations);
		Mockito.when(meOperations.getUserProfile()).thenReturn(
				new MixcloudProfile("cloudplaylists", "Cloud Playlists",
						"http://www.mixcloud.com/cloudplaylists"));

		TestConnectionValues connectionValues = new TestConnectionValues();
		apiAdapter.setConnectionValues(mixcloud, connectionValues);
		assertEquals("Cloud Playlists", connectionValues.getDisplayName());
		assertNull(connectionValues.getImageUrl());
		assertEquals("http://www.mixcloud.com/cloudplaylists",
				connectionValues.getProfileUrl());
		assertEquals("cloudplaylists", connectionValues.getProviderUserId());
	}

	private static class TestConnectionValues implements ConnectionValues {

		private String displayName;
		private String imageUrl;
		private String profileUrl;
		private String providerUserId;

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getProfileUrl() {
			return profileUrl;
		}

		public void setProfileUrl(String profileUrl) {
			this.profileUrl = profileUrl;
		}

		public String getProviderUserId() {
			return providerUserId;
		}

		public void setProviderUserId(String providerUserId) {
			this.providerUserId = providerUserId;
		}

	}
}
