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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RulesLoader {

    public static ApiRulesConfig loadRules(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Чтение из файла по пути
            return objectMapper.readValue(new File(filePath), ApiRulesConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse rules JSON", e);
        }
    }

    public static ApiRulesConfig loadRulesFromClasspath(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        // Чтение из ресурсов (src/main/resources)
        try (InputStream inputStream = RulesLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + fileName);
            }
            return objectMapper.readValue(inputStream, ApiRulesConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse rules JSON from classpath", e);
        }
    }
}