package com.example.exercicio1topicos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener {

    private TextInputLayout txtName;
    private TextInputLayout txtEmail;
    private TextInputLayout txtPhone;
    private TextInputLayout txtBirthday;

    @NotEmpty(message = "Campo obrigatório.")
    @Length(min = 3, max = 20, message = "O nome deve ter entre 3 e 20 caracteres.")
    private TextInputEditText etName;

    @NotEmpty(message = "Campo obrigatório.")
    @Email(message = "E-mail inválido.")
    private TextInputEditText etEmail;

    @NotEmpty(message = "Campo obrigatório.")
    @Length(min = 13, max = 13, message = "O telefone deve ter 13 caracteres.")
    private TextInputEditText etPhone;

    @NotEmpty(message = "Campo obrigatório.")
    @Length(min = 10, max = 10, message = "A data de nascimento deve ter 10 caracteres.")
    private TextInputEditText etBirthday;

    private RadioGroup rgGender;
    private RadioButton rbSelectedGender;
    private CheckBox cbMusic;
    private CheckBox cbMovies;
    private CheckBox cbVideogames;
    private Button btRegister;
    private Button btSend;
    private User user;
    private ArrayList<User> userList;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();

        applyInputMasks();

        this.rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSelectedGender = findViewById(checkedId);
            }
        });

        this.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeErrors();
                validator.validate();
            }
        });

        this.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userList.size() > 0){
                    Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("USER_LIST", userList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private ArrayList<String> getCheckedHobbies() {
        ArrayList<String> checkedHobbies = new ArrayList<>();
        if(cbMusic.isChecked()){
            checkedHobbies.add(cbMusic.getText().toString());
        }
        if(cbMovies.isChecked()){
            checkedHobbies.add(cbMovies.getText().toString());
        }
        if(cbVideogames.isChecked()){
            checkedHobbies.add(cbVideogames.getText().toString());
        }
        return checkedHobbies;
    }

    private void initializeComponents() {
        this.txtName = findViewById(R.id.txt_name);
        this.txtEmail = findViewById(R.id.txt_email);
        this.txtPhone = findViewById(R.id.txt_phone);
        this.txtBirthday = findViewById(R.id.txt_birthday);
        this.etName = findViewById(R.id.et_name);
        this.etEmail = findViewById(R.id.et_email);
        this.etPhone = findViewById(R.id.et_phone);
        this.etBirthday = findViewById(R.id.et_birthday);
        this.rgGender = findViewById(R.id.rg_gender);
        this.rbSelectedGender = findViewById(R.id.rb_male);
        this.cbMusic = findViewById(R.id.cb_music);
        this.cbMovies = findViewById(R.id.cb_movies);
        this.cbVideogames = findViewById(R.id.cb_videogames);
        this.btRegister = findViewById(R.id.bt_register);
        this.btSend = findViewById(R.id.bt_send);
        this.user = new User();
        this.userList = new ArrayList<>();
        this.validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void applyInputMasks() {
        SimpleMaskFormatter dateFormatter = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher phoneTextWatcher = new MaskTextWatcher(etBirthday, dateFormatter);
        etBirthday.addTextChangedListener(phoneTextWatcher);

        SimpleMaskFormatter phoneFormatter = new SimpleMaskFormatter("(NN)NNNNNNNNN");
        MaskTextWatcher dateTextWatcher = new MaskTextWatcher(etPhone, phoneFormatter);
        etPhone.addTextChangedListener(dateTextWatcher);
    }

    private void removeErrors(){
        txtName.setError("");
        txtEmail.setError("");
        txtPhone.setError("");
        txtBirthday.setError("");
    }

    @Override
    public void onValidationSucceeded() {
        user.setName(etName.getText().toString());
        user.setEmail(etEmail.getText().toString());
        user.setPhone(etPhone.getText().toString());
        user.setGender(rbSelectedGender.getText().toString());
        user.setHobbies(getCheckedHobbies());
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
            user.setBirthday(df.parse(etBirthday.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(),user.toString(),Toast.LENGTH_SHORT).show();
        userList.add(user);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError e:errors) {
            View view = e.getView();
            String errorMessage = e.getCollatedErrorMessage(this);

            if(view instanceof TextInputEditText){
                switch (view.getId()){
                    case R.id.et_name:
                        txtName.setError(errorMessage);
                        break;
                    case R.id.et_email:
                        txtEmail.setError(errorMessage);
                        break;
                    case R.id.et_phone:
                        txtPhone.setError(errorMessage);
                        break;
                    case R.id.et_birthday:
                        txtBirthday.setError(errorMessage);
                        break;
                }
            }

        }
    }
}
