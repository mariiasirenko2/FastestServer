package com.example.fastest_server.docx;

import com.example.fastest_server.question.Question;
import com.example.fastest_server.variant.Variant;
import com.google.zxing.WriterException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class DocxService {
    @Autowired
    private DocxReader docxReader;
    @Autowired
    private DocxWriter docxWriter;
    @Autowired
    private DocxExceptionErrorHandler exceptionHandler;

    public byte[] generateBlanks(List<Variant> variantList) {
        try {
            return docxWriter.generateBlanks(variantList);
        } catch (Docx4JException e) {
            exceptionHandler.handleDocx4JException(e);
        } catch (IOException e) {
            exceptionHandler.handleIOException(e);
        } catch (WriterException e) {
            exceptionHandler.handleWriterException(e);
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return null;
    }
    public byte[] generateResults(List<Variant> variantList) {
        try {
            return docxWriter.generateResults(variantList);
        } catch (Docx4JException e) {
            exceptionHandler.handleException(e);
            return null;
        }
    }
    public byte[] generateQuestionDoc(List<Variant> variantList) {
        try {
            return docxWriter.generateQuestionDoc(variantList);
        } catch (Docx4JException e) {
            exceptionHandler.handleException(e);
            return null;
        }
    }
    public List<Question> readQuestions(InputStream questionsFile) {
        try {
            return docxReader.readQuestions(questionsFile);
        } catch (Docx4JException e) {
            exceptionHandler.handleDocx4JException(e);
        } catch (JAXBException e) {
            exceptionHandler.handleJAXBException(e);
        }
        return null;
    }
    public List<String> readStudents(InputStream file) {
        try {
            return docxReader.readStudents(file);
        } catch (Docx4JException e) {
            exceptionHandler.handleDocx4JException(e);
        } catch (JAXBException e) {
            exceptionHandler.handleJAXBException(e);
        }
        return null;
    }
}
