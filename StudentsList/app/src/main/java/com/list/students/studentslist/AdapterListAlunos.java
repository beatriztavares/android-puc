package com.list.students.studentslist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.squareup.picasso.Picasso;



public class AdapterListAlunos extends BaseAdapter {

    private final Context context;
    private final java.util.List<Aluno> alunos;
    APIService service;
    static final String TAG = "Retrofit";


    public AdapterListAlunos(Context context, java.util.List<Aluno> alunos) {

        this.context = context;
        this.alunos = alunos;
    }


    @Override
    public int getCount() {

        return alunos != null ? alunos.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Infla a view da linha/item
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_aluno, parent, false);

        // findViewById das views que precisa atualizar
        ImageView foto = (ImageView) view.findViewById(R.id.imgAlunoUrl);
        TextView nome = (TextView) view.findViewById(R.id.txtNomeAluno);
        TextView idade = (TextView) view.findViewById(R.id.txtIdadeAluno);
        TextView endereco = (TextView) view.findViewById(R.id.txtEnderecoAluno);
        ImageView delete = (ImageView) view.findViewById(R.id.imgDeleteAluno);

        // Atualiza os valores das views
        final Aluno aluno = alunos.get(position);
        Picasso.with(context).load(aluno.getFotoUrl()).into(foto);
        nome.setText(aluno.getNome());
        idade.setText(String.valueOf(String.valueOf(aluno.getIdade())));
        endereco.setText(aluno.getEndereco());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletarAluno(aluno);
            }
        });

        // Retorna a view deste planeta
        return view;
    }


    private void deletarAluno(Aluno a) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(APIService.class);
        Call<RetornoAlunos> call = service.deleteAluno(a.getObjectId());
        call.enqueue(new Callback<RetornoAlunos>() {
            @Override
            public void onResponse(Call<RetornoAlunos> call, Response<RetornoAlunos> response) {
                if (response.isSuccessful()) {
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<RetornoAlunos> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
