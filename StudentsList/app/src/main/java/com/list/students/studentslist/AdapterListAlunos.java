package com.list.students.studentslist;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class AdapterListAlunos extends BaseAdapter {

    private final Context context;
    private final java.util.List<Aluno> alunos;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        // Infla a view da linha/item
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_aluno, parent, false);

        // findViewById das views que precisa atualizar
        ImageView foto = (ImageView) view.findViewById(R.id.imgAlunoUrl);
        TextView nome = (TextView) view.findViewById(R.id.txtNomeAluno);
        TextView idade = (TextView) view.findViewById(R.id.txtIdadeAluno);
        TextView endereco = (TextView) view.findViewById(R.id.txtEnderecoAluno);

        // Atualiza os valores das views
        Aluno aluno = alunos.get(position);
        Picasso.with(context).load(aluno.getFotoUrl()).into(foto);
        nome.setText(aluno.getNome());
        idade.setText(String.valueOf(String.valueOf(aluno.getIdade())));
        endereco.setText(aluno.getEndereco());

        // Retorna a view deste planeta
        return view;
    }
}
