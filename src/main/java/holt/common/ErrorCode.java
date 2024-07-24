package holt.common;

import lombok.Getter;

/**
 * @author Weiyang Wu
 * @date 2024/7/24 13:10
 */
@Getter
public enum ErrorCode {

    SUCCESS(0, "Ok",""),
    PARAMS_ERROR(40000, "Request parameter error", ""),
    NULL_ERROR(40001, "Parameter is null", ""),
    NOT_FOUND_ERROR(40002, "Not found", ""),
    NOT_LOGIN(40100, "User is not logged in", ""),
    NO_AUTH(40101, "Not authorised access", ""),
    SYSTEM_ERROR(50000, "System error", ""),
    NOT_ALLOWED(40500, "User is not allowed to call this method", "");
    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

}
