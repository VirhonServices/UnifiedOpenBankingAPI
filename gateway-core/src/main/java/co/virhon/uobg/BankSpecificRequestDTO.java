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
import java.util.Map;

public class BankSpecificRequestDTO {
    private String url;
    private String method; // GET, POST, DELETE и т.д.
    private Map<String, String> headers;
    private Map<String, String> pathParameters; // Для подстановки {consentId} в URL
    private String payloadJson; // Готовый сериализованный JSON для тела запроса

    // Конструкторы, геттеры и сеттеры
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    public Map<String, String> getPathParameters() { return pathParameters; }
    public void setPathParameters(Map<String, String> pathParameters) { this.pathParameters = pathParameters; }

    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
}