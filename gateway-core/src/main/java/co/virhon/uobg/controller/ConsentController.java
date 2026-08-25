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

package co.virhon.uobg.controller;

import co.virhon.uobg.GatewayService;
import co.virhon.uobg.dto.UnifiedCreateConsentRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/consents")
public class ConsentController {
    private final GatewayService gatewayService;

    public ConsentController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping("/account-access")
    public ResponseEntity<?> createConsent(
            // Тело запроса (Jackson автоматически смапит JSON на поля access, consentType и т.д.)
            @RequestBody UnifiedCreateConsentRequestDTO requestDto,

            // Заголовки (мапим вручную в поля DTO, чтобы собрать весь контекст в одном объекте)
            @RequestHeader("BankID") String bankId,
            @RequestHeader("X-Request-ID") String xRequestId,
            @RequestHeader(value = "ClientID", required = false) String clientId,
            @RequestHeader("PSU-ID") String psuId,
            @RequestHeader("PSU-ID-type") String psuIdType,
            @RequestHeader("PSU-IP-Address") String psuIpAddress,
            @RequestHeader(value = "TPP-Redirect-URI", required = false) String tppRedirectUri,
            @RequestHeader(value = "TPP-Nok-Redirect-URI", required = false) String tppNokRedirectUri
    ) {
        // Заполняем контекстные поля заголовков внутрь DTO
        requestDto.setBankId(bankId);
        requestDto.setXRequestId(xRequestId);
        if ("raiffeisen".equalsIgnoreCase(bankId)
                || "ua-raiffeisen".equalsIgnoreCase(bankId)) {
            requestDto.setClientId(clientId);
        }
        requestDto.setPsuId(psuId);
        requestDto.setPsuIdType(psuIdType);
        requestDto.setPsuIpAddress(psuIpAddress);
        requestDto.setTppRedirectUri(tppRedirectUri);
        requestDto.setTppNokRedirectUri(tppNokRedirectUri);

        // Передаем единый объект в сервис
        var response = gatewayService.processConsentCreation(requestDto);
        return ResponseEntity.status(201).body(response);
    }
}
