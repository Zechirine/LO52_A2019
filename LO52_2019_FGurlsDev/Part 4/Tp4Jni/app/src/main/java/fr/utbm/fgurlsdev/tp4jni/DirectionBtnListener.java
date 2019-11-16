package fr.utbm.fgurlsdev.tp4jni;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DirectionBtnListener implements View.OnClickListener {
    private Button btn;
    private TextView tv;

    public DirectionBtnListener(){}

    public DirectionBtnListener(TextView tv){
        this.tv = tv;
    }

    @Override
    public void onClick(View v) {
        btn = v.findViewById(v.getId());
        tv.setText(NDKTools.directionBtn(btn.getText().toString()));
    }
}
