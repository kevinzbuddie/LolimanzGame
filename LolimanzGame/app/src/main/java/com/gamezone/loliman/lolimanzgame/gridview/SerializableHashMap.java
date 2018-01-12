package com.gamezone.loliman.lolimanzgame.gridview;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Loliman on 2018/1/11.
 */

public class SerializableHashMap implements Serializable{

    private HashMap<String,Object> map;

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(HashMap map) {
        this.map = map;
    }
}
