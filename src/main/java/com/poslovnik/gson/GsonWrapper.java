/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poslovnik.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poslovnik.model.data.Payout;

/**
 *
 * @author mixa
 */
public class GsonWrapper {
    private static Gson instance;
    
    public static Gson getGson() {
        if (instance == null) {
            instance = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(Payout.class, new PayoutSerializer())
                    .create();
        }
        
        return instance;
    }
}
