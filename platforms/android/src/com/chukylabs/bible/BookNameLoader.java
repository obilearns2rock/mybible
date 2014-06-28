/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chukylabs.bible;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 *
 * @author obi
 */
public class BookNameLoader {
    LinkedList<String> bookCollection;
    
    private BookNameLoader() {
    }
    
    public static BookNameLoader getInstance() {
        return BookNameLoaderHolder.INSTANCE;
    }
    
    private static class BookNameLoaderHolder {

        private static final BookNameLoader INSTANCE = new BookNameLoader();
    }
    
    public void initialize(InputStream is){
        bookCollection = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = "";
        try {
            while((line = reader.readLine()) != null){
                bookCollection.add(line.replaceAll("\\s:\\s", "_"));
            }
        } catch (IOException ex) {
            
        }
    }

    public LinkedList<String> getBookCollection() {
        return bookCollection;
    }

}
