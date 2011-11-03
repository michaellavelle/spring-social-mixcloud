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

package org.springframework.social.mixcloud.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.social.test.client.RequestMatchers.method;
import static org.springframework.social.test.client.RequestMatchers.requestTo;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

import java.util.List;

import org.junit.Test;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.mixcloud.api.impl.MixcloudItem;

public class UserTemplateTest extends AbstractMixcloudApiTest {

	@Test
	public void getUserProfile_currentUser() {

		mockServer
				.expect(requestTo("https://api.mixcloud.com/me?access_token=someAccessToken"))
				.andExpect(method(GET))
				.andRespond(
						withResponse(jsonResource("testdata/full-profile"),
								responseHeaders));

		MixcloudProfile profile = mixcloud.meOperations().getUserProfile();
		assertBasicProfileData(profile);
	}
	
	@Test
	public void getFavorites_currentUser() {

		mockServer
				.expect(requestTo("https://api.mixcloud.com/me/favorites?access_token=someAccessToken"))
				.andExpect(method(GET))
				.andRespond(
						withResponse(jsonResource("testdata/favorites"),
								responseHeaders));

		List<MixcloudItem> favorites = mixcloud.meOperations().getFavorites();
		assertItemData(favorites.get(0));
	}
	
	

	@Test(expected = NotAuthorizedException.class)
	public void getUserProfile_currentUser_unauthorized() {
		unauthorizedMixcloud.meOperations().getUserProfile();
	}

	@Test
	public void getUserProfile_specificUserByUserId() {

		mockServer
				.expect(requestTo("https://api.mixcloud.com/cloudplaylists?access_token=someAccessToken"))
				.andExpect(method(GET))
				.andRespond(
						withResponse(jsonResource("testdata/full-profile"),
								responseHeaders));

		MixcloudProfile profile = mixcloud.usersOperations()
				.userOperations("cloudplaylists").getUserProfile();
		assertBasicProfileData(profile);
	}

	private void assertBasicProfileData(MixcloudProfile profile) {
		assertEquals("cloudplaylists", profile.getUsername());
		assertEquals("Cloud Playlists", profile.getName());
		assertEquals("http://www.mixcloud.com/cloudplaylists", profile.getUrl());
	}
	
	private void assertItemData(MixcloudItem item) {
		assertEquals("DJRobL - Indi Dance & Nu Disco Vol 2", item.getName());
		assertEquals("http://www.mixcloud.com/DJRobL/djrobl-indi-dance-nu-disco-vol-2/", item.getUrl());
	}


}
