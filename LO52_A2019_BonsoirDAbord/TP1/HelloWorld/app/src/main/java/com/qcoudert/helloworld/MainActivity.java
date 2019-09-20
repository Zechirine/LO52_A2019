package com.qcoudert.helloworld;

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

    /**
     * Fonction onClick liée au bouton R.id.button
     * Lance l'activité {@link HelloWorldActivity} afin d'afficher "Hello World !"
     * @param v - View cliquée
     */
    public void onClickButton(View v) {
        Intent i = new Intent(getApplicationContext(), HelloWorldActivity.class);
        startActivity(i);
    }
}
