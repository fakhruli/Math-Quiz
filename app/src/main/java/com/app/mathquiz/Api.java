package com.app.mathquiz;

import com.app.mathquiz.model.RegisterDevice;
import com.app.mathquiz.model.FetchUser;
import com.app.mathquiz.model.Score;
import com.app.mathquiz.model.Siswa;
import com.app.mathquiz.model.SoalDiscover;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by islam on 10/08/17.
 */

public interface Api {
    @POST("reg/device")
    @FormUrlEncoded
    Call<RegisterDevice> registerDevice(@Field("email") String email, @Field("token") String token);

    // Register + login guru
    @POST("reg/guru/")
    @FormUrlEncoded
    Call<FetchUser> registerGuru(@Field("nama") String nama, @Field("token") String token);

    // Register + login siswa
    @POST("reg/siswa/")
    @FormUrlEncoded
    Call<FetchUser> registerSiswa(@Field("nama") String nama, @Field("token") String token);

    @GET("guru/{nama}/{token}")
    Call<FetchUser> fetchUser(@Path("nama") String namaUser, @Path("token") String tokenUser);

    // ambil data semua siswa
    @GET("siswa/all")
    Call<Siswa> getAllSiswa();

    // ambil soal
    @GET("quiz/{no}")
    Call<SoalDiscover> getSoal(@Path("no") int no);

    // cek jawaban benar atau salah
    @GET("jawab/{jawab}/{noSoal}/{noPlayer}/{score}")
    Call<FetchUser> cekJawaban(@Path("jawab") String jawab,
                               @Path("noSoal") String noSoal,
                               @Path("noPlayer") String noPlayer,
                               @Path("score") String score);

    // ambil jawaban benar
    @GET("jawaban/benar/{noSoal}")
    Call<String> getJawaban(@Path("noSoal")String noSoal);

    // ambil total player
    @GET("user/total")
    Call<String> getTotalUser();

    // game control for guru
    @GET("game/{param}")
    Call<FetchUser> gameControl(@Path("param") String param);

    // get player score
    @GET("score/{no}")
    Call<Score> getScorePlayer(@Path("no") String no);
}
