package com.example.fastest_server.test;

import com.example.fastest_server.question.DocxReader;
import com.example.fastest_server.question.Question;
import com.example.fastest_server.user.User;
import lombok.AllArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public void addTest(Test test) {
        testRepository.saveAndFlush(test);
    }

    public Test getTestById(int id) {
        return testRepository.getById(id);
    }

    public List<Test> getTestByOwner(User owner) {
        return testRepository.findByOwner(owner);
    }

    public List<Question> readQuestions(MultipartFile questionsMultipartFile) throws JAXBException, Docx4JException, IOException {
        DocxReader docxReader = new DocxReader();
        File questionsFile = new File("testQuestion.docx");
        try (OutputStream os = new FileOutputStream(questionsFile)) {
            os.write(questionsMultipartFile.getBytes());
        }
        docxReader.readQuestions(questionsFile);
        questionsFile.delete();
        return docxReader.getQuestionList();
    }

    public List<String> readStudents(MultipartFile studentsMultipartFile) throws JAXBException, Docx4JException, IOException {
        DocxReader docxReader = new DocxReader();
        File studentsFile = new File("testStudent.docx");
        try (OutputStream os = new FileOutputStream(studentsFile)) {
            os.write(studentsMultipartFile.getBytes());
        }
        docxReader.readStudents(studentsFile);
        studentsFile.delete();
        return docxReader.getStudentList();
    }




}
