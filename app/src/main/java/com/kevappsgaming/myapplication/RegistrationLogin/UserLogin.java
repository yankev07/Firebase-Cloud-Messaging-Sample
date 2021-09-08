package com.kevappsgaming.myapplication.RegistrationLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kevappsgaming.myapplication.Helper.InputValidation;
import com.kevappsgaming.myapplication.Helper.PreferenceUtils;
import com.kevappsgaming.myapplication.R;
import com.kevappsgaming.myapplication.WelcomeActivity;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = UserLogin.this;

    private TextInputEditText userEmail;
    private TextInputEditText userPassword;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView appCompatTextViewRegisterLink;

    private InputValidation inputValidation;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        userEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        userPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.buttonLogin);
        appCompatTextViewRegisterLink = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

        appCompatButtonLogin.setOnClickListener(this);
        appCompatTextViewRegisterLink.setOnClickListener(this);

        if (PreferenceUtils.getEmail(this) != null ){
            userEmail.setText(PreferenceUtils.getEmail(this));
            userPassword.setText(PreferenceUtils.getPassword(this));
        }else{

        }

        findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }

    private void userLogin(){

        inputValidation = new InputValidation(activity);

        if(!inputValidation.isInputEditTextFilled(userEmail, textInputLayoutEmail, getString(R.string.error_message_email))){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(userPassword, textInputLayoutPassword, getString(R.string.error_message_pwdempty))){
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(userEmail.getText().toString().trim(), userPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    // add flags to clear all the opened activities
                    // otherwise the app will come back to login activity if the user presses "down"
                    PreferenceUtils.saveEmail(userEmail.getText().toString().trim(), getApplicationContext());
                    PreferenceUtils.savePassword(userPassword.getText().toString().trim(), getApplicationContext());
                    Intent intent = new Intent(UserLogin.this, WelcomeActivity.class);
                    intent.putExtra("email", userEmail.getText().toString().trim());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void fetchUserName(String email){

    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.textViewLinkRegister:
                finish();
                startActivity(new Intent(getApplicationContext(), UserRegistration.class));
                break;

            case R.id.buttonLogin:
                userLogin();
                break;
        }
    }
}
