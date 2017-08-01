package com.list.students.studentslist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
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
    FloatingActionButton fab;
    ImageView foto;
    View view, viewList;
    public final static String EXTRA_ENDERECO = "ENDERECO";
    public final static String EXTRA_NOME = "NOME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        viewList = inflater.inflate(R.layout.activity_list, null);
        setContentView(viewList);
        view = inflater.inflate(R.layout.dialog_add, null);

        final EditText url = (EditText)view.findViewById(R.id.url);
        foto = (ImageView)view.findViewById(R.id.fotoAluno);

        url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                atualizaFoto(url.getText().toString());
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAluno();
            }
        });

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

    private void atualizaFoto(String url) {
        Picasso.with(this)
                .load(url)
                .into(foto);
    }

    private void addAluno() {

        final EditText name = (EditText)view.findViewById(R.id.name);
        final EditText idade = (EditText)view.findViewById(R.id.idade);
        final EditText telefone = (EditText)view.findViewById(R.id.tel);
        final EditText endereco = (EditText)view.findViewById(R.id.endereco);
        final EditText fotoUrl = (EditText)view.findViewById(R.id.url);
        pDialog.setMessage("Adicionando aluno(a)...");

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(view)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Aluno novo = new Aluno();
                novo.setNome(name.getText().toString());
                novo.setIdade(Integer.getInteger(idade.getText().toString()));
                novo.setTelefone(telefone.getText().toString());
                novo.setEndereco(endereco.getText().toString());
                novo.setFotoUrl(fotoUrl.getText().toString());
                callAddAluno(novo);
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "Cancelado!", Toast.LENGTH_LONG).show();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void callAddAluno(Aluno aluno) {
        pDialog.show();
        Call<RetornoAlunos> call = service.addAluno(aluno);
        call.enqueue(new Callback<RetornoAlunos>() {
            @Override
            public void onResponse(Call<RetornoAlunos> call, Response<RetornoAlunos> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Aluno(a) adicionado(a) com sucesso!", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                    pDialog.setMessage("Buscando dados...");
                    pDialog.setIndeterminate(true);
                    pDialog.setCancelable(false);
                    callGetAlunos();
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
                showMaps(aluno);
            }
        });
    }

    private void showMaps(Aluno aluno) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(EXTRA_ENDERECO, aluno.getEndereco());
        intent.putExtra(EXTRA_NOME, aluno.getNome());
        startActivity(intent);
    }


}
