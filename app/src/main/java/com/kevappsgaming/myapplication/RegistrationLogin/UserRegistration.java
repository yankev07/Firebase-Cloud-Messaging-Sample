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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kevappsgaming.myapplication.Helper.InputValidation;
import com.kevappsgaming.myapplication.Helper.UserInformation;
import com.kevappsgaming.myapplication.R;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText userEmail;
    private TextInputEditText userPassword;
    private TextInputEditText userPasswordConfirmed;

    private TextInputLayout textInputLayoutFirstName;
    private TextInputLayout textInputLayoutLastName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirm;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private ProgressBar progressBar;
    private InputValidation inputValidation;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        firstName = (TextInputEditText) findViewById(R.id.textInputEditTextFirstName);
        lastName = (TextInputEditText) findViewById(R.id.textInputEditTextLastName);
        userEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        userPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        userPasswordConfirmed = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        textInputLayoutFirstName = (TextInputLayout) findViewById(R.id.textInputLayoutFirstName);
        textInputLayoutLastName = (TextInputLayout) findViewById(R.id.textInputLayoutLastName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirm = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.buttonRegister);
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);

        findViewById(R.id.relativeLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }

    private void userRegistration(){

        String first_name = firstName.getText().toString().trim();
        String last_name = lastName.getText().toString().trim();
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String passwordConfirmed = userPasswordConfirmed.getText().toString().trim();

        inputValidation = new InputValidation(UserRegistration.this);

        if(!inputValidation.isInputEditTextFilled(firstName, textInputLayoutFirstName, getString(R.string.error_message_username))){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(lastName, textInputLayoutFirstName, getString(R.string.error_message_username))){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(userEmail, textInputLayoutEmail, getString(R.string.error_message_email))){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(userPassword, textInputLayoutPassword, getString(R.string.error_message_pwdempty))){
            return;
        }
        if(!inputValidation.isInputEditTextMatches(userPassword, userPasswordConfirmed, textInputLayoutPassword, getString(R.string.error_message_pwdmatch))){
            return;
        }

        // If validations are ok, we'll first show a progress bar.
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    // User is successfully registered and logged in.
                    // we will start the profile activity from here
                    // right now let's display a toast only
                    Toast.makeText(UserRegistration.this, "Successful! Please login", Toast.LENGTH_SHORT).show();
                    saveUserInformation();
                    Intent intent = new Intent(UserRegistration.this, UserLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    if(task.getException()instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(UserRegistration.this, "You are already registered", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(UserRegistration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void saveUserInformation(){

        String first_name = firstName.getText().toString().trim();
        String last_name = lastName.getText().toString().trim();
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        UserInformation userInformation = new UserInformation(user.getUid(), first_name, last_name, email, password, "null", "null");

        databaseReference.child(user.getUid()).setValue(userInformation);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.textViewLinkRegister:
                finish();
                startActivity(new Intent(getApplicationContext(), UserLogin.class));
                break;

            case R.id.buttonRegister:
                userRegistration();
                break;
        }
    }
}
