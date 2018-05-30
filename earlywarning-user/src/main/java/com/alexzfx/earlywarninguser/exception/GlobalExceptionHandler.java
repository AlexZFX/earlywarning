package com.alexzfx.earlywarninguser.exception;

import com.alexzfx.earlywarninguser.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
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
        if (e instanceof UnauthorizedException) {
            return new BaseResponse(403, "权限不足");
        }
        log.error("unknown error", e);
        log.error(e.getLocalizedMessage());
        return new BaseResponse();
    }


}
