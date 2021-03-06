package com.example.workshopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editTextEmail, editTextPhone, editTextPassword, editTextPassword2, ediTextName;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        ediTextName = (EditText) findViewById(R.id.RegisterName);
        editTextEmail = (EditText) findViewById(R.id.RegisterEmail);
        editTextPhone = (EditText) findViewById(R.id.RegisterPhone);
        editTextPassword = (EditText) findViewById(R.id.RegisterPassword);
        editTextPassword2 = (EditText) findViewById(R.id.RegisterPassword2);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupTip);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void Register(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        String name = ediTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        String tip = "PostaroLice";

        int ID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(ID);

        if(radioButton.getId() == R.id.radioVolonter) {
            tip = "Volonter";
        }

        if(name.equals("")) {
            ediTextName.setError("???????????????????????? ????????!");
            ediTextName.requestFocus();
            return;
        }

        if(email.equals("")) {
            editTextEmail.setError("???????????????????????? ????????!");
            editTextEmail.requestFocus();
            return;
        } else {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("?????????????? ?????????????? ??-???????? ????????????!");
                editTextEmail.requestFocus();
                return;
            }
        }

        if(password.equals("")) {
            editTextPassword.setError("???????????????????????? ????????!");
            editTextPassword.requestFocus();
            return;
        } else {
            if(password.length() < 6) {
                editTextPassword.setError("?????????????? ???????????? ???? 6 ??????????????????!");
                editTextPassword.requestFocus();
                return;
            }
        }

        if(password2.equals("")) {
            editTextPassword2.setError("???????????????????????? ????????!");
            editTextPassword2.requestFocus();
            return;
        }

        if(!password.equals(password2)) {
            editTextPassword.setError("?????????????????? ???? ???? ??????????????????????!");
            editTextPassword.requestFocus();
            return;
        }

        if(phone.equals("")) {
            editTextPhone.setError("???????????????????????? ????????!");
            editTextPhone.requestFocus();
            return;
        } else {
            if(!Patterns.PHONE.matcher(phone).matches()) {
                editTextPhone.setError("?????????????? ?????????????? ???????????????????? ????????!");
                editTextPhone.requestFocus();
                return;
            }
        }


        String finalTip = tip;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    User user = new User(email, phone, finalTip, name,0,0);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "?????????????? ???? ??????????????????????????!", Toast.LENGTH_SHORT).show();

                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "?????????????????? ????????????????????????.?????????????? ???? ????????????????!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "?????????????????? ????????????????????????.?????????????? ???? ????????????????!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}