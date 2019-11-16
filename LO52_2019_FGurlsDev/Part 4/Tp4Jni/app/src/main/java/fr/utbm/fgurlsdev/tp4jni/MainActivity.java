package fr.utbm.fgurlsdev.tp4jni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textShow;
    private Button btnRead;
    private Button btnWrite;
    private Button btnUp;
    private Button btndown;
    private Button btnLeft;
    private Button btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(NDKTools.sayHi());

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editContent = editText.getText().toString();
                if(!Outils.isCorrectContent(editContent)) Toast.makeText(MainActivity.this,"Please enter a number between 0 and 10!", Toast.LENGTH_SHORT).show();
                else textShow.setText(NDKTools.readBtn(editContent));
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editContent = editText.getText().toString();
                if(!Outils.isCorrectContent(editContent)) Toast.makeText(MainActivity.this,"Please enter a number between 0 and 10!", Toast.LENGTH_SHORT).show();
                else textShow.setText(NDKTools.writeBtn(editContent));
            }
        });

        btnUp.setOnClickListener(new DirectionBtnListener(textShow));
        btndown.setOnClickListener(new DirectionBtnListener(textShow));
        btnLeft.setOnClickListener(new DirectionBtnListener(textShow));
        btnRight.setOnClickListener(new DirectionBtnListener(textShow));
    }

    private void initView(){
        editText = findViewById(R.id.editText);
        textShow = findViewById(R.id.show_text);
        btnRead = findViewById(R.id.btn_read);
        btnWrite = findViewById(R.id.btn_write);
        btnUp = findViewById(R.id.btn_up);
        btndown = findViewById(R.id.btn_down);
        btnLeft = findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);
    }
}
