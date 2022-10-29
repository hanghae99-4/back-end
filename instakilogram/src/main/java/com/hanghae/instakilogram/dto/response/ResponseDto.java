package com.hanghae.instakilogram.dto.response;

import com.hanghae.instakilogram.entity.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private Error error;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }
    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>(false, null, new Error(message));
    }
}
