package com.example.urpm.common.enums;

import lombok.Getter;

@Getter
public enum RestResultEnum {

    SUCCESS(200, "success"),
    FAIL(400, "Request Filed"),
    NOT_FOUND(404, "Not Found"),
    NOT_ALLOWED(405, "Method Not Allowed"),
    SERVER_ERROR(500, "Internal Server Error"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden");

    private int code;

    private String message;

    RestResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
