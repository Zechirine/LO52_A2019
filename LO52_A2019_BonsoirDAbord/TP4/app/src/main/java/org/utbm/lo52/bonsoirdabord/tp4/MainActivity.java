package org.utbm.lo52.bonsoirdabord.tp4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView label; //Used by native

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = findViewById(R.id.main_label);
    }

    public native void callNativeRead(int nbr);
    public native void callNativeWrite(int nbr);
    public native void callNativeButton(String str);

    private int parseInput() {
        EditText field = findViewById(R.id.main_field);
        String content = field.getText().toString();
        int ret;

        try {
            ret = Integer.parseInt(content);
        } catch(NumberFormatException ex) {
            field.setError(getText(R.string.invalid_number));
            return -1;
        }

        if(ret < 0 || ret > 10) {
            field.setError(getText(R.string.number_out_of_range));
            return -1;
        }

        return ret;
    }

    public void onDirectionClicked(View v) {
        String label = ((Button) v).getText().toString();
        callNativeButton(label);
    }

    public void onReadClicked(View v) {
        int nbr = parseInput();
        if(nbr < 0)
            return;

        callNativeRead(nbr);
    }

    public void onWriteClicked(View v) {
        int nbr = parseInput();
        if(nbr < 0)
            return;

        callNativeWrite(nbr);
    }
}
