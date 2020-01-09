package com.utbm.keepit.ui.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.utbm.keepit.R;
import com.utbm.keepit.ui.views.PickerView;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDialog extends Dialog {

    //spinner data
    private List<String> allExercises ;
    private Spinner spinnerExercise;
    private ArrayAdapter<String> adapterExercise;
    //时间选项框
    private PickerView hourPick,secondPick,minutePick;
    private static String hour="0",minute="0" ,second="0" ;

    private int duration;
    private long eId;

    private Button yes;//确定按钮
    private Button no;//取消按钮

    /**
     * 自定义Dialog监听器
     */
    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后
         * 刷新进入 dialg的活动的数据
         */
        void setExerciseSelectedId(Long id);
        void setExerciseDuration(int duration, Long id);
    }

    private PriorityListener listener;

    public ExerciseDialog(Context context, List<String> allExe, PriorityListener listener) {
        super(context);
        this.allExercises= allExe;
        this.listener=listener;
//                new ArrayList<String>();
//        for(String s: allExe){
//            allExercises.add(s);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_choose_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();

        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        hourPick = findViewById(R.id.seance_exercise_hour_pick);
        secondPick = findViewById(R.id.seance_exercise_second_pick);
        minutePick = findViewById(R.id.seance_exercise_minute_pick);;
        spinnerExercise  = findViewById(R.id.seance_exercise_chose);
        yes = (Button) findViewById(R.id.seance_exercise_yes);
        no = (Button) findViewById(R.id.seance_exercise_no);

        List<String> hours = new ArrayList<String>();
        List<String> seconds = new ArrayList<String>();
        for (int i = 0; i < 24; i++)
        {
            hours.add(i < 10 ? "0" + i : "" + i);
        }
        for (int i = 0; i < 60; i++)
        {
            seconds.add(i < 10 ? "0" + i : "" + i);
        }
        hourPick.setData(hours);
        minutePick.setData(seconds);
        secondPick.setData(seconds);

        hourPick.setOnSelectListener(new PickerView.onSelectListener()
        {

            @Override
            public void onSelect(String text)
            {
                hour = text;
            }
        });

        minutePick.setOnSelectListener(new PickerView.onSelectListener()
        {

            @Override
            public void onSelect(String text)
            {                minute = text;
            }
        });

        secondPick.setOnSelectListener(new PickerView.onSelectListener()
        {

            @Override
            public void onSelect(String text)
            {
//                Toast.makeText(getActivity().this, "选择了 " + text + " 秒",
//                        Toast.LENGTH_SHORT).show();
                second = text;
            }
        });

        adapterExercise=new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_item,allExercises);
        adapterExercise.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercise.setAdapter(adapterExercise);

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {

        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedOrder = spinnerExercise.getSelectedItemPosition();
                String selectedE = allExercises.get(selectedOrder);
                if(selectedE == null){
                    Toast.makeText(getContext().getApplicationContext(), "select one exercise", Toast.LENGTH_SHORT).show();
                }
                else if(hour=="0"&&minute=="0"&&second=="0"){
                    Toast.makeText(getContext().getApplicationContext(), "please choisir duration", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Long eId = Long.valueOf(selectedE.split(":")[0]);
                            //tempExercises.add(exerciseService.findExerciseById((Long)eId));
                            //exerciceListAdapter.notifyDataSetChanged();
                    int duration = Integer.parseInt(hour)*3600+Integer.parseInt(minute)*60+Integer.parseInt(second);

                    System.out.println(eId+" "+duration);
                    listener.setExerciseDuration(duration,eId);
                    listener.setExerciseSelectedId(eId);

                    dismiss();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
//
//    /**
//     * 设置确定按钮和取消被点击的接口
//     */
//    public interface onYesOnclickListener {
//        public void onYesClick();
//    }
//
//    public interface onNoOnclickListener {
//        public void onNoClick();
//    }
}
