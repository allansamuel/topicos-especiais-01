package com.example.exercicio1topicos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UserListActivity extends ListActivity  {

    private Bundle bundle;
    private ArrayList<User> userList;
    private ListView lvUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        initializeComponents();
        ArrayAdapter<User> adapterUserList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        super.setListAdapter(adapterUserList);

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
        this.userList = (ArrayList<User>) bundle.getSerializable("USER_LIST");
        this.lvUserList = findViewById(R.id.lv_user_list);
    }
}
