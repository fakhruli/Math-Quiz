package com.app.mathquiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by islam on 13/08/17.
 */

public class Siswa {
    @SerializedName("nama")
    @Expose
    private List<String> nama = null;
    @SerializedName("no_siswa")
    @Expose
    private List<String> noSiswa = null;
    @SerializedName("tokens")
    @Expose
    private List<String> tokens = null;

    public List<String> getNama() {
        return nama;
    }

    public void setNama(List<String> nama) {
        this.nama = nama;
    }

    public List<String> getNoSiswa() {
        return noSiswa;
    }

    public void setNoSiswa(List<String> noSiswa) {
        this.noSiswa = noSiswa;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
