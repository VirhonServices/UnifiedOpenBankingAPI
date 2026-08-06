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