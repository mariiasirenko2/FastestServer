package com.example.fastest_server.test;


import com.example.fastest_server.answer.AnswerService;
import com.example.fastest_server.question.DocxReader;
import com.example.fastest_server.question.Question;
import com.example.fastest_server.question.QuestionService;
import com.example.fastest_server.user.User;
import com.example.fastest_server.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
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


    @GetMapping("/profile/{idUser}/GetTests")
    public List<Test> getTests(@PathVariable(value = "idUser") int idUser) {
        return testService.getTestByOwner(userService.getUserById(idUser));

    }



    @PostMapping("/profile/{idUser}/GenerateTest")
    public void generateTest(@RequestParam(value = "testName") String testName,
                             @RequestParam(value = "questionFile") MultipartFile questionMultipartFile,
                             @RequestParam(value = "studentFile") MultipartFile studentMultipartFile,
                             @PathVariable(value = "idUser") int idUser) throws JAXBException, Docx4JException, IOException {
        Test test = new Test(testName, userService.getUserById(idUser));
        test.setQuestions(testService.readQuestions(questionMultipartFile));
        test.setQuestionsKeys();
        test.setStudents(testService.readStudents(studentMultipartFile));
        testService.addTest(test);
    }

    @PostMapping("/profile/{idUser}/Tests/{idTest}/Variants")
    public List<Question> getTestQuestions(@PathVariable(value = "idTest") int idTest) throws Docx4JException, JAXBException {
        Test test = testService.getTestById(idTest);
        testService.generateVariants(test);
        //DocxReader docxReader = new DocxReader();
       // docxReader.generateVariants((List) test.getQuestions(), test.getStudents());
        return (List) test.getQuestions();

    }



}
