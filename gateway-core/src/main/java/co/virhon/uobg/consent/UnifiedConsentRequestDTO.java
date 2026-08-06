package co.virhon.uobg.consent;

import java.time.LocalDate;

public class UnifiedConsentRequestDTO {
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
}