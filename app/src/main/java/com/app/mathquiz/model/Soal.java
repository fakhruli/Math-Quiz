package com.app.mathquiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by islam on 16/08/17.
 */

public class Soal {
    @SerializedName("id_soal")
    @Expose
    private String idSoal;
    @SerializedName("soal")
    @Expose
    private String soal;
    @SerializedName("jawab1")
    @Expose
    private String jawab1;
    @SerializedName("jawab2")
    @Expose
    private String jawab2;
    @SerializedName("jawab3")
    @Expose
    private String jawab3;
    @SerializedName("jawab4")
    @Expose
    private String jawab4;
    @SerializedName("jawab5")
    @Expose
    private String jawab5;
    @SerializedName("benar")
    @Expose
    private String benar;

    public String getIdSoal() {
        return idSoal;
    }

    public void setIdSoal(String idSoal) {
        this.idSoal = idSoal;
    }

    public String getSoal() {
        return soal;
    }

    public void setSoal(String soal) {
        this.soal = soal;
    }

    public String getJawab1() {
        return jawab1;
    }

    public void setJawab1(String jawab1) {
        this.jawab1 = jawab1;
    }

    public String getJawab2() {
        return jawab2;
    }

    public void setJawab2(String jawab2) {
        this.jawab2 = jawab2;
    }

    public String getJawab3() {
        return jawab3;
    }

    public void setJawab3(String jawab3) {
        this.jawab3 = jawab3;
    }

    public String getJawab4() {
        return jawab4;
    }

    public void setJawab4(String jawab4) {
        this.jawab4 = jawab4;
    }

    public String getJawab5() {
        return jawab5;
    }

    public void setJawab5(String jawab5) {
        this.jawab5 = jawab5;
    }

    public String getBenar() {
        return benar;
    }

    public void setBenar(String benar) {
        this.benar = benar;
    }
}
