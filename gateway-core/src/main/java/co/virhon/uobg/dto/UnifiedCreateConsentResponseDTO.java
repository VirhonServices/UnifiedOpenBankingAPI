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

package co.virhon.uobg.dto;

import co.virhon.uobg.domain.consent.ConsentLinks;
import co.virhon.uobg.domain.consent.ConsentStatus;
import co.virhon.uobg.domain.consent.ScaMethod;

public class UnifiedCreateConsentResponseDTO {
    private String consentId;
    private ConsentStatus consentStatus;
    private String psuMessage;
    private ScaMethod scaMethods;
    private ConsentLinks links;

    // Getters/Setters/Builders

    public String getConsentId() {
        return consentId;
    }

    public void setConsentId(String consentId) {
        this.consentId = consentId;
    }

    public ConsentStatus getConsentStatus() {
        return consentStatus;
    }

    public void setConsentStatus(ConsentStatus consentStatus) {
        this.consentStatus = consentStatus;
    }

    public String getPsuMessage() {
        return psuMessage;
    }

    public void setPsuMessage(String psuMessage) {
        this.psuMessage = psuMessage;
    }

    public ScaMethod getScaMethods() {
        return scaMethods;
    }

    public void setScaMethods(ScaMethod scaMethods) {
        this.scaMethods = scaMethods;
    }

    public ConsentLinks getLinks() {
        return links;
    }

    public void setLinks(ConsentLinks links) {
        this.links = links;
    }
}