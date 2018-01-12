package com.gamezone.loliman.lolimanzgame.gridview;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gamezone.loliman.lolimanzgame.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Loliman on 2018/1/10.
 */

public class GameGridViewActivity extends AppCompatActivity {

    private GridView game_grid_view;
    private int difficulty_level = 0;
    private int view_height = 0;
    private String first_click = "";
    private String second_click = "";
    private int prev_position = -1;
    private ProgressBar progress_bar;
    private int progress;
    private Timer timer;
    private TimerTask timerTask;
    private GameCountDownTimer gameCountDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_grid);
        game_grid_view = findViewById(R.id.id_game_grid_001);
        progress_bar = findViewById(R.id.id_game_progress_004);
        progress = progress_bar.getProgress();

        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("game_bundle");
        String sDifficulty = bundle.getString("difficulty_level");
        SerializableHashMap game_map = (SerializableHashMap) bundle.getSerializable("game_map");

        difficulty_level = Integer.parseInt(sDifficulty);
        game_grid_view.setNumColumns(difficulty_level);

        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams)game_grid_view.getLayoutParams();
        Params.height = dm2.widthPixels;
        view_height = dm2.widthPixels;
        game_grid_view.setLayoutParams(Params);

        final GameGirdViewAdapter game_adapter = new GameGirdViewAdapter(this, difficulty_level, view_height, game_map);
        game_grid_view.setAdapter(game_adapter);

        start();

        game_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                game_adapter.setSeclection(position);
                game_adapter.notifyDataSetChanged();
                TextView tv = (TextView) game_adapter.getView(position, view, null).findViewById(R.id.id_grid_item_text_002);//get the grid item.
                if (first_click == "") {
                    first_click = String.valueOf(tv.getText());
                    prev_position = position;
                }
                else
                {
                    second_click = String.valueOf(tv.getText());
                    if (first_click.equals(second_click)) {
                        if(prev_position != position) {
                            end();
                            Toast.makeText(GameGridViewActivity.this, "Bingo...!\nSCORE:"+String.valueOf(progress), Toast.LENGTH_LONG).show();
                            prev_position = -1;
                            first_click = "";
                            second_click = "";
                        }
                    }else {
                        Toast.makeText(GameGridViewActivity.this, "Yooo...!", Toast.LENGTH_SHORT).show();
                        first_click = "";
                        second_click = "";

                    }
                }
            }
        });
    }

    /**
     * 倒计时
     *
     * @author admin
     *
     */
    private class GameCountDownTimer extends CountDownTimer {

        public GameCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        // 可直接更新UI
        @Override
        public void onTick(long millisUntilFinished) {

            progress--;
            //设置进度条进度
            progress_bar.setProgress(progress);
        }

        @Override
        public void onFinish() {
            progress_bar.setProgress(0);
            Toast.makeText(GameGridViewActivity.this, "Ahoo...!",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开始
     */
    public void start() {
        long countDownInterval = 1000;  // 间隔时间
        long millisInFuture = progress_bar.getProgress() * countDownInterval;   // 时长
        gameCountDownTimer = new GameCountDownTimer(millisInFuture, countDownInterval);
        gameCountDownTimer.start();
    }

    /**
     * 结束
     */
    public void end() {
        if (gameCountDownTimer != null) {
            gameCountDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        end();
    }
/*
    //activity启动后开始计时
    @Override
    protected void onResume() {
        super.onResume();
        StartTimer();
    }

    //进入后台后计时器暂停
    @Override
    protected void onPause() {
        super.onPause();
        EndTimer();
    }

    public void StartTimer() {
        //如果timer和timerTask已经被置null了
        if (timer == null && timerTask == null) {
            //新建timer和timerTask
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    //每次progress减一
                    progress--;
                    //如果到零了，报告
                    if (progress == 0) {
                        onPause();
                        Toast.makeText(GameGridViewActivity.this, "Ahoo...!",Toast.LENGTH_SHORT).show();
                    }
                    //设置进度条进度
                    progress_bar.setProgress(progress);
                }
            };
            //开始执行timer,第一个参数是要执行的任务，
            //第二个参数是开始的延迟时间（单位毫秒）或者是Date类型的日期，代表开始执行时的系统时间
            //第三个参数是计时器两次计时之间的间隔（单位毫秒）
            timer.schedule(timerTask, 1000, 1000);
        }
    }

    public void EndTimer()
    {
        //这里很奇怪的是如果仅仅是把值赋成Null的话计时并没有停止，循环一次过后Progress就每次都加二了，循环两次过后就是加三
        //如果仅仅是cancel掉的话也不能再进行调用了
        //所以想要彻底重置timer的话需要cancel后再置null
        timer.cancel();
        timerTask.cancel();
        timer=null;
        timerTask=null;
    }*/
}
