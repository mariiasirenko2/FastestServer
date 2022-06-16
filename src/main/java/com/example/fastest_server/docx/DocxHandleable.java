package com.example.fastest_server.docx;

import com.example.fastest_server.Handleable;
import com.google.zxing.WriterException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import javax.xml.bind.JAXBException;
import java.io.IOException;

interface DocxHandleable extends Handleable {
    <T extends Docx4JException> void handleDocx4JException(T e);
    <T extends IOException> void handleIOException(T e);
    <T extends WriterException> void handleWriterException(T e);
    <T extends JAXBException> void handleJAXBException(T e);

    @Override
    default void handleException(Exception e) {
        System.out.println("An unknown error occurred while working with file");
    }
}
