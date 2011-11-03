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
package org.springframework.social.mixcloud.api.impl.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.social.mixcloud.api.MixcloudProfile;
import org.springframework.social.mixcloud.api.impl.MixcloudItem;

/**
 * Jackson module for setting up mixin annotations on Mixcloud model types. This
 * enables the use of Jackson annotations without directly annotating the model
 * classes themselves.
 * 
 * @author Michael Lavelle
 */
public class MixcloudModule extends SimpleModule {

	public MixcloudModule() {
		super("MixcloudProfile", new Version(1, 0, 0, null));
	}

	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(MixcloudProfile.class,
				MixcloudProfileMixin.class);
		context.setMixInAnnotations(MixcloudItem.class,
				MixcloudItemMixin.class);

	}
}
