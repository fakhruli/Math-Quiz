package com.app.mathquiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.app.mathquiz.adapter.RecyclerViewAdapter;
import com.app.mathquiz.model.Score;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScoringActivity extends AppCompatActivity {
    private static final String TAG = ScoringActivity.class.getSimpleName();
    @BindView(R.id.score_rcv)
    RecyclerView rcv_score;
    @BindView(R.id.btnMainMenu)
    Button btnMainMenu;
    RecyclerViewAdapter adapter;

    List<String> score = new ArrayList<>();
    List<List<String>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);
        ButterKnife.bind(this);

        getScore();

        LinearLayoutManager lm = new LinearLayoutManager(this);
        rcv_score.setLayoutManager(lm);
        adapter = new RecyclerViewAdapter(this, data);
        rcv_score.setAdapter(adapter);

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(new Intent(ScoringActivity.this,MainGuru.class));
                finish();
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
            if (intent.getExtras().getString("title").equals("guru")) {
                if (intent.getExtras().getString("message").equals("score baru")) {
                    Log.d(TAG, "onReceive: wtf");
                    getScore();
                }
            }
        }
    };

    private void getScore() {
        Retrofit retrofit = new RetrofitClient().getClient();
        Api api = retrofit.create(Api.class);
        Call<Score> getScore = api.getScorePlayer("0");
        getScore.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(Call<Score> call, Response<Score> response) {
                score.clear();
                score.addAll(response.body().getScore());
                Log.d(TAG, "getScore: " + response.raw());
                addData();
            }

            @Override
            public void onFailure(Call<Score> call, Throwable throwable) {

            }
        });
    }

    private void addData() {
        data.clear();
        for (int i = 0; i < score.size(); i++) {
            List<String> masukan = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                masukan.add(AppConfig.no_siswa.get(i));
                masukan.add(AppConfig.nama_siswa.get(i));
                masukan.add(score.get(i));
            }
            data.add(masukan);
        }
        sortingData();
    }

    private void sortingData() {
        Collections.sort(data, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> strings, List<String> t1) {
                return t1.get(2).compareTo(strings.get(2));
            }
        });
        Log.d(TAG, "sortingData: " + data);
        adapter.notifyDataSetChanged();
    }
}
