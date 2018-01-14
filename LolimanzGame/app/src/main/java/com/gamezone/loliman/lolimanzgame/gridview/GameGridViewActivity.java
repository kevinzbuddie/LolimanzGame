package com.gamezone.loliman.lolimanzgame.gridview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gamezone.loliman.lolimanzgame.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.gamezone.loliman.lolimanzgame.GameEntryActivity.ReWriteFile;
import static com.gamezone.loliman.lolimanzgame.GameEntryActivity.ReadFile;
import static com.gamezone.loliman.lolimanzgame.GameEntryActivity.mColumn;


/**
 * Created by Loliman on 2018/1/10.
 */

public class GameGridViewActivity extends AppCompatActivity {

    private GridView game_grid_view;
    private int difficulty_level = 0;
    private SerializableHashMap game_map;
    private int view_height = 0;
    private String first_click = "";
    private String second_click = "";
    private int prev_position = -1;
    private ProgressBar progress_bar;
    private int progress;
    private Timer timer;
    private TimerTask timerTask;
    private GameCountDownTimer gameCountDownTimer;
    private int resultCode = 1980; //for result intent!

    private Button redo_button;
    private Button next_button;

    private String sDifficulty="";
    private String isDone="";
    private int stars=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_grid);
        game_grid_view = findViewById(R.id.id_game_grid_001);
        progress_bar = findViewById(R.id.id_game_progress_004);
        progress = progress_bar.getProgress();

        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("game_bundle");
        sDifficulty = bundle.getString("difficulty_level");
        game_map = (SerializableHashMap) bundle.getSerializable("game_map");


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
                            next_button.setEnabled(true);//can enter the next set.

                            //todo modify the set data file.
                            isDone = "done";
                            if (progress >= 40) {
                                stars = 3;
                            }else if (progress >= 20){
                                stars = 2;
                            } else {
                                stars = 1;
                            }
                            // sendResultBack();
                            update_set_data_file();

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

        redo_button = findViewById(R.id.id_redo_button_005);
        next_button = findViewById(R.id.id_next_button_006);

        redo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(GameGridViewActivity.this, GameGridViewActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("difficulty_level", sDifficulty);
                bundle.putSerializable("game_map", game_map);
                intent.putExtra("game_bundle", bundle);

                startActivity(intent);
            }
        });
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Integer.valueOf(sDifficulty) == 10) {
                    Toast.makeText(GameGridViewActivity.this, "next set", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    finish();
                    Intent intent = new Intent(GameGridViewActivity.this, GameGridViewActivity.class);

                    Bundle bundle = new Bundle();
                    sDifficulty = String.valueOf(Integer.valueOf(sDifficulty) + 1);
                    bundle.putString("difficulty_level", sDifficulty);
                    bundle.putSerializable("game_map", game_map);
                    intent.putExtra("game_bundle", bundle);

                    startActivity(intent);
                }
            }
        });
    }

    public void sendResultBack(){
        //for result intent!
        Intent mIntent = new Intent();

        mIntent.putExtra("whichSet", sDifficulty);
        mIntent.putExtra("isDone", isDone);

        // 设置结果，并进行传送
        this.setResult(Activity.RESULT_OK, mIntent);
    }

    public void update_set_data_file(){

        int game_set_data[][] = new int[mColumn*mColumn][2];//two value for each set
        String sDataRead = null;
        try {
            sDataRead = ReadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert sDataRead != null;
        String[] s = sDataRead.split("\n");
        for (int i = 0; i<mColumn*mColumn; i++){
            String[] ss = s[i].split(",");
            game_set_data[i][0] = Integer.valueOf(ss[0]);
            game_set_data[i][1] = Integer.valueOf(ss[1]);
        }

        game_set_data[Integer.valueOf(sDifficulty)-2][0] = 2; //2 means that this set had been done!
        game_set_data[Integer.valueOf(sDifficulty)-2][1] = stars; //
        String sWriteData = "";
        for (int i = 0; i<mColumn*mColumn; i++){
            sWriteData = sWriteData+String.valueOf(game_set_data[i][0])+","+String.valueOf(game_set_data[i][1]+"\n");
        }
        try {
            ReWriteFile(sWriteData);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
