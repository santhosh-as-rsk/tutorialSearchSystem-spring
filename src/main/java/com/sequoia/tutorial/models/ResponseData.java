package com.sequoia.tutorial.models;

public class ResponseData {
    private int statusCode;
    private String message;
    private Object outputData;

    public ResponseData(int statusCode, String message, Object outputData) {
        this.statusCode = statusCode;
        this.message = message;
        this.outputData = outputData;
    }

    public ResponseData() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getOutputData() {
        return outputData;
    }

    public void setOutputData(Object outputData) {
        this.outputData = outputData;
    }
}
