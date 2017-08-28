package com.app.mathquiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by islam on 12/08/17.
 */

public class FetchUser {
    @SerializedName("failure")
    @Expose
    private Boolean failure;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("no")
    @Expose
    private String no;

    public Boolean getFailure() {
        return failure;
    }

    public void setFailure(Boolean failure) {
        this.failure = failure;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
