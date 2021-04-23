package com.example.exercicio1topicos;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CustomerService {

    @GET("customer/")
    Call<ArrayList<Customer>> getAll();

    @POST("customer/")
    Call<Customer> create(@Body Customer customer);

    @PUT("customer/{id}")
    Call<Customer> update(@Path("id") long id, @Body Customer customer);

    @DELETE("customer/{id}")
    Call<Void> delete(@Path("id") long id);
}
