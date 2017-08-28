package com.app.mathquiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by islam on 16/08/17.
 */

public class SoalDiscover {
    @SerializedName("soal")
    @Expose
    private List<Soal> soal = null;

    public List<Soal> getSoal() {
        return soal;
    }

    public void setSoal(List<Soal> soal) {
        this.soal = soal;
    }
}
