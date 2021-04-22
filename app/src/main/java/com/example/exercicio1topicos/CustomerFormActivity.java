package com.example.exercicio1topicos;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormActivity extends AppCompatActivity implements Validator.ValidationListener {

    private TextInputLayout txtName;
    private TextInputLayout txtEmail;
    private TextInputLayout txtPhone;
    private TextInputLayout txtBirthday;

    @NotEmpty(messageResId = R.string.error_required_field)
    @Length(min = 3, max = 20, messageResId = R.string.error_name_field)
    private TextInputEditText etName;

    @NotEmpty(messageResId = R.string.error_required_field)
    @Email(messageResId = R.string.error_email_field)
    private TextInputEditText etEmail;

    @NotEmpty(messageResId = R.string.error_required_field)
    @Length(min = 13, max = 13, messageResId = R.string.error_phone_field)
    private TextInputEditText etPhone;

    @NotEmpty(messageResId = R.string.error_required_field)
    @Length(min = 10, max = 10, messageResId = R.string.error_birthday_field)
    private TextInputEditText etBirthday;

    private RadioGroup rgBlackList;
    private RadioButton rbSelectedBlackList;
    private Button btRegister;
    private Customer customer;

    private Validator validator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_form);

        initializeComponents();

        this.rgBlackList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSelectedBlackList = findViewById(checkedId);
            }
        });

        this.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeErrors();
                validator.validate();
            }
        });
    }

    private void initializeComponents() {
        txtName = findViewById(R.id.txt_name);
        txtEmail = findViewById(R.id.txt_email);
        txtPhone = findViewById(R.id.txt_phone);
        txtBirthday = findViewById(R.id.txt_birthday);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etBirthday = findViewById(R.id.et_birthday);
        rgBlackList = findViewById(R.id.rg_gender);
        rbSelectedBlackList = findViewById(R.id.rb_male);
        btRegister = findViewById(R.id.bt_register);
        customer = new Customer();

        validator = new Validator(this);
        validator.setValidationListener(this);
        applyInputMasks();
    }

    private void applyInputMasks() {
        SimpleMaskFormatter dateFormatter = new SimpleMaskFormatter(getString(R.string.date_mask));
        MaskTextWatcher phoneTextWatcher = new MaskTextWatcher(etBirthday, dateFormatter);
        etBirthday.addTextChangedListener(phoneTextWatcher);

        SimpleMaskFormatter phoneFormatter = new SimpleMaskFormatter(getString(R.string.phone_mask));
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
        customer.setName(etName.getText().toString());
        customer.setEmail(etEmail.getText().toString());
        customer.setPhoneNumber(etPhone.getText().toString());

        if(rbSelectedBlackList.getText().toString().equals(R.string.yes)) {
            customer.setBlackList(true);
        } else {
            customer.setBlackList(false);
        }

        try {
            SimpleDateFormat df = new SimpleDateFormat(getString(R.string.date_formatter));
            customer.setBirthday(df.parse(etBirthday.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        userList.add(user);

        Snackbar.make(getWindow().getDecorView().getRootView(),
                R.string.customer_register_success ,
                Snackbar.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER_LIST", userList);
        intent.putExtras(bundle);
        startActivity(intent);
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