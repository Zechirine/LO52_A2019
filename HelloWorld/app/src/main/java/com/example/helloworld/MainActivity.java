package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Envoie sur l'activité HelloWorldTxt quand on clique sur le bouton
    public void helloWorld(View view)
    {
        Intent intent = new Intent(MainActivity.this, HelloWorldTxt.class);
        startActivity(intent);
    }
}
