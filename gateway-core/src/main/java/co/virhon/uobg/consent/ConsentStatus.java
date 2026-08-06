package co.virhon.uobg.consent;

public enum ConsentStatus {
    received, rejected, valid, partiallyAuthorised, revokedByPsu,
    terminatedByTpp, replacedByTpp, expired
}