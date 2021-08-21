package com.imran.quantumit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.imran.quantumit.database.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {
    ImageView iv;
    SharedPrefManager sharedPrefManager;
    Boolean login;
    public static FirebaseAuth mAuth;
    public static GoogleSignInClient mgoogleSignInClient;
    FirebaseUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth=FirebaseAuth.getInstance();

        GoogleSignInOptions gso=new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("529142258271-k7t0dkkbtlu1dvoev7b02h9dqdkoi2sd.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mgoogleSignInClient= GoogleSignIn.getClient(SplashActivity.this,gso);

        sharedPrefManager=new SharedPrefManager(this);
        login=sharedPrefManager.isLoginIn();

        iv=findViewById(R.id.splashiv);
        currentuser= mAuth.getCurrentUser();
        animation();
    }

    private void animation()
    {
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.slideup);
        iv.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {

                if (login ==true || currentuser != null) {
                    gotohomepage();
                } else {
                    startActivity(new Intent(SplashActivity.this, SignupActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }
    private void gotohomepage()
    {
        Intent intent=new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}