package com.app.mathquiz;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.mathquiz.model.FetchUser;
import com.app.mathquiz.model.RegisterDevice;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.btnguru)
    Button btnGuru;
    @BindView(R.id.btnsiswa)
    Button btnSiswa;

    ProgressDialog progressDialog;
    Context mCtx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.d(TAG, "onCreate: " + SharedPrefManager.getmInstance(this).getDeviceToken());

        btnGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register("guru");
            }
        });
        btnSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register("siswa");
            }
        });
    }

    private void register(final String role) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_reg);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText nama = (EditText) dialog.findViewById(R.id.nama);
        Button save = (Button) dialog.findViewById(R.id.saveReg);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTokenToServer(String.valueOf(nama.getText()), role);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void sendTokenToServer(final String nama, String role) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering device...");
        progressDialog.show();

        final String token = SharedPrefManager.getmInstance(this).getDeviceToken();

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        Retrofit retrofit = new RetrofitClient().getClient();
        Api api = retrofit.create(Api.class);
        Call<FetchUser> fetchUserCall = null;

        if (role == "guru") {
            fetchUserCall = api.registerGuru(nama, token);
        } else {
            fetchUserCall = api.registerSiswa(nama, token);
        }

        fetchUserCall.enqueue(new Callback<FetchUser>() {
            @Override
            public void onResponse(Call<FetchUser> call, Response<FetchUser> response) {
                Log.d(TAG, "onResponse: " + response.body());
                Log.d(TAG, "Test : " + response.raw());
                Intent intent = null;
                if (!response.body().getFailure()) {
                    if (response.body().getRole().equals("guru")) {
                        intent = new Intent(mCtx, MainGuru.class);
                    } else {
                        intent = new Intent(mCtx, MainSiswa.class);
                        intent.putExtra("no_player",response.body().getNo());
                        intent.putExtra("nama_player",nama);
                    }

                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                } else {
                    Toast.makeText(MainActivity.this,"User tidak ditemukan",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<FetchUser> call, Throwable throwable) {

            }
        });
    }

}
