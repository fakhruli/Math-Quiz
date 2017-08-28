package com.app.mathquiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by islam on 26/08/17.
 */

public class Score {
    @SerializedName("score")
    @Expose
    private List<String> score = null;

    public List<String> getScore() {
        return score;
    }

    public void setScore(List<String> score) {
        this.score = score;
    }
}
