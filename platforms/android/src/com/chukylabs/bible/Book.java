/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chukylabs.bible;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author obi
 */
public class Book {
    private final String name;
    private ArrayList<String> chapterList;
    private LinkedHashMap<String, LinkedList<BookVerse>> chapters;
    
    public Book(String name){
        this.name = name;
    }

    public LinkedHashMap<String, LinkedList<BookVerse>> getChapters() {
        return chapters;
    }

    public void setChapters(LinkedHashMap<String, LinkedList<BookVerse>> chapters) {
        this.chapters = chapters;
        this.chapterList = new ArrayList<>(chapters.keySet());
    }        

    public String getName() {
        return name;
    }

    public ArrayList<String> getChapterList() {
        return chapterList;
    }   
}
