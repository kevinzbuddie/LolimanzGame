package com.gamezone.loliman.lolimanzgame.gridview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.gamezone.loliman.lolimanzgame.R;

/**
 * Created by Loliman on 2018/1/10.
 */

public class GameGridViewActivity extends AppCompatActivity {

    private GridView game_grid_view;
    private int count = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_grid);

        game_grid_view = findViewById(R.id.id_game_grid_001);

        RelativeLayout.LayoutParams Params =  (RelativeLayout.LayoutParams)game_grid_view.getLayoutParams();
        Params.height = Params.width;
        game_grid_view.setLayoutParams(Params);

        game_grid_view.setNumColumns(count);
        game_grid_view.setAdapter(new GameGirdViewAdapter(this, count*count));
    }
}
