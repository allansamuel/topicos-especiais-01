package com.example.exercicio1topicos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class UserListActivity extends ListActivity {

    private Bundle bundle;
    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        initializeComponents();
        ArrayAdapter<User> adapterUserList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        super.setListAdapter(adapterUserList);
    }

    private void initializeComponents() {
        this.bundle = getIntent().getExtras();
        this.userList = (ArrayList<User>) bundle.getSerializable("USER_LIST");
    }
}
