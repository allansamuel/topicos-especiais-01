package com.example.exercicio1topicos;

import android.content.Intent;
import android.os.Bundle;

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

        customerService.getAll().enqueue(new Callback<ArrayList<Customer>>() {
            @Override
            public void onResponse(Call<ArrayList<Customer>> call, final Response<ArrayList<Customer>> response) {
                if(response.isSuccessful()){
                    customerArrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, response.body());
                    lvCustomerList.setAdapter(customerArrayAdapter);

                    lvCustomerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("USER_OBJECT", response.body().get(position));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }else{
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "response not successful",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Customer>> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "onfailure",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }
}