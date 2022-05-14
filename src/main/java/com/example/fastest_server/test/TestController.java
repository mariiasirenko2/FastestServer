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
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/profile/{idUser}/GetTests")
    public List<Test> getTests(@PathVariable(value = "idUser") int idUser) {
        return testService.getTestByOwner(userService.getUserById(idUser));

    }



    /*@PostMapping("/profile/{idUser}/GenerateTest") TODO: uncomment to work with file names instead of files
    public void generateTest(@RequestParam(value = "testName") String testName,
                             @RequestParam(value = "questionFile") String questionFile,
                             //  @RequestParam(value = "studentFile") String studentFile,
                             @PathVariable(value = "idUser") int idUser) throws JAXBException, Docx4JException {
        Test test = new Test(testName, questionFile, userService.getUserById(idUser));
        test.setQuestions(questionService.readQuestions(questionFile));
        test.setQuestionsKeys();
        testService.addTest(test);
    }*/

    @PostMapping("/profile/{idUser}/GenerateTest")
    public void generateTest(@RequestParam(value = "testName") String testName,
                             @RequestParam(value = "questionFile") File questionFile,
                             //  @RequestParam(value = "studentFile") String studentFile,
                             @PathVariable(value = "idUser") int idUser) throws JAXBException, Docx4JException {
        Test test = new Test(testName, "test.docx", userService.getUserById(idUser));
        test.setQuestions(questionService.readQuestions(questionFile));
        test.setQuestionsKeys();
        testService.addTest(test);
    }
}
