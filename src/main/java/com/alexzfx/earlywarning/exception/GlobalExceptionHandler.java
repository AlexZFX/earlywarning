package com.alexzfx.earlywarning.exception;

import com.alexzfx.earlywarning.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public BaseResponse allException(BaseException e) {
        return new BaseResponse(e.getRet(), e.getMsg());
    }

    @ExceptionHandler(value = {Exception.class})
    public BaseResponse unknownException(Exception e) {
        log.error("unknown error", e);
        return new BaseResponse();
    }


}
