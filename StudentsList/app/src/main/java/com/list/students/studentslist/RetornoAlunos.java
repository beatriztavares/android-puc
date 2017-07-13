package com.list.students.studentslist;

import java.util.List;


public class RetornoAlunos {
    private List<Aluno> results = null;

    public List<Aluno> getAlunos() {
        return results;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.results = alunos;
    }
}
