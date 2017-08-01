package com.list.students.studentslist;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    String BASE_URL  = "https://parseapi.back4app.com/classes/";

    @Headers({
            "X-Parse-Application-Id: FWmmldOSRF8GE7jR8424Ex9Tu2ZHLTrggQHLJvjY",
            "X-Parse-REST-API-Key: RegHHKDEd3qf260q0mGUM7Z7GMsWry79eKsv3Jic"
    })
    @GET("Aluno")
    Call<RetornoAlunos> listAlunos();

    @Headers({
            "X-Parse-Application-Id: FWmmldOSRF8GE7jR8424Ex9Tu2ZHLTrggQHLJvjY",
            "X-Parse-REST-API-Key: RegHHKDEd3qf260q0mGUM7Z7GMsWry79eKsv3Jic",
            "Content-type: application/json"
    })
    @POST("Aluno")
    Call<RetornoAlunos> addAluno(@Body Aluno aluno);

    @Headers({
            "X-Parse-Application-Id: FWmmldOSRF8GE7jR8424Ex9Tu2ZHLTrggQHLJvjY",
            "X-Parse-REST-API-Key: RegHHKDEd3qf260q0mGUM7Z7GMsWry79eKsv3Jic"
    })
    @DELETE("Aluno/{objectId}")
    Call<RetornoAlunos> deleteAluno(@Path("objectId") String alunoId);
}
