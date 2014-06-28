/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chukylabs.bible;

import com.chukylabs.bible.BookMark.Note;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author obi
 */
public class BookMarks {

    private File source;
    private LinkedList<BookMark> hPriorityList;
    private LinkedList<BookMark> mPriorityList;
    private LinkedList<BookMark> lPriorityList;

    private BookMarks() {
        hPriorityList = new LinkedList<>();
        mPriorityList = new LinkedList<>();
        lPriorityList = new LinkedList<>();
    }

    public static BookMarks getInstance() {
        return BookMarkHolder.INSTANCE;
    }

    private static class BookMarkHolder {

        private static final BookMarks INSTANCE = new BookMarks();
    }

    public void initialize(File source) {
        this.source = source;
    }

    public void load() throws XMLStreamException, FileNotFoundException {
        if (source.exists()) {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(source));
            BookMark bm = null;
            String date = "";
            while(reader.hasNext()){
                int event = reader.next();
                if(event == XMLStreamConstants.START_ELEMENT){
                    String element = reader.getLocalName();
                    if(element.equals("bm")){
                        date = reader.getAttributeValue(0);
                    }
                    if(element.equals("vr")){
                        String n = reader.getAttributeValue(0);
                        String c = reader.getAttributeValue(1);
                        String v = reader.getAttributeValue(2);
                        String p = reader.getAttributeValue(3);
                        
                        bm = new BookMark(date, n, c, v);
                        switch(p){
                            case "HIGH":
                                bm.setPriority(BookMark.Priority.HIGH);
                                hPriorityList.add(bm);
                                break;
                            case "MEDIUM":
                                bm.setPriority(BookMark.Priority.MEDIUM);
                                mPriorityList.add(bm);
                                break;
                            case "LOW":
                                bm.setPriority(BookMark.Priority.LOW);
                                lPriorityList.add(bm);
                                break;
                        }                        
                    }
                    
                    if(element.equals("nt")){
                        String ntDate = reader.getAttributeValue(0);
                        String text = reader.getElementText();
                        bm.addNote(ntDate, text);
                    }
                }
            }
        }
    }

    public void save() throws FileNotFoundException, XMLStreamException {
        if (source.exists()) {
            XMLOutputFactory factory = XMLOutputFactory.newFactory();
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(source));
            writer.writeStartDocument();
            writer.writeStartElement("bookmarks");
            
            LinkedList<BookMark> all = new LinkedList<>();
            all.addAll(hPriorityList);
            all.addAll(mPriorityList);
            all.addAll(lPriorityList);
            
            for(BookMark bm : all){
                writer.writeStartElement("bm");
                writer.writeAttribute("d", bm.getDate());
                writer.writeStartElement("vr");
                writer.writeAttribute("n", bm.getBook());
                writer.writeAttribute("c", bm.getChapter());
                writer.writeAttribute("v", bm.getVerse());
                writer.writeAttribute("p", bm.getPriority().name());
                writer.writeEndElement();
                writer.writeStartElement("notes");
                for(Note note : bm.getNotes()){
                    writer.writeStartElement("nt");
                    writer.writeAttribute("d", note.date);
                    writer.writeCharacters(note.note);
                    writer.writeEndElement();
                }
                writer.writeEndElement();
                writer.writeEndElement();
            }
            
            writer.writeEndElement();
            writer.writeEndDocument();
            
            writer.flush();
            writer.close();
        }
    }
    
    public void attach(BookVerse verse){
        BookMark bm = find(verse);
        verse.setAttachment(bm);
    }

    public void add(BookMark b) {
        BookMark bm = find(b);
        if(bm != null){
            if(bm.getPriority() != b.getPriority()){
                switch(bm.getPriority()){
                    case HIGH:
                        hPriorityList.remove(bm);
                        break;
                    case MEDIUM:
                        mPriorityList.remove(bm);
                        break;
                    case LOW:
                        lPriorityList.remove(bm);
                        break;
                }
                b.getNotes().addAll(bm.getNotes());
            }else{
                return;
            }
            
        }
        switch (b.getPriority()) {
            case HIGH:
                hPriorityList.add(b);
                break;
            case MEDIUM:
                mPriorityList.add(b);
                break;
            case LOW:
                lPriorityList.add(b);
                break;
        }
    }

    public BookMark find(BookVerse verse){
        for(BookMark bm : hPriorityList){
            if(bm.getBook().equals(verse.getBook()) && bm.getChapter().equals(verse.getChapter()) && bm.getVerse().equals(verse.getVerse())){
                return bm;
            }
        }
        for(BookMark bm : mPriorityList){
            if(bm.getBook().equals(verse.getBook()) && bm.getChapter().equals(verse.getChapter()) && bm.getVerse().equals(verse.getVerse())){
                return bm;
            }
        }
        for(BookMark bm : lPriorityList){
            if(bm.getBook().equals(verse.getBook()) && bm.getChapter().equals(verse.getChapter()) && bm.getVerse().equals(verse.getVerse())){
                return bm;
            }
        }
        return null;
    }
    
    public BookMark find(BookMark verse){
        for(BookMark bm : hPriorityList){
            if(bm.getBook().equals(verse.getBook()) && bm.getChapter().equals(verse.getChapter()) && bm.getVerse().equals(verse.getVerse())){
                return bm;
            }
        }
        for(BookMark bm : mPriorityList){
            if(bm.getBook().equals(verse.getBook()) && bm.getChapter().equals(verse.getChapter()) && bm.getVerse().equals(verse.getVerse())){
                return bm;
            }
        }
        for(BookMark bm : lPriorityList){
            if(bm.getBook().equals(verse.getBook()) && bm.getChapter().equals(verse.getChapter()) && bm.getVerse().equals(verse.getVerse())){
                return bm;
            }
        }
        return null;
    }
    
    public File getSource() {
        return source;
    }

    public LinkedList<BookMark> getHiPriorityList() {
        return hPriorityList;
    }

    public LinkedList<BookMark> getMedPriorityList() {
        return mPriorityList;
    }

    public LinkedList<BookMark> getLoPriorityList() {
        return lPriorityList;
    }

}
