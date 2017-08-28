package com.app.mathquiz;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mathquiz.model.FetchUser;
import com.app.mathquiz.model.SoalDiscover;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by islam on 12/08/17.
 */

public class MainSiswa extends AppCompatActivity {
    ProgressDialog progressDialog;

    private final String TAG = this.getClass().getSimpleName();
    private static String no = null;
    private static String nama_player = null;
    private static int noSoal = 0;
    private static int maxSoal = 0;
    private static int jwb = 0;
    private static int cek = 1;

    private static int pangkat = 1;
    private static int kurang = 0;

    private static int pangkat1 = 1;
    private static int kurang1 = 0;

    private static int pangkat2 = 1;
    private static int kurang2 = 0;

    private static int score = 0;
    private static int totalUser = 0;

    SoalDiscover soalDiscover = null;

    Context context = this;
    Dialog dialog, msgDialog;

    @BindView(R.id.txt_nama)
    TextView txtNama;
    @BindView(R.id.txt_nourut)
    TextView txtNourut;
    @BindView(R.id.txt_score)
    TextView txtScore;
    @BindView(R.id.img_soal)
    ImageView imgSoal;
    @BindView(R.id.jwb_1)
    ImageView jwb1;
    @BindView(R.id.jwb_2)
    ImageView jwb2;
    @BindView(R.id.jwb_3)
    ImageView jwb3;
    @BindView(R.id.jwb_4)
    ImageView jwb4;

    TextView txtKonfirm, txt_msg;
    ImageView imgBenar;
    Button btn_lanjut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        no = this.getIntent().getStringExtra("no_player");
        nama_player = this.getIntent().getStringExtra("nama_player");
        setContentView(R.layout.not_yet);

        score = 0;
        cek = 1;

        noSoal = 0;
        soalDiscover = null;

        pangkat = 1;
        kurang = 0;

        pangkat1 = 1;
        kurang1 = 0;

        pangkat2 = 1;
        kurang2 = 0;

