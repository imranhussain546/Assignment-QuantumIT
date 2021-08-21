package com.imran.quantumit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.imran.quantumit.database.Database;
import com.imran.quantumit.database.MyDao;
import com.imran.quantumit.database.SharedPrefManager;
import com.imran.quantumit.model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    MyDao db;
    Database dataBase;
    EditText emailEt,passwordEt;
    Button buttonLogin;
    CardView googlecustombutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initview();
        dataBase = Room.databaseBuilder(this, Database.class, "userDB")
                .allowMainThreadQueries()
                .build();

        db = dataBase.getUserDao();
    }

    private void initview()
    {
        emailEt=findViewById(R.id.etEmailLogin);
        passwordEt=findViewById(R.id.etpasswordLogin);
        buttonLogin=findViewById(R.id.btnLogin);
        googlecustombutton=findViewById(R.id.googlebtn);
        buttonLogin.setOnClickListener(this);
        googlecustombutton.setOnClickListener(this);


    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

            case R.id.btnLogin:
                loginValidate();
                break;
            case R.id.googlebtn:
                GoogleSignin();
                break;

        }
    }
    private void GoogleSignin() {

        Intent signInIntent=SplashActivity.mgoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1001)
        {
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {

                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }catch(ApiException e)
            {
                Log.e("Apiexp",e.toString());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken)
    {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        SplashActivity.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }
                    }
                });
    }

    private void loginValidate() {
        String email=emailEt.getText().toString();
        String password=passwordEt.getText().toString();

        if (email.isEmpty())
        {
            emailEt.requestFocus();
            emailEt.setError("Please enter Email ID");
            return;
        }
        if (password.isEmpty())
        {
            passwordEt.requestFocus();
            passwordEt.setError("Enter Password");
            return;
        }
        validateUserAndLogin(email,password);

    }

    private void validateUserAndLogin(String email, String password)
    {
        User user =dataBase.getUserDao().getUser(email,password);



        if (user != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            SharedPrefManager sf=new SharedPrefManager(this);
            sf.saveUser(user);
            startActivity(i);
            finish();
        }else{
            //Log.e("savedata",user.getEmail().toString());
            Toast.makeText(LoginActivity.this, "Unregistered user, or incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this,SignupActivity.class));

    }

}