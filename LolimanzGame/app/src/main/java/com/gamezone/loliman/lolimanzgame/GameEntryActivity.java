package com.gamezone.loliman.lolimanzgame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.gamezone.loliman.lolimanzgame.gridview.GameGridViewActivity;
import com.gamezone.loliman.lolimanzgame.gridview.SerializableHashMap;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;


/**
 * A login screen that offers login via email/password.
 */
public class GameEntryActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private EditText editText_difficulty;
    private String sDifficulty;
    private Button mStartGameButton;
    private HashMap mGameMap = new HashMap();
    private String mMapTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_entry);

        mStartGameButton = findViewById(R.id.start_game_button);
        mStartGameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sDifficulty = editText_difficulty.getText().toString();
                attemptLogin();
            }
        });

        editText_difficulty = findViewById(R.id.id_difficulty);
        editText_difficulty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sDifficulty = editText_difficulty.getText().toString();
                return false;
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        if (sDifficulty.equals("")){
            Toast.makeText(GameEntryActivity.this, "请输入难度！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Integer.valueOf(sDifficulty).intValue() > 10) {
            Toast.makeText(GameEntryActivity.this, "难度不能大于10", Toast.LENGTH_SHORT).show();
            return;
        }else if (Integer.valueOf(sDifficulty).intValue() <= 1) {
            Toast.makeText(GameEntryActivity.this, "难度要大于1", Toast.LENGTH_SHORT).show();
            return;
        }

        XmlResourceParser game_map_xml = getResources().getXml(R.xml.game_map);

        if (Integer.valueOf(sDifficulty).intValue() > 1 && Integer.valueOf(sDifficulty).intValue() <= 6) {
            Random rand = new Random();
            int tag = rand.nextInt(4);
            if (tag == 0) {
                mMapTag = "butterfly";
            } else if (tag == 1) {
                mMapTag = "emotion";
            } else if (tag == 2) {
                mMapTag = "office";
            } else {
                mMapTag = "soccer";
            }
        } else if (Integer.valueOf(sDifficulty).intValue() > 6 && Integer.valueOf(sDifficulty).intValue() <= 8) {
            Random rand = new Random();
            int tag = rand.nextInt(3);
            if (tag == 0) {
                mMapTag = "emotion";
            } else if (tag == 1) {
                mMapTag = "office";
            } else {
                mMapTag = "soccer";
            }
        } else if (Integer.valueOf(sDifficulty).intValue() > 8 && (Integer.valueOf(sDifficulty).intValue() <= 10)) {
            Random rand = new Random();
            int tag = rand.nextInt(2);
            if (tag == 0) {
                mMapTag = "soccer";
            } else {
                mMapTag = "soccer";
            }
        } else if (Integer.valueOf(sDifficulty).intValue() > 10) {
            //Todo
        }

        try {
            mGameMap.clear();

            //还没有到XML文档的结尾处
            while (game_map_xml.getEventType() != XmlResourceParser.END_DOCUMENT) {
                //如果遇到了开始标签
                if (game_map_xml.getEventType() == XmlResourceParser.START_TAG) {
                    //获取该标签的标签名
                    String tagName = game_map_xml.getName();
                    if (tagName.equals(mMapTag)) {
                        //根据属性名获取属性值
                        String index = game_map_xml.getAttributeValue(null, "index");

                        //根据属性索引来获取属性值
                        game_map_xml.getAttributeName(0);

                        //获取文本节点的值
                        String picture_id = game_map_xml.nextText();
                        mGameMap.put(index, picture_id);
                    }
                }
                //获取解析器的下一个事件
                game_map_xml.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(GameEntryActivity.this, GameGridViewActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("difficulty_level", sDifficulty);

        SerializableHashMap intent_map = new SerializableHashMap();
        intent_map.setMap(mGameMap);
        bundle.putSerializable("game_map", intent_map);

        intent.putExtra("game_bundle", bundle);
        startActivity(intent);
    }
}

