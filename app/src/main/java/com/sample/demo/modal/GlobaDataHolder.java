package com.sample.demo.modal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobaDataHolder {

    private static GlobaDataHolder globaDataHolder;

    public static GlobaDataHolder getGlobaDataHolder() {

        if (null == globaDataHolder) {
            globaDataHolder = new GlobaDataHolder();
        }
        return globaDataHolder;
    }


    private ConcurrentHashMap<String, ArrayList<Books>> productMap = new ConcurrentHashMap<String, ArrayList<Books>>();

    private List<Books> shoppingList = Collections.synchronizedList(new ArrayList<Books>());

    public List<Books> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ArrayList<Books> getShoppingList) {
        this.shoppingList = getShoppingList;
    }

    public Map<String, ArrayList<Books>> getProductMap() {
        return productMap;
    }


}
