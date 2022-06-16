package com.example.fastest_server.docx;

import com.example.fastest_server.Handleable;
import com.google.zxing.WriterException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
class DocxExceptionErrorHandler implements DocxHandleable {

    public void handleDocx4JException(Docx4JException e) {
        System.out.println("An error occurred while creating, reading or saving the file");
    }

    public void handleIOException(IOException e) {
        System.out.println("An error occurred while working with input or output streams");
    }

    public void handleWriterException(WriterException e) {
        System.out.println("An error occurred while trying to encode data into the QR-code");
    }

    public void handleJAXBException(JAXBException e) {
        System.out.println("An error occurred while working with XML file structure");
    }

}
