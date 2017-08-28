package com.app.mathquiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.app.mathquiz.adapter.RecyclerViewAdapter;
import com.app.mathquiz.model.FetchUser;
import com.app.mathquiz.model.Siswa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class MainGuru extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.btnMulai)
    Button btnMulai;
    @BindView(R.id.btnReset)
    Button btnReset;
    @BindView(R.id.guru_rcv)
    RecyclerView rcv;

    RecyclerViewAdapter adapter;

    List<String> nama = new ArrayList<>();
    List<String> nosiswa = new ArrayList<>();
    List<String> score = new ArrayList<>();

    List<List<String>> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guru_main);
        ButterKnife.bind(MainGuru.this);

        getAllDataSiswa();

        adapter = new RecyclerViewAdapter(this,data);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(lm);
    }

    @OnClick({R.id.btnMulai,R.id.btnReset})
    public void submit(Button btn) {
        Intent intent = null;
        switch (btn.getId()) {
            case R.id.btnMulai :
                if (nama.size() == 0) {
                    Toast.makeText(MainGuru.this,"Belum ada pemain yang bergabung",Toast.LENGTH_SHORT).show();
                } else {
                    mulaiGame();
                    intent = new Intent(MainGuru.this,GuruStartActivity.class);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.btnReset :
                resetGame();
                break;
        }
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
            if (intent.getExtras().getString("title").equals("guru")) {
                if (intent.getExtras().getString("message").equals("player baru")) {
                    getAllDataSiswa();
                }
            }
        }
    };

    private void getAllDataSiswa() {
        Retrofit retrofit = new RetrofitClient().getClient();
        Api api = retrofit.create(Api.class);
        Call<Siswa> getAllSiswa = api.getAllSiswa();
        getAllSiswa.enqueue(new Callback<Siswa>() {
            @Override
            public void onResponse(Call<Siswa> call, Response<Siswa> response) {
                nama.clear();
                nosiswa.clear();
                AppConfig.nama_siswa.clear();
                AppConfig.no_siswa.clear();

                nama.addAll(response.body().getNama());
                AppConfig.nama_siswa.addAll(nama);
                nosiswa.addAll(response.body().getNoSiswa());
                AppConfig.no_siswa.addAll(nosiswa);
                addData();
                Log.d(TAG, "getAllDataSiswa: " + nama);
            }

            @Override
            public void onFailure(Call<Siswa> call, Throwable throwable) {

            }
        });
    }

    private void addData() {
        score.clear();
        data.clear();
        Log.d(TAG, "addData: " + AppConfig.nama_siswa);
        for (int i = 0; i < nama.size(); i++) {
            List<String> masukan = new ArrayList<>();
            score.add(null);
            for (int j = 0; j < 3; j++) {
                masukan.add(AppConfig.no_siswa.get(i));
                masukan.add(AppConfig.nama_siswa.get(i));
                masukan.add(score.get(i));
            }
            data.add(masukan);
        }
        adapter.notifyDataSetChanged();
    }

    private void resetGame() {
        if (nama.size() == 0) {
            Toast.makeText(this,"Tidak ada data",Toast.LENGTH_SHORT).show();
        } else {
            Retrofit retrofit = new RetrofitClient().getClient();
            Api api = retrofit.create(Api.class);
            Call<FetchUser> reset = api.gameControl("reset");
            reset.enqueue(new Callback<FetchUser>() {
                @Override
                public void onResponse(Call<FetchUser> call, Response<FetchUser> response) {

                }

                @Override
                public void onFailure(Call<FetchUser> call, Throwable throwable) {

                }
            });
            data.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(MainGuru.this,"Berhasil reset data",Toast.LENGTH_SHORT).show();
        }
    }

    private void mulaiGame() {
        Retrofit retrofit = new RetrofitClient().getClient();
        Api api = retrofit.create(Api.class);
        Call<FetchUser> nextGame = api.gameControl("mulai");
        nextGame.enqueue(new Callback<FetchUser>() {
            @Override
            public void onResponse(Call<FetchUser> call, Response<FetchUser> response) {

            }

            @Override
            public void onFailure(Call<FetchUser> call, Throwable throwable) {

            }
        });
    }
}
