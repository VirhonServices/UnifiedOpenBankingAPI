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

package co.virhon.uobg;

import co.virhon.uobg.domain.consent.AccountAccessRight;
import co.virhon.uobg.domain.consent.ConsentAccess;
import co.virhon.uobg.domain.consent.ConsentAccount;
import co.virhon.uobg.domain.consent.ConsentPaymentAccess;
import co.virhon.uobg.domain.consent.ConsentType;
import co.virhon.uobg.dto.BankSpecificRequestDTO;
import co.virhon.uobg.dto.UnifiedCreateConsentRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GatewayMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void mapsUnifiedConsentRequestUsingBankRules() throws Exception {
        GatewayMapper<UnifiedCreateConsentRequestDTO, BankSpecificRequestDTO> mapper =
                new GatewayMapper<>(
                        UnifiedCreateConsentRequestDTO.class,
                        BankSpecificRequestDTO.class,
                        "UA"
                );
        UnifiedCreateConsentRequestDTO source = createConsentRequest();

        BankSpecificRequestDTO result = mapper.map(source, "PrivatBank");
        JsonNode payload = objectMapper.readTree(result.getPayloadJson());
        JsonNode payment = payload.path("access").path("payments").get(0);

        assertAll(
                () -> assertEquals("/v2/consents/account-access", result.getUrl()),
                () -> assertEquals("post", result.getMethod()),
                () -> assertEquals("psu-42", result.getHeaders().get("PSU-ID")),
                () -> assertEquals("192.0.2.10", result.getHeaders().get("PSU-IP-Address")),
                () -> assertEquals("UA123456789", payment.path("account").path("iban").asText()),
                () -> assertEquals("UAH", payment.path("account").path("currency").asText()),
                () -> assertEquals("balances", payment.path("rights").get(0).asText()),
                () -> assertEquals("detailed", payload.path("consentType").asText()),
                () -> assertEquals(true, payload.path("reccurringIndicator").asBoolean()),
                () -> assertEquals("2026-12-31", payload.path("validTo").asText()),
                () -> assertEquals(4, payload.path("frequencyPerDay").asInt())
        );
    }

    private static UnifiedCreateConsentRequestDTO createConsentRequest() {
        ConsentAccount account = new ConsentAccount();
        account.setIban("UA123456789");
        account.setCcy("UAH");

        ConsentPaymentAccess payment = new ConsentPaymentAccess();
        payment.setAccount(account);
        payment.setRights(List.of(AccountAccessRight.balances));

        ConsentAccess access = new ConsentAccess();
        access.setPayments(List.of(payment));

        UnifiedCreateConsentRequestDTO request = new UnifiedCreateConsentRequestDTO();
        request.setAccess(access);
        request.setConsentType(ConsentType.detailed);
        request.setRecurringIndicator(true);
        request.setValidTo(LocalDate.of(2026, 12, 31));
        request.setFrequencyPerDay(4);
        request.setPsuId("psu-42");
        request.setPsuIpAddress("192.0.2.10");
        return request;
    }
}
