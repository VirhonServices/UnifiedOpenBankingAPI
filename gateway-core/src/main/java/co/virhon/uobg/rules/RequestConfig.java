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

import java.util.List;

class RequestConfig {
    private List<ParameterRule> path;
    private List<ParameterRule> headers;
    private Payload payload;

    // вложенный объект в getConsentStatus (request.request)
    private RequestConfig request;

    // геттеры и сеттеры
    public List<ParameterRule> getPath() { return path; }
    public void setPath(List<ParameterRule> path) { this.path = path; }
    public List<ParameterRule> getHeaders() { return headers; }
    public void setHeaders(List<ParameterRule> headers) { this.headers = headers; }
    public Payload getPayload() { return payload; }
    public void setPayload(Payload payload) { this.payload = payload; }
    public RequestConfig getRequest() { return request; }
    public void setRequest(RequestConfig request) { this.request = request; }
}