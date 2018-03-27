package com.alexzfx.earlywarning.util;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class BaseResponse<T> {

    private static final int SUCCESS_CODE = 200;
    private static final String SUCCESS_INFO = "success";
    private static final int ERROR_CODE = 500;
    private static final String SERVER_ERROR_INFO = "unknown error";

    public static BaseResponse EMPTY_SUCCESS_RESPONSE;

    static {
        EMPTY_SUCCESS_RESPONSE = new BaseResponse();
        EMPTY_SUCCESS_RESPONSE.setRet(SUCCESS_CODE);
        EMPTY_SUCCESS_RESPONSE.setMsg(SUCCESS_INFO);
    }

    @Expose
    private Integer ret;
    @Expose
    private T data;
    @Expose
    private String msg;


    public BaseResponse() {
        this.ret = ERROR_CODE;
        this.msg = SERVER_ERROR_INFO;
    }

    public BaseResponse(T data) {
        this.ret = SUCCESS_CODE;
        this.data = data;
        this.msg = SUCCESS_INFO;
    }

    public BaseResponse(Integer ret,String msg){
        this.ret = ret;
        this.msg = msg;
    }


}
