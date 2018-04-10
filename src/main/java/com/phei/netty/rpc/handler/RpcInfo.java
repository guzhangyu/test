package com.phei.netty.rpc.handler;

import java.util.Arrays;

/**
 * Created by guzy on 2018-04-10.
 */
public class RpcInfo {

    private String id;

    private String service;

    private String method;

    private Object[] args;

    private Boolean success;

    private Object result;

    private String exception;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    //    private StackTraceElement[] stackTrace;
//
//    public StackTraceElement[] getStackTrace() {
//        return stackTrace;
//    }
//
//    public void setStackTrace(StackTraceElement[] stackTrace) {
//        this.stackTrace = stackTrace;
//    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "RpcInfo{" +
                "id='" + id + '\'' +
                ", service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", args=" + Arrays.toString(args) +
                ", result=" + result +
                '}';
    }
}
