package holt.common;

import lombok.Data;

import java.io.Serializable;

/**
 * A general type of response object
 * @author Weiyang Wu
 * @date 2024/7/24 11:39
 * @param <T> data with any type
 */
@Data
public class BaseResponse<T> implements Serializable {
    int code;

    T data;

    String message;

    String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
