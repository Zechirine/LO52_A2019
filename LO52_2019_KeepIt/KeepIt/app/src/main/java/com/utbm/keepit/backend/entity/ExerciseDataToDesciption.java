package com.utbm.keepit.backend.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseDataToDesciption {
    //  private int typePublic; // 0/1/2  -->  adulte、jeune、les deux 适合成年人 未成年人 老人...
    //    private int levelDifficult;  // 动作难度
    //    private int levelGroup; // 0 1 2  运动的人的等级   入门  业余 专业运动员
    public static Map<Integer,String> descripPublic = new HashMap<Integer, String>();
    public static Map<Integer,String> descripDifficult = new HashMap<Integer, String>();
    public static Map<Integer,String> descripGroup = new HashMap<Integer, String>();

    static {
        descripDifficult.put(0,"très faclie");
        descripDifficult.put(1,"faclie");
        descripDifficult.put(2,"normal");
        descripDifficult.put(3,"difficile");
        descripDifficult.put(4,"très difficile");
        descripDifficult.put(5,"impossible");

        descripGroup.put(0,"Ne jamais bouge");
        descripGroup.put(1,"Débutant");
        descripGroup.put(2,"Amateur");
        descripGroup.put(3,"Professionnel");
        descripGroup.put(4,"Haut niveau sportif");

        descripPublic.put(0,"Enfant");
        descripPublic.put(1,"Mineur");
        descripPublic.put(2,"Adulte");
        descripPublic.put(3,"Vielle");}

    public static int getKey(Map map, Object value){
        for(Object key: map.keySet()){
            if(map.get(key).equals(value)){
                return Integer.parseInt(key.toString());
            }
        }
        return -1;
    }
}
