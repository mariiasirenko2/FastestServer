package com.example.fastest_server.question;

import com.example.fastest_server.answer.AnswerService;
import com.example.fastest_server.test.Test;
import lombok.AllArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {

    @Autowired
    private final QuestionRepository questionRepository;

    @Autowired
    private final AnswerService answerService;

    public void addQuestion(Question question) {
        questionRepository.saveAndFlush(question);
    }

}
