package com.dfirago.jarvissmarthome.web.model;

/**
 * Created by dmfi on 05/01/2017.
 */
public class NetworkSelectResponse {

    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
