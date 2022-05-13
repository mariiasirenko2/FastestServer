package com.example.fastest_server.test;


import com.example.fastest_server.answer.AnswerService;
import com.example.fastest_server.question.Question;
import com.example.fastest_server.question.QuestionService;
import com.example.fastest_server.user.User;
import com.example.fastest_server.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Iterator;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path="/fastest")
@Slf4j
public class TestController {
    @Autowired
    TestService testService;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    AnswerService answerService;


    @PostMapping("/GenerateTest")
    public void generateTest(String testName, File file) throws JAXBException, Docx4JException {
        Test test = new Test(testName, "file_location");
        test.setQuestions(questionService.readQuestions(file));
        test.setQuestionsKeys();
        testService.addTest(test);
    }

    /*
    @PostMapping("/GenerateTest_old")
    public void generateTest(String testName, String fileLocation) throws JAXBException, Docx4JException {
        Test test = new Test(testName, fileLocation);
        test.setQuestions(questionService.readQuestions(fileLocation));
        test.setQuestionsKeys();
        testService.addTest(test);
    }*/
}
