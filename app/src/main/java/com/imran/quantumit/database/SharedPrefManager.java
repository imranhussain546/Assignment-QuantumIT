package com.imran.quantumit.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.imran.quantumit.model.User;


public class SharedPrefManager {

    public static String SHA_NAME="LLLOG";
    private SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public void saveUser(User u)
    {
        sharedPreferences=context.getSharedPreferences(SHA_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        //editor.putString("First",u.getFirst());
       // editor.putString("Last",u.getLast());
        editor.putString("Email",u.getEmail());
        editor.putBoolean("logged",true);
        editor.apply();

    }

    public boolean isLoginIn()
    {
        sharedPreferences=context.getSharedPreferences(SHA_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged",false);
    }

    public void logout()
    {
        sharedPreferences=context.getSharedPreferences(SHA_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
