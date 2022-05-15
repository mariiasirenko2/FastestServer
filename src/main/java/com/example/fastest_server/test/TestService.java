package com.example.fastest_server.test;

import com.example.fastest_server.question.DocxReader;
import com.example.fastest_server.question.Question;
import com.example.fastest_server.user.User;
import lombok.AllArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
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

    public List<Question> readQuestions(String questionsFile) throws JAXBException, Docx4JException {
        DocxReader docxReader = new DocxReader();
        return docxReader.readQuestions(questionsFile);
    }

    public List<String> readStudents(String studentsFile) throws JAXBException, Docx4JException {
        DocxReader docxReader = new DocxReader();
        return docxReader.readStudents(studentsFile);
    }




}
