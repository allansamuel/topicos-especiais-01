package com.example.exercicio1topicos;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Bundle bundle;
    private ArrayList<User> userList;
    private ListView lvUserList;
    private FloatingActionButton fabAdd;
    private ArrayAdapter<User> adapterUserList;

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

        lvUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER_OBJECT", userList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initializeComponents() {
        this.bundle = getIntent().getExtras();
        if(bundle != null) {
            this.userList = (ArrayList<User>) bundle.getSerializable("USER_LIST");
            this.adapterUserList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
            this.lvUserList.setAdapter(adapterUserList);
        }
        this.lvUserList = findViewById(android.R.id.list);
        this.fabAdd = findViewById(R.id.fab);
    }
}