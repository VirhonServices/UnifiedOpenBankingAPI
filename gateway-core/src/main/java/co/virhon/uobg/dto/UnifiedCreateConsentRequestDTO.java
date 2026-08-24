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

import co.virhon.uobg.domain.consent.ConsentAccess;
import co.virhon.uobg.domain.consent.ConsentType;

import java.time.LocalDate;

public class UnifiedCreateConsentRequestDTO {
    // Данные тела запроса
    private ConsentAccess access;
    private ConsentType consentType;
    private Boolean recurringIndicator;
    private LocalDate validTo;
    private Integer frequencyPerDay;

    // Контекстные параметры (извлеченные из HTTP-заголовков)
    private String bankId;
    private String psuId;
    private String psuIdType;
    private String psuIpAddress;
    private String tppRedirectUri;
    private String tppNokRedirectUri;
    // Getters/Setters/Builders


    public ConsentAccess getAccess() {
        return access;
    }

    public void setAccess(ConsentAccess access) {
        this.access = access;
    }

    public ConsentType getConsentType() {
        return consentType;
    }

    public void setConsentType(ConsentType consentType) {
        this.consentType = consentType;
    }

    public Boolean getRecurringIndicator() {
        return recurringIndicator;
    }

    public void setRecurringIndicator(Boolean recurringIndicator) {
        this.recurringIndicator = recurringIndicator;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public Integer getFrequencyPerDay() {
        return frequencyPerDay;
    }

    public void setFrequencyPerDay(Integer frequencyPerDay) {
        this.frequencyPerDay = frequencyPerDay;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getPsuId() {
        return psuId;
    }

    public void setPsuId(String psuId) {
        this.psuId = psuId;
    }

    public String getPsuIdType() {
        return psuIdType;
    }

    public void setPsuIdType(String psuIdType) {
        this.psuIdType = psuIdType;
    }

    public String getPsuIpAddress() {
        return psuIpAddress;
    }

    public void setPsuIpAddress(String psuIpAddress) {
        this.psuIpAddress = psuIpAddress;
    }

    public String getTppRedirectUri() {
        return tppRedirectUri;
    }

    public void setTppRedirectUri(String tppRedirectUri) {
        this.tppRedirectUri = tppRedirectUri;
    }

    public String getTppNokRedirectUri() {
        return tppNokRedirectUri;
    }

    public void setTppNokRedirectUri(String tppNokRedirectUri) {
        this.tppNokRedirectUri = tppNokRedirectUri;
    }
}
