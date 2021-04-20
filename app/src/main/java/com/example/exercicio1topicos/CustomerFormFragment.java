package com.example.exercicio1topicos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerFormFragment extends Fragment implements Validator.ValidationListener {

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

    private RadioGroup rgGender;
    private RadioButton rbSelectedGender;
    private CheckBox cbMusic;
    private CheckBox cbMovies;
    private CheckBox cbVideogames;
    private Button btRegister;
    private User user;
    private ArrayList<User> userList;

    private Validator validator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_form, container, false);

        initializeComponents(view);

        this.rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbSelectedGender = getView().findViewById(checkedId);
            }
        });

        this.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeErrors();
                validator.validate();
            }
        });

        return view;
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

    private void initializeComponents(View view) {
        txtName = (TextInputLayout) view.findViewById(R.id.txt_name);
        txtEmail = (TextInputLayout) view.findViewById(R.id.txt_email);
        txtPhone = (TextInputLayout) view.findViewById(R.id.txt_phone);
        txtBirthday = (TextInputLayout) view.findViewById(R.id.txt_birthday);
        etName = (TextInputEditText) view.findViewById(R.id.et_name);
        etEmail = (TextInputEditText) view.findViewById(R.id.et_email);
        etPhone = (TextInputEditText) view.findViewById(R.id.et_phone);
        etBirthday = (TextInputEditText) view.findViewById(R.id.et_birthday);
        rgGender = (RadioGroup) view.findViewById(R.id.rg_gender);
        rbSelectedGender = (RadioButton) view.findViewById(R.id.rb_male);
        cbMusic = (CheckBox) view.findViewById(R.id.cb_music);
        cbMovies = (CheckBox) view.findViewById(R.id.cb_movies);
        cbVideogames = (CheckBox) view.findViewById(R.id.cb_videogames);
        btRegister = (Button) view.findViewById(R.id.bt_register);
        user = new User();
        userList = new ArrayList<>();
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
        user.setName(etName.getText().toString());
        user.setEmail(etEmail.getText().toString());
        user.setPhone(etPhone.getText().toString());
        user.setGender(rbSelectedGender.getText().toString());
        user.setHobbies(getCheckedHobbies());
        try {
            SimpleDateFormat df = new SimpleDateFormat(getString(R.string.date_formatter));
            user.setBirthday(df.parse(etBirthday.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Toast.makeText(getContext(), user.toString(), Toast.LENGTH_SHORT).show();
        userList.add(user);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError e:errors) {
            View view = e.getView();
            String errorMessage = e.getCollatedErrorMessage(getContext());

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