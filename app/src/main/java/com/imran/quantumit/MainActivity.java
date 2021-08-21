package com.imran.quantumit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.imran.quantumit.database.SharedPrefManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private BottomNavigationView bottomNavigationView;
    SharedPrefManager sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sf=new SharedPrefManager(MainActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.Navigationheader,R.string.NavigationClose);
        toggle.syncState();
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_Brands,R.id.nav_offer,R.id.nav_sale,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_home:
                        loadFragment(new HomeFragment());
                        return true;
                    case R.id.navigation_offer:
                        loadFragment(new OfferFragment());
                        return true;

                    case R.id.navigation_service:
                        loadFragment(new SaleFragment());
                        return true;

                    case R.id.navigation_brand:
                        loadFragment(new BrandFragment());
                        return true;
                    case R.id.navigation_logout:
                       logout();
                        return true;
                }
                return false;
            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main, new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        getuserdata();
    }
    private void getuserdata()
    {
        View view=navigationView.getHeaderView(0);
        ImageView timage=view.findViewById(R.id.navpimage);
        TextView tname=view.findViewById(R.id.navpname);
        TextView temail=view.findViewById(R.id.navpemail);

        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null) {
            Glide.with(this).load(account.getPhotoUrl()).into(timage);
            tname.setText(account.getDisplayName());
            temail.setText(account.getEmail());
        }
        else{

        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                loadFragment(new HomeFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_sale:
                loadFragment(new SaleFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_offer:
                loadFragment(new OfferFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_Brands:
                loadFragment(new BrandFragment());
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_logout:
                logout();
                drawer.closeDrawer(GravityCompat.START);
                break;

        }
        return false;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.commit();
    }

    public void logout() {
        SplashActivity.mgoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    SplashActivity.mAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
        sf.logout();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show( );
    }
}