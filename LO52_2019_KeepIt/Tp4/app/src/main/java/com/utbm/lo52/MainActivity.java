package com.utbm.lo52;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText ed;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button left,right,up,down,read,write;

        left=findViewById(R.id.left);
        right=findViewById(R.id.right);
        up=findViewById(R.id.up);
        down=findViewById(R.id.down);
        read=findViewById(R.id.read);
        write=findViewById(R.id.write);
        ed=findViewById(R.id.ed);
        // Example of a call to a native method
//        TextView tv = findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        left.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ed.setText(left());
            }
        });
        right.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ed.setText(right());
            }
        });
        up.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ed.setText(up());
            }
        });
        down.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ed.setText(down());
            }
        });
        read.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int number=Integer.parseInt(ed.getText().toString());
                ed.setText(""+read(number));
            }
        });
        write.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int number=Integer.parseInt(ed.getText().toString());
                ed.setText(""+write(number));
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String left();
    public native String right();
    public native String down();
    public native String up();
    public native int write(int number);
    public native int read(int number);
}
