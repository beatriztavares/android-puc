package com.list.students.studentslist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Beatriz Tavares on 11/07/2017.
 */

public class MainActivity extends AppCompatActivity {

    static final String TAG = "Retrofit";
    ListView listviewAlunos;
    APIService service;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //ProgressDialog para loading enquanto baixa os dados
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Buscando dados...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        listviewAlunos = (ListView) findViewById(R.id.lista);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(APIService.class);

        callGetAlunos();
    }

    private void callGetAlunos() {
        pDialog.show();
        Call<RetornoAlunos> call = service.listAlunos();
        call.enqueue(new Callback<RetornoAlunos>() {
            @Override
            public void onResponse(Call<RetornoAlunos> call, Response<RetornoAlunos> response) {
                if (response.isSuccessful()) {
                    RetornoAlunos retorno = response.body();
                    makeAdapterListViewCustomizado(retorno.getAlunos());
                } else {
                    Log.e(TAG, response.message());
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RetornoAlunos> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                pDialog.dismiss();
            }
        });

    }

    private void makeAdapterListViewCustomizado(List<Aluno> alunos) {
        final AdapterListAlunos adapterCustomizado = new AdapterListAlunos(this, alunos);
        listviewAlunos.setAdapter(adapterCustomizado);
        listviewAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Aluno aluno = (Aluno) adapterCustomizado.getItem(position);
                Toast.makeText(getApplicationContext(), "Mostrar o Maps", Toast.LENGTH_LONG).show();
            }
        });
    }


}
