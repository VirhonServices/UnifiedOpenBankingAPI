package co.virhon.uobg;

import java.util.Map;

public class BankSpecificResponseDTO {
    private int httpStatusCode;
    private Map<String, String> headers;
    private String rawBody; // Сырой ответ от банка (JSON-строка)

    // Конструкторы, геттеры и сеттеры
    public int getHttpStatusCode() { return httpStatusCode; }
    public void setHttpStatusCode(int httpStatusCode) { this.httpStatusCode = httpStatusCode; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    public String getRawBody() { return rawBody; }
    public void setRawBody(String rawBody) { this.rawBody = rawBody; }
}