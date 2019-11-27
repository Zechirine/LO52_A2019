package com.utbm.tp4_jni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        this.ed= findViewById(R.id.Def);
        //ed.setText(stringFromJNI());
        Button left,right,up,down,read,write;

        left =findViewById(R.id.LEFT);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.setText(linksFromJNI());
            }
        });
        right =findViewById(R.id.RIGHT);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.setText(rechtFromJNI());
            }
        });
        up =findViewById(R.id.UP);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.setText(obenFromJNI());
            }
        });
        down =findViewById(R.id.Down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.setText(niederFromJNI());
            }
        });

        read =findViewById(R.id.READ);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int min=0;
                int max=10;
                Random rand=new Random();
                int nombreAleatoire = rand.nextInt(max - min + 1) + min;
                ed.setText("READ : "+squareFromJNI(nombreAleatoire));
            }
        });
        write =findViewById(R.id.WRITE);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int min=0;
                int max=10;
                Random rand=new Random();
                int nombreAleatoire = rand.nextInt(max - min + 1) + min;
                ed.setText("WRITE : "+squareFromJNI(nombreAleatoire));
            }
        });
    }



    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native int squareFromJNI(int a);
    public native int cubedFromJNI(int a);
    public native String obenFromJNI();
    public native String niederFromJNI();
    public native String rechtFromJNI();
    public native String linksFromJNI();
}
