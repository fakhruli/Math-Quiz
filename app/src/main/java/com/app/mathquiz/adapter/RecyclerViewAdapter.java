package com.app.mathquiz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mathquiz.Api;
import com.app.mathquiz.AppConfig;
import com.app.mathquiz.R;
import com.app.mathquiz.RetrofitClient;
import com.app.mathquiz.model.Siswa;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by islam on 12/08/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    Context context;
    List<List<String>> data;

    public RecyclerViewAdapter(Context context, List<List<String>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rcv_layout,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.no.setText(AppConfig.no_siswa.get(position).toString());
        holder.nama.setText(data.get(position).get(1).toString());
        if (data.get(position).get(2) != null) {
            holder.score.setText(data.get(position).get(2).toString());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama,no,score;

        public ViewHolder(View itemView) {
            super(itemView);
            nama = (TextView)itemView.findViewById(R.id.rcv_namasiswa);
            no = (TextView)itemView.findViewById(R.id.rcv_nosiswa);
            score = (TextView)itemView.findViewById(R.id.rcv_score);
        }
    }
}
