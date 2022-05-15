package com.example.fastest_server.question;

import com.example.fastest_server.test.Test;
import lombok.AllArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    /*public List<Question> readQuestions(File file) throws JAXBException, Docx4JException {
        QuestionReader questionReader = new QuestionReader(file);
        return questionReader.getQuestionList();

    }*/


    public void addQuestion(Question question) {
        questionRepository.saveAndFlush(question);
    }

}
