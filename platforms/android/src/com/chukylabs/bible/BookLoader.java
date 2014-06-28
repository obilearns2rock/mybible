/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chukylabs.bible;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author obi
 */
public class BookLoader {

    String source;
    String prevBookName;
    LinkedHashMap<String, LinkedList<BookVerse>> prevBook;
    XMLInputFactory factory;

    private BookLoader() {
        factory = XMLInputFactory.newFactory();
        prevBookName = "";
    }

    public static BookLoader getInstance() {
        return BookLoaderHolder.INSTANCE;
    }

    private static class BookLoaderHolder {

        private static final BookLoader INSTANCE = new BookLoader();
    }

    public void initialize(String source) {
        this.source = source;
    }

    /**
     * Note: this method should be called in a thread as it may take more time
     * to return
     *
     * @param bookName The name of the book
     * @return LinkedHashMap
     * The chapter is the key and
     * the verses are the Linked list
     */
    public LinkedHashMap<String, LinkedList<BookVerse>> getBook(String bookName) {
        if (prevBookName.equalsIgnoreCase(bookName)) {
            return prevBook;
        }
        LinkedHashMap<String, LinkedList<BookVerse>> result = new LinkedHashMap<>();
        Path p = Paths.get(source, "/" + bookName + ".xml");
        InputStream is = null;
//        System.out.println(p + " exists:" + Files.exists(p));
        try {
            is = Files.newInputStream(p, StandardOpenOption.READ);
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    String startElement = reader.getLocalName();
                    if (!result.containsKey(startElement) && startElement.matches("ch\\d{3}")) {
                        result.put(startElement, new LinkedList<BookVerse>());
                    }
                    if (startElement.matches("ch\\d{3}")) {
//                        reader.next();
                        String content = reader.getElementText();
                        LinkedList<BookVerse> verses = result.get(startElement);
                        verses.add(new BookVerse(String.format("%03d", verses.size() + 1), startElement, bookName, content));
                    }
                }
            }
        } catch (IOException | XMLStreamException ex) {
            
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {

            }
        }
        prevBookName = bookName;
        prevBook = result;
        return result;
    }

    public String getSource() {
        return source;
    }
    
}
