package com.alexzfx.earlywarninguser.exception;

public class BaseException extends RuntimeException {

    private int ret;
    private String msg;

    @Override
    public String toString() {
        return "BaseException{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                '}';
    }

    public BaseException(int ret, String msg){
        super();
        this.ret = ret;
        this.msg = msg;
    }

    public BaseException() {
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
