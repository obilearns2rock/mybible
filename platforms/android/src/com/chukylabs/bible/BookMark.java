/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chukylabs.bible;

import java.util.LinkedList;

/**
 *
 * @author obi
 */
public class BookMark {
    private final String book, chapter, verse;
    private final String date;
    private Priority priority;
    
    private LinkedList<Note> notes;
    
    public BookMark(String date, String book, String chapter, String verse){
        this.date = date;
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
        this.priority = Priority.MEDIUM;
    }
    
    public static enum Priority{
        HIGH, MEDIUM, LOW
    }
    
    public class Note{
        String date;
        String note;
        
        public Note(String date, String note){
            this.date = date;
            this.note = note;
        }
    }
    
    public void addNote(String date, String note){
        if(notes == null){
            notes = new LinkedList<>();
        }
        notes.add(new Note(date, note));
    }

    public String getBook() {
        return book;
    }

    public String getChapter() {
        return chapter;
    }

    public String getVerse() {
        return verse;
    }
    
    public String getDate() {
        return date;
    }

    public LinkedList<Note> getNotes() {
        return notes;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public String getXML(){
        StringBuilder notesXml = new StringBuilder();
        for(Note note : notes){
            String xml = String.format(""
                    + "<nt d=\"%s\">"
                    + "%s"
                    + "</nt>", note.date, note.note);
            notesXml.append(xml);
        }
        String xml = String.format(""
                + "<bm d=\"%s\">"
                + "<vr n=\"%s\" c=\"%s\" v=\"%s\" p=\"%s\">"
                + "</vr>"
                + "<notes>"
                + "%s"
                + "</notes>"
                + "</bm>",getDate(), getBook(), getChapter(), getVerse(), priority,notesXml.toString());
        return xml;
    }

    @Override
    public String toString() {
        return getXML();
    }
    
}
