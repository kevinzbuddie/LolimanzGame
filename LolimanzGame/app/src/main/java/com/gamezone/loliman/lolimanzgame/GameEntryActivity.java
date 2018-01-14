package com.gamezone.loliman.lolimanzgame;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gamezone.loliman.lolimanzgame.gridview.GameGridViewActivity;
import com.gamezone.loliman.lolimanzgame.gridview.GameSetEntriesAdapter;
import com.gamezone.loliman.lolimanzgame.gridview.SerializableHashMap;
import com.gamezone.loliman.lolimanzgame.fileOperations.FileOperations;

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
    private GridView game_set_entries_view;
    private EditText editText_difficulty;
    private String sDifficulty;
    private Button mStartGameButton;
    private int view_height;
    public static int mColumn = 4;
    private int game_set_data[][] = new int[mColumn*mColumn][2];//two value for each set
    private HashMap mGameMap = new HashMap();
    private String mMapTag;
    private int requestCode = 1980; //for result intent!

    public static FileOperations game_set_data_file = new FileOperations("lolimanzGame", "game_set_data.dat");

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_entry);

        game_set_entries_view = findViewById(R.id.id_game_set_entries);
        game_set_entries_view.setNumColumns(mColumn);

        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams)game_set_entries_view.getLayoutParams();
        Params.height = dm2.widthPixels;
        view_height = dm2.widthPixels;
        game_set_entries_view.setLayoutParams(Params);

        verifyStoragePermissions(this);

        //initialize the data file
        if (!game_set_data_file.FileIsExist()){
            try {
                game_set_data_file.CreateNewFile();
                game_set_data_file.WriteFile("1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n1,0\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String sDataRead = null;
        try {
            sDataRead = game_set_data_file.ReadFile();
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

        final GameSetEntriesAdapter game_adapter = new GameSetEntriesAdapter(this, mColumn, view_height, game_set_data);
        game_set_entries_view.setAdapter(game_adapter);

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

        game_set_entries_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                game_adapter.setSeclection(position);
                game_adapter.notifyDataSetChanged();
                if ( position ==0 || game_set_data[position][0] != 1 ) {
                    sDifficulty = String.valueOf(position + 2);
                    attemptLogin();
                }
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,"android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        if (Integer.valueOf(sDifficulty) > 10) {
            Toast.makeText(GameEntryActivity.this, "未完工，敬请期待...", Toast.LENGTH_SHORT).show();
            return;
        }else if (Integer.valueOf(sDifficulty) <= 1) {
            Toast.makeText(GameEntryActivity.this, "未完工，敬请期待...", Toast.LENGTH_SHORT).show();
            return;
        }

        XmlResourceParser game_map_xml = getResources().getXml(R.xml.game_map);

        if (Integer.valueOf(sDifficulty)> 1 && Integer.valueOf(sDifficulty) <= 6) {
            Random rand = new Random();
            int tag = rand.nextInt(4);
            if (tag == 0) {
                mMapTag = "soccer";//"butterfly";
            } else if (tag == 1) {
                mMapTag = "soccer";//"emotion";
            } else if (tag == 2) {
                mMapTag = "soccer";//"office";
            } else {
                mMapTag = "soccer";
            }
        } else if (Integer.valueOf(sDifficulty)> 6 && Integer.valueOf(sDifficulty) <= 8) {
            Random rand = new Random();
            int tag = rand.nextInt(3);
            if (tag == 0) {
                mMapTag = "soccer";//"emotion";
            } else if (tag == 1) {
                mMapTag = "soccer";//"office";
            } else {
                mMapTag = "soccer";
            }
        } else if (Integer.valueOf(sDifficulty) > 8 && (Integer.valueOf(sDifficulty) <= 10)) {
            Random rand = new Random();
            int tag = rand.nextInt(2);
            if (tag == 0) {
                mMapTag = "soccer";
            } else {
                mMapTag = "soccer";
            }
        } else if (Integer.valueOf(sDifficulty) > 10) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        Intent intent = new Intent(GameEntryActivity.this, GameGridViewActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("difficulty_level", sDifficulty);

        SerializableHashMap intent_map = new SerializableHashMap();
        intent_map.setMap(mGameMap);
        bundle.putSerializable("game_map", intent_map);

        intent.putExtra("game_bundle", bundle);
        startActivityForResult(intent, requestCode);//for result intent!
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case 1980:
                String sDataRead = null;
                try {
                    sDataRead = game_set_data_file.ReadFile();
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

                final GameSetEntriesAdapter game_adapter = new GameSetEntriesAdapter(this, mColumn, view_height, game_set_data);
                game_set_entries_view.setAdapter(game_adapter);

                game_set_entries_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        game_adapter.setSeclection(position);
                        game_adapter.notifyDataSetChanged();
                        if ( position ==0 || game_set_data[position][0] != 1 ) {
                            sDifficulty = String.valueOf(position + 2);
                            attemptLogin();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}

