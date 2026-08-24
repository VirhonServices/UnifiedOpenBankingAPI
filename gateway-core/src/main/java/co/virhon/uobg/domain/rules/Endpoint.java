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

package co.virhon.uobg.domain.rules;

public class Endpoint {
    private String url;
    private String method;
    private RequestConfig request;
    private ResponseConfig response;

    // геттеры и сеттеры
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public RequestConfig getRequest() { return request; }
    public void setRequest(RequestConfig request) { this.request = request; }
    public ResponseConfig getResponse() { return response; }
    public void setResponse(ResponseConfig response) { this.response = response; }
}
