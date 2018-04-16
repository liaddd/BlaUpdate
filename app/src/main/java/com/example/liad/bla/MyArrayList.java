package com.example.liad.bla;

import java.util.ArrayList;



public class MyArrayList<E> extends ArrayList {

    private static MyArrayList instance = null;

    public static MyArrayList getInstance() {
        if(instance == null) {
            instance = new MyArrayList();
        }
        return instance;
    }
}
