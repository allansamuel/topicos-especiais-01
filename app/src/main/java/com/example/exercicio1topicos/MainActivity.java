package com.example.exercicio1topicos;

import android.content.Intent;
import android.os.Bundle;

import com.example.exercicio1topicos.model.Customer;
import com.example.exercicio1topicos.service.CustomerService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private CustomerService customerService;
    private ListView lvCustomerList;
    private FloatingActionButton fabAdd;
    private ArrayAdapter<Customer> customerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();

        getAllCustomers();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerFormActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeComponents() {
        this.lvCustomerList = findViewById(android.R.id.list);
        this.fabAdd = findViewById(R.id.fab);
        this.retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.customerService = retrofit.create(CustomerService.class);
    }

    private void getAllCustomers() {
        this.customerService.getAll().enqueue(new Callback<ArrayList<Customer>>() {
            @Override
            public void onResponse(Call<ArrayList<Customer>> call, final Response<ArrayList<Customer>> response) {
                if(response.isSuccessful()){
                    customerArrayAdapter = new ArrayAdapter<>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            response.body());
                    lvCustomerList.setAdapter(customerArrayAdapter);

                    lvCustomerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), CustomerFormActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(getString(R.string.customer_bundle), response.body().get(position));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }else{
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            getString(R.string.error_response),
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Customer>> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        getString(R.string.error_request),
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }
}