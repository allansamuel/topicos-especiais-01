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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CustomerFormActivity extends AppCompatActivity {

    private Bundle bundle;

    private Retrofit retrofit;
    private CustomerService customerService;

    private TextInputLayout txtName;
    private TextInputLayout txtPhone;
    private TextInputLayout txtBirthday;
    private TextInputEditText etName;
    private TextInputEditText etPhone;
    private TextInputEditText etBirthday;

    private RadioGroup rgBlackList;
    private RadioButton rbSelectedBlackList;
    private RadioButton rbBlackListNo;
    private RadioButton rbBlackListYes;

    private Button btRegister;
    private LinearLayout llExistingCustomer;
    private Button btDelete;
    private Button btEdit;

    private Customer customer;

    private SimpleDateFormat dateFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_form);

        initializeComponents();

        rgBlackList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSelectedBlackList = findViewById(checkedId);
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getCustomerFromForm();
                    createCustomer();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getCustomerFromForm();
                    deleteCustomer();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getCustomerFromForm();
                    editCustomer();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean hasCustomerBundle () {
        bundle = getIntent().getExtras();
        if(bundle != null) {
            return true;
        }
        return false;
    }

    private void initializeComponents() {
        dateFormat = new SimpleDateFormat(getString(R.string.date_formatter));

        txtName = findViewById(R.id.txt_name);
        txtPhone = findViewById(R.id.txt_phone);
        txtBirthday = findViewById(R.id.txt_birthday);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etBirthday = findViewById(R.id.et_birthday);

        rgBlackList = findViewById(R.id.rg_gender);
        rbBlackListNo = findViewById(R.id.rb_blacklist_no);
        rbBlackListYes = findViewById(R.id.rb_blacklist_yes);
        rbSelectedBlackList = findViewById(R.id.rb_blacklist_no);

        btRegister = findViewById(R.id.bt_register);
        llExistingCustomer = findViewById(R.id.ll_existing_customer);
        btDelete = findViewById(R.id.bt_delete);
        btEdit = findViewById(R.id.bt_edit);
        applyInputMasks();

        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        customerService = retrofit.create(CustomerService.class);

        if(hasCustomerBundle()) {
            customer = (Customer) bundle.getSerializable("CUSTOMER_OBJECT");
            btRegister.setVisibility(View.INVISIBLE);
            llExistingCustomer.setVisibility(View.VISIBLE);

            etName.setText(customer.getName());
            etPhone.setText(customer.getPhoneNumber());

            Date birthday = new Date(customer.getBirthDateInMillis());
            etBirthday.setText(dateFormat.format(birthday));


            if(customer.isBlacklist()){
                rbBlackListYes.setChecked(true);
            }else{
                rbBlackListNo.setChecked(true);
            }
        }else{
            customer = new Customer();
        }
    }

    private void applyInputMasks() {
        SimpleMaskFormatter dateFormatter = new SimpleMaskFormatter(getString(R.string.date_mask));
        MaskTextWatcher phoneTextWatcher = new MaskTextWatcher(etBirthday, dateFormatter);
        etBirthday.addTextChangedListener(phoneTextWatcher);

        SimpleMaskFormatter phoneFormatter = new SimpleMaskFormatter(getString(R.string.phone_mask));
        MaskTextWatcher dateTextWatcher = new MaskTextWatcher(etPhone, phoneFormatter);
        etPhone.addTextChangedListener(dateTextWatcher);
    }

    private void getCustomerFromForm() throws ParseException {
        customer.setName(etName.getText().toString());
        customer.setPhoneNumber(etPhone.getText().toString());

        if(rbBlackListYes.isChecked()) {
            customer.setBlacklist(true);
        } else {
            customer.setBlacklist(false);
        }

        Date date = dateFormat.parse(etBirthday.getText().toString());
        customer.setBirthDateInMillis(date.getTime());
    }

    private void createCustomer() {
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

    private void deleteCustomer() {
        customerService.delete(customer.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "deu ruim",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void editCustomer() {
        customerService.update(customer.getId(), customer).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
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
}