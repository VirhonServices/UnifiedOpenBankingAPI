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

import co.virhon.uobg.dto.BankSpecificRequestDTO;
import co.virhon.uobg.dto.UnifiedCreateConsentRequestDTO;
import co.virhon.uobg.dto.UnifiedCreateConsentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {

    @Autowired
    private GatewayMapper<UnifiedCreateConsentRequestDTO, BankSpecificRequestDTO> mapper;

    public BankSpecificRequestDTO processConsentCreation(UnifiedCreateConsentRequestDTO requestDTO) {
        return mapper.map(requestDTO, requestDTO.getBankId());
    }
}
