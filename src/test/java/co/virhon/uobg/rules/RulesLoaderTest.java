/*
 * Copyright 2026 Viktor Honcharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.virhon.uobg.rules;

import co.virhon.uobg.domain.rules.ApiRulesConfig;
import co.virhon.uobg.domain.rules.RulesLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RulesLoaderTest {

    @Test
    void loadRulesTest() {
        RulesLoader loader = new RulesLoader();
        ApiRulesConfig config = loader.loadRulesFromClasspath("rules/ua/privatbank/consent.json");

        assertNotNull(config);
        assertTrue(config.getEndpoints().containsKey("createConsent"));
        assertEquals("/v2/consents/account-access",
                config.getEndpoints().get("createConsent").getUrl());
    }

}
