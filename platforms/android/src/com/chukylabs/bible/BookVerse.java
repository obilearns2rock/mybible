/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chukylabs.bible;

/**
 *
 * @author obi
 */
public class BookVerse {
    private final String verse, chapter, book, content;
    private Object attachment;
    
    public BookVerse(String verse, String chapter, String book, String content){
        this.verse = verse;
        this.chapter = chapter;
        this.book = book;
        this.content = content;
    }

    public String getVerse() {
        return verse;
    }

    public String getChapter() {
        return chapter;
    }

    public String getBook() {
        return book;
    }

    public String getContent() {
        return content;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", verse, content);
    }
    
}
