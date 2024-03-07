package org.moonshot.jwt;

public enum JwtValidationType {
    VALID_ACCESS,
    VALID_REFRESH,
    INVALID_JWT_SIGNATURE,
    INVALID_JWT_TOKEN,
    EXPIRED_JWT_TOKEN,
    UNSUPPORTED_JWT_TOKEN,
    EMPTY_JWT                 
}