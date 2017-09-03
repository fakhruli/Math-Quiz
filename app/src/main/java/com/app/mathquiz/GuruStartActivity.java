package com.app.mathquiz;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
 * Created by islam on 19/08/17.
 */

public class GuruStartActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private static int noSoal = 0;
    private static int maxSoal = 0;
    private static int jwb = 0;
    private static int cek = 1;
    private static int pangkat = 1;
    private static int kurang = 0;
    private final int max_player = AppConfig.nama_siswa.size();

    SoalDiscover soalDiscover = null;
    Context context = this;
    Dialog dialog, dialogjwb;

    @BindView(R.id.txt_nama)
    TextView txtNama;
    @BindView(R.id.txt_nourut)
    TextView txtNourut;
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
    @BindView(R.id.jwb_5)
    ImageView jwb5;

    TextView txtKonfirm, txtbenar;
    ImageView imgBenar, jwbbenar;
    Button btn_next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guru_start);
        ButterKnife.bind(this);

        pangkat = 1;
        kurang = 0;

        soalDiscover = null;

        getSoal();
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setSoal();
            }
        },1000);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_next);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtKonfirm = (TextView) dialog.findViewById(R.id.txt_benar);
        imgBenar = (ImageView) dialog.findViewById(R.id.jwb_benar);
        btn_next = (Button) dialog.findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_next.getText().toString().equals("Akhiri Permainan")) {
                    // akhiri permainan
                    stopGame();
                } else {
                    sendToPlayer();
                    setSoal();
                    dialog.dismiss();
                }
            }
        });

        dialogjwb = new Dialog(this);
        dialogjwb.setContentView(R.layout.dialog_jwb);
        dialogjwb.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtbenar = (TextView) dialogjwb.findViewById(R.id.txt_benar);
        jwbbenar = (ImageView) dialogjwb.findViewById(R.id.jwb_benar);
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
            Log.d("bla", "onReceive: " + intent.getExtras().getString("message"));
            String title = intent.getExtras().getString("title");
            if (title.equals("guru")) {
                if (intent.getExtras().getString("message").equals("benar")) {
                    Log.d(TAG, "onReceive: Benar");
                    getJawab(String.valueOf(noSoal));
                    txtKonfirm.setText("Jawaban Yang Dipilih Benar");
                    if (noSoal == max_player) btn_next.setText("Akhiri Permainan");
                    dialog.show();
                } else if (intent.getExtras().getString("message").equals("salah")) {
                    getJawab(String.valueOf(noSoal));
                    txtKonfirm.setText("Jawaban Yang Dipilih Salah");
                    if (noSoal == max_player) btn_next.setText("Akhiri Permainan");
                    dialog.show();
                }
            }
        }
    };

    @OnClick({R.id.jwb_1, R.id.jwb_2, R.id.jwb_3, R.id.jwb_4})
    public void submit(ImageView img) {
        showJawab();
    }

    private void showJawab() {
        txtbenar.setText("Jawabannya Adalah...");
        Retrofit retrofit = new RetrofitClient().getClient();
        Api api = retrofit.create(Api.class);
        Call<String> getJawaban = api.getJawaban(String.valueOf(noSoal));
        getJawaban.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: " + response.body());
                Glide.with(context).load(AppConfig.BASE_URL + response.body()).into(jwbbenar);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.d(TAG, "onFailure: " + throwable.getMessage());
            }
        });
        dialogjwb.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },3000);
    }

    private void setSoal() {
        Log.d(TAG, "setSoal: " + soalDiscover);
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
            Glide.with(context)
                    .load(AppConfig.BASE_URL + soalDiscover.getSoal().get(noSoal).getJawab5())
                    .into(jwb5);
            txtNourut.setText(String.valueOf(noSoal + 1));
            txtNama.setText(AppConfig.nama_siswa.get(noSoal));
        } else {
            noSoal = 0;
        }
        noSoal++;
    }

    private boolean getSoal() {
        Retrofit retrofit = new RetrofitClient().getClient();
        Api api = retrofit.create(Api.class);
        Call<SoalDiscover> getSoal = api.getSoal(noSoal);
        getSoal.enqueue(new Callback<SoalDiscover>() {
            @Override
            public void onResponse(Call<SoalDiscover> call, Response<SoalDiscover> response) {

                if (soalDiscover == null) {
                    soalDiscover = response.body();
                    Log.d(TAG, "onResponse: " + response.body().getSoal().get(0).getSoal());
                    maxSoal = soalDiscover.getSoal().size();
                }
            }

            @Override
            public void onFailure(Call<SoalDiscover> call, Throwable throwable) {

            }
        });
        return true;
    }

    private void getJawab(String nosoal) {
        cek++;
        Log.d(TAG, "playing: " + cek);
        if (cek >= ((3*pangkat) - kurang)) {
            Retrofit retrofit = new RetrofitClient().getClient();
            Api api = retrofit.create(Api.class);
            Call<String> getJawaban = api.getJawaban(String.valueOf(nosoal));
            getJawaban.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                    Glide.with(context).load(AppConfig.BASE_URL + response.body()).into(imgBenar);
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    Log.d(TAG, "onFailure: " + throwable.getMessage());
                }
            });
            cek = 1;
            pangkat++;
            kurang++;
        }
    }

    private void sendToPlayer() {
        Retrofit retrofit = new RetrofitClient().getClient();
        Api api = retrofit.create(Api.class);
        Call<FetchUser> nextGame = api.gameControl("next");
        nextGame.enqueue(new Callback<FetchUser>() {
            @Override
            public void onResponse(Call<FetchUser> call, Response<FetchUser> response) {

            }

            @Override
            public void onFailure(Call<FetchUser> call, Throwable throwable) {

            }
        });
    }

    private void stopGame() {
        dialog.dismiss();
        noSoal = 0;
        Retrofit retrofit = new RetrofitClient().getClient();
        Api api = retrofit.create(Api.class);
        Call<FetchUser> stopGame = api.gameControl("stop");
        stopGame.enqueue(new Callback<FetchUser>() {
            @Override
            public void onResponse(Call<FetchUser> call, Response<FetchUser> response) {

            }

            @Override
            public void onFailure(Call<FetchUser> call, Throwable throwable) {

            }
        });
        Toast.makeText(this, "Game telah berakhir", Toast.LENGTH_SHORT).show();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        startActivity(new Intent(GuruStartActivity.this,ScoringActivity.class));
        finish();
    }
}
