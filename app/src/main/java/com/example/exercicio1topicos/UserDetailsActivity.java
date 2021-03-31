package com.example.exercicio1topicos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class UserDetailsActivity extends AppCompatActivity {

    private Bundle bundle;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initializeComponents();

        Toast.makeText(this, formatUserDetails(),Toast.LENGTH_LONG).show();
    }

    private void initializeComponents() {
        this.bundle = getIntent().getExtras();
        this.user = (User) bundle.getSerializable("USER_OBJECT");
    }

    private String formatUserDetails() {
        String hobbies = "";
        for(String item:user.getHobbies()){
           hobbies += item + ",\n";
        }
        return user.getName() + ",\n" +
               user.getEmail() + ",\n" +
               user.getPhone() + ",\n" +
               user.getBirthday() + ",\n" +
               user.getGender() + ",\n" +
               hobbies;
    }
}
