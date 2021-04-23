package com.example.exercicio1topicos;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
import java.util.Date;
import java.util.List;

public class CustomerFormActivity extends AppCompatActivity implements Validator.ValidationListener {

    private Retrofit retrofit;
    private CustomerService customerService;
    private TextInputLayout txtName;
    private TextInputLayout txtEmail;
    private TextInputLayout txtPhone;
    private TextInputLayout txtBirthday;

    @NotEmpty(messageResId = R.string.error_required_field)
    @Length(min = 3, max = 20, messageResId = R.string.error_name_field)
    private TextInputEditText etName;

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
        txtPhone = findViewById(R.id.txt_phone);
        txtBirthday = findViewById(R.id.txt_birthday);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etBirthday = findViewById(R.id.et_birthday);
        rgBlackList = findViewById(R.id.rg_gender);
        rbSelectedBlackList = findViewById(R.id.rb_male);
        btRegister = findViewById(R.id.bt_register);
        customer = new Customer();

        validator = new Validator(this);
        validator.setValidationListener(this);
        applyInputMasks();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.customerService = retrofit.create(CustomerService.class);
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
        txtPhone.setError("");
        txtBirthday.setError("");
    }

    private void createCustomer() throws ParseException {
        customer.setName(etName.getText().toString());
        customer.setPhoneNumber(etPhone.getText().toString());

        if(rbSelectedBlackList.getText().toString().equals(R.string.yes)) {
            customer.setBlackList(true);
        } else {
            customer.setBlackList(false);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_formatter));
        Date date = sdf.parse(etBirthday.getText().toString());
        customer.setBirthDateInMillis(date.getTime());

        Date creationDate = new Date();
        customer.setCreationTimestamp(creationDate.toString());

        customerService.create(customer).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        R.string.customer_register_success ,
                        Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "deu ruim",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        try {
            createCustomer();
        } catch (ParseException e) {
            e.printStackTrace();
            Snackbar.make(getWindow().getDecorView().getRootView(),
                    "deu ruim no parse",
                    Snackbar.LENGTH_LONG).show();
        }
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