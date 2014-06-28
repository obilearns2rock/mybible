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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author obi
 */
public class BookSearch {

    String source;
    String prevBookName;
    LinkedHashMap<String, LinkedList<BookVerse>> prevResult;
    XMLInputFactory factory;

    private BookSearch() {
        factory = XMLInputFactory.newFactory();
        prevBookName = "";
    }

    public static BookSearch getInstance() {
        return BookSearchHolder.INSTANCE;
    }

    private static class BookSearchHolder {

        private static final BookSearch INSTANCE = new BookSearch();
    }

    public void initialize(String source) {
        this.source = source;
    }

    /**
     * Note: this method should be called in a thread as it may take more time
     * to return
     *
     * @param bookName The name of the book
     * @param searchTerm The text to search
     * @return LinkedHashMap
     * The chapter is the key and
     * the verses are in the Linked list
     */
    public LinkedHashMap<String, LinkedList<BookVerse>> searchBook(String bookName, String searchTerm) {
        if (prevBookName.equalsIgnoreCase(bookName)) {
            return prevResult;
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
                        
                        Pattern pattern = Pattern.compile("[\\s\\p{Punct}[^\\w]]*" + searchTerm + "[\\s\\p{Punct}]+", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(content);
                        
                        if (matcher.find()) {
                            LinkedList<BookVerse> verses = result.get(startElement);
                            verses.add(new BookVerse(String.format("%03d", verses.size() + 1), startElement, bookName, content));
                        }
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
        prevResult = result;
        return result;
    }
}