        getSoal();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_jwb);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtKonfirm = (TextView) dialog.findViewById(R.id.txt_benar);
        imgBenar = (ImageView) dialog.findViewById(R.id.jwb_benar);

        msgDialog = new Dialog(this);
        msgDialog.setContentView(R.layout.dialog_msg);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txt_msg = (TextView) msgDialog.findViewById(R.id.txt_msg);
        btn_lanjut = (Button) msgDialog.findViewById(R.id.btn_lanjut);
        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgDialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter("MyData"));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter("MyData"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver((mMessageReceiver));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver((mMessageReceiver));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getExtras().getString("message");
            if (intent.getExtras().getString("title").equals("siswa")) {
                if (message.equals("mulai")) {
                    setContentView(R.layout.siswa_main);
                    ButterKnife.bind(MainSiswa.this);

                    txtNourut.setText("No.Urut: " + no);
                    txtNama.setText(nama_player);

                    Log.d(TAG, "onReceive: " + getTotalUser());
                    if (getTotalUser()) playing();

                } else if (message.equals("next")) {
                    dialog.dismiss();
                    playing();
                } else if (message.equals("stop")) {
                    dialog.dismiss();
                    stopGame();
                } else if (message.equals("benar")) {
                    txtKonfirm.setText("Jawaban Yang Dipilih Benar");
                    imgBenar.setVisibility(View.VISIBLE);
                    getJawab(String.valueOf(noSoal));
                } else if (message.equals("salah")) {
                    txtKonfirm.setText("Jawaban Yang Dipilih Salah");
                    imgBenar.setVisibility(View.INVISIBLE);
                    dialog.show();
                }
            }
        }
    };

    @OnClick({R.id.jwb_1, R.id.jwb_2, R.id.jwb_3, R.id.jwb_4})
    public void submit(ImageView img) {
        switch (img.getId()) {
            case R.id.jwb_1:
                jwb = 1;
                break;
            case R.id.jwb_2:
                jwb = 2;
                break;
            case R.id.jwb_3:
                jwb = 3;
                break;
            case R.id.jwb_4:
                jwb = 4;
                break;
        }
        if (Integer.parseInt(no) != noSoal) jwb = 0;
        Log.d(TAG, "submit: " + jwb);
        cekJawab(jwb);
    }

    private void playing() {
        cek++;
        if (cek == ((3 * pangkat) - kurang)) {
            setSoal();
            cek = 1;
            pangkat++;
            kurang++;
        }
    }

    private void getSoal() {
        Retrofit retrofit = new RetrofitClient().getClient();
        Api api = retrofit.create(Api.class);
        Call<SoalDiscover> getSoal = api.getSoal(noSoal);
        getSoal.enqueue(new Callback<SoalDiscover>() {
            @Override
            public void onResponse(Call<SoalDiscover> call, Response<SoalDiscover> response) {

                if (soalDiscover == null) {
                    soalDiscover = response.body();
                    maxSoal = soalDiscover.getSoal().size();
                }
            }

            @Override
            public void onFailure(Call<SoalDiscover> call, Throwable throwable) {

            }
        });
    }

    private void setSoal() {
        Log.d(TAG, "setSoal: " + noSoal);
        if ((noSoal + 1) == Integer.parseInt(no)) {
            msgDialog.show();
        }

        if (noSoal < maxSoal) {
            Glide.with(context)
                    .load(AppConfig.BASE_URL + soalDiscover.getSoal().get(noSoal).getSoal())
                    .into(imgSoal);
            Glide.with(context)
                    .load(AppConfig.BASE_URL + soalDiscover.getSoal().get(noSoal).getJawab1())
                    .into(jwb1);
            Glide.with(context)
                    .load(AppConfig.BASE_URL + soalDiscover.getSoal().get(noSoal).getJawab2())
                    .into(jwb2);
            Glide.with(context)
                    .load(AppConfig.BASE_URL + soalDiscover.getSoal().get(noSoal).getJawab3())
                    .into(jwb3);
            Glide.with(context)
                    .load(AppConfig.BASE_URL + soalDiscover.getSoal().get(noSoal).getJawab4())
                    .into(jwb4);

            txtScore.setText("No.Soal: " + String.valueOf(noSoal + 1));
        } else {
            noSoal = 0;
            Log.d(TAG, "setSoal: hancuritt");
        }
        noSoal++;
    }

    // Send Jawaban To Server
    private void cekJawab(int jawab) {
        if (jawab != 0) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Mengupload Jawaban...");
            progressDialog.show();
            Retrofit retrofit = new RetrofitClient().getClient();
            Api api = retrofit.create(Api.class);
            Call<FetchUser> getTotalUser = api.cekJawaban(
                    String.valueOf(jawab),
                    String.valueOf(noSoal),
                    no,
                    String.valueOf(score));
            getTotalUser.enqueue(new Callback<FetchUser>() {
                @Override
                public void onResponse(Call<FetchUser> call, Response<FetchUser> response) {
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<FetchUser> call, Throwable throwable) {

                }
            });
        } else {
            Toast.makeText(this, "Bukan giliran kamu...", Toast.LENGTH_SHORT).show();
        }
    }

    private void getJawab(String nosoal) {
        cek++;
        if (cek == ((3 * pangkat1) - kurang1)) {
            if (no.equals(nosoal)) score += 10;
            Retrofit retrofit = new RetrofitClient().getClient();
            Api api = retrofit.create(Api.class);
            Call<String> getJawaban = api.getJawaban(String.valueOf(nosoal));
            getJawaban.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Glide.with(context).load(AppConfig.BASE_URL + response.body()).into(imgBenar);
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {

                }
            });
            dialog.show();
            cek = 1;
        }
    }

    private Boolean getTotalUser() {
        Retrofit retrofit = new RetrofitClient().getClient();
        Api api = retrofit.create(Api.class);
        Call<String> getTotalUser = api.getTotalUser();
        getTotalUser.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                totalUser = Integer.parseInt(response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {

            }
        });
        return true;
    }

    private void stopGame() {
        cek++;
        Log.d(TAG, "playing: " + cek + " " + pangkat2 + " " + kurang2);
        if (cek == ((3 * pangkat2) - kurang2)) {
            cek = 1;
            Toast.makeText(this, "Game telah berakhir", Toast.LENGTH_SHORT).show();
            showScore();
        }
    }

    private void showScore() {
        txt_msg.setText("Kamu Mendapatkan Score: " + score);
        btn_lanjut.setText("Logout");
        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgDialog.dismiss();
                finish();
            }
        });

        msgDialog.show();
    }

}