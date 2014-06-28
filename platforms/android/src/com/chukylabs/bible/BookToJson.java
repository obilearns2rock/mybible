/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chukylabs.bible;

import com.chukylabs.bible.Book;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author obi
 */
public class BookToJson {
    private static GsonBuilder b;
    public static void init(boolean pretty){  
        b = new GsonBuilder();
        if(pretty){
            b.setPrettyPrinting();
        }
        b.disableHtmlEscaping();
        b.setExclusionStrategies(new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if (f.getName().equalsIgnoreCase("chapter")) {
                    return true;
                }
                if (f.getName().equalsIgnoreCase("book")) {
                    return true;
                }
                if (f.getName().equalsIgnoreCase("verse")) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
    }
    
    public static String convert(Book book){
        if(b == null){
            init(true);
        }
        Gson json = b.create();
        return json.toJson(book);
    }
}
