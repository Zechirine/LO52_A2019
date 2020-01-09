package com.utbm.keepit.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.utbm.keepit.R;
import com.utbm.keepit.backend.entity.Exercise;
import com.utbm.keepit.backend.service.ExerciseService;
import com.utbm.keepit.backend.service.ExerciseWithJoinSeance;

import java.util.List;

public class StopwatchActivity extends AppCompatActivity {
    //Numbers of seconds displayed in the stopwatch 

    private ExerciseService exerciseService = new ExerciseService();
    private Long tid;
    //private List<Integer> seconds;

    volatile ImageView exerImage;
    private Button previousExe;
    private Button nextExe;

    volatile private boolean running;
    volatile private int seconds;
    volatile private List<ExerciseWithJoinSeance> listExerciceData;
    volatile private int i; // index of exercises

    @Override
    public void onBackPressed() {
        //running 结束才可以退出
        if(running == false){
            super.onBackPressed();
        }
//        super.onBackPressed();//注销该方法，相当于重写父类这个方法
    }

    public void changeImageView(){
        try{
            exerImage.setImageURI(Uri.parse(listExerciceData.get(i).e.getImageResource()));
        }catch(Exception e){
            exerImage.setImageResource(R.mipmap.muscle);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_stopwatch);
        tid=getIntent().getExtras().getLong("seanceid");
        listExerciceData=exerciseService.findBySceanceId(tid);

        exerImage = findViewById(R.id.stopWatchImage);
        previousExe = findViewById(R.id.previousExercise);
        nextExe = findViewById(R.id.nextExercise);

        changeImageView();

            i =0;
            seconds = listExerciceData.get(i).jse.getDuration();
            runTimer();
    }

//    onClickprevious" onClickNext
    public void onClickprevious(View view){
        if(i==0){
            Toast.makeText(StopwatchActivity.this, R.string.FirstExe, Toast.LENGTH_SHORT).show();
        }else{
            i--;
            seconds = listExerciceData.get(i).jse.getDuration();
            changeImageView();
            running=false;
        }
    }

    public void onClickNext(View view){
        if(i==listExerciceData.size()-1){
            Toast.makeText(StopwatchActivity.this, R.string.LastExe, Toast.LENGTH_SHORT).show();
        }else{
            i++;
            seconds = listExerciceData.get(i).jse.getDuration();
            changeImageView();
            running=false;
        }
    }

    public void onClickStart(View view){ running = true;
        }
    //Stop the stopwatch running when the Stop button is clicked 
    public void onClickStop(View view){
            running = false;
        }
    //Reset the stopwatch running when the Reset button is clicked 
    public void onClickReset(View view){
            running = false;
            seconds = listExerciceData.get(i).jse.getDuration();
    }

    private void runTimer(){

        final TextView timeView = (TextView)findViewById(R.id.time_view);
       /* for (int i = 0; i < listExerciceData.size() - 1; i++) {
            int seconds = listExerciceData.get(i).jse.getDuration();*/

        final Handler handler = new Handler();

            handler.post(new Runnable() {
                @Override

                public void run() {
                    Exercise exe = listExerciceData.get(i).e;
                    String uri = listExerciceData.get(i).e.getImageResource();
//                    System.out.println(uri);
                    int hours = seconds / 3600;
                    int minutes = (seconds % 3600) / 60;
                    int secs = seconds % 60;
                    String time = String.format("%d:%02d:%02d", hours, minutes, secs);
                    timeView.setText(time);
                    if (running) {
                        seconds--;
                        if (seconds > 0) {
//                        //TODO : 每30s 每 15s
                            if(seconds % 30 == 0){
                                int h,m,s;
                                h= seconds / 3600;
                                m= seconds%3600 / 60;
                                s= seconds-3600*h-60*m;
                                String th="",tm="",ts="";
                                if(h!=0){th=h+"H ";}
                                if(m!=0){tm=m+"M ";}
                                if(s!=0){ts=s+"S ";}
                                String timeLeft  = "Il reste "+th+tm+ts;
                                Toast.makeText(StopwatchActivity.this, timeLeft, Toast.LENGTH_SHORT).show();
                                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                vib.vibrate(1000);
                            }
                        }
                        else {// Seance 结束的时候震动
                            if(i==listExerciceData.size()-1){
                                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                vib.vibrate(2000);
                                Toast.makeText(StopwatchActivity.this, R.string.felicitation, Toast.LENGTH_SHORT).show();
                                running = false;
                            }
                            else if (i < listExerciceData.size()) {
                                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                vib.vibrate(1000);
                                i++;
                                seconds = listExerciceData.get(i).jse.getDuration();
                                changeImageView();
                            }
                        }
                    }
                    handler.postDelayed(this, 1000);

                }

            });
    }
}
