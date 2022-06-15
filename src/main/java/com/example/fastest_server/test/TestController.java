package com.example.fastest_server.test;


import com.example.fastest_server.answer.AnswerService;
import com.example.fastest_server.answer.Chars;
import com.example.fastest_server.question.DocxReader;
import com.example.fastest_server.question.Question;
import com.example.fastest_server.question.QuestionService;
import com.example.fastest_server.user.User;
import com.example.fastest_server.user.UserService;
import com.example.fastest_server.variant.Variant;
import com.example.fastest_server.variant.VariantService;
import com.example.fastest_server.variantquestion.VariantQuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    VariantService variantService;
    @Autowired
    VariantQuestionService variantQuestionService;
    @Autowired
    UserService userService;

    @Autowired
    AnswerService answerService;


    @GetMapping("/profile/{idUser}/GetTests")
    public List<TestNameId> getTests(@PathVariable(value = "idUser") int idUser) {
        return testService.getTestByOwner(userService.getUserById(idUser));

    }

    @GetMapping("/profile/{idUser}/Tests/{idTest}/V")
    public List<Variant> getVariantsOfTest(@PathVariable(value = "idUser") int idUser,
                                     @PathVariable(value = "idTest") int idTest){
        return variantService.getVariantsList(idTest);
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
        testService.generateVariants(test);

    }

    //WORk
    @GetMapping("/profile/{idUser}/Variant/{idVariant}/T")
    public List<Chars> getAnswer(@PathVariable(value = "idUser") int idUser,
                                 @PathVariable(value = "idVariant") int idVariant){
        return variantQuestionService.getAnswers(idVariant);
    }

    @PostMapping("/profile/{idUser}/Variant/{idVariant}")
    public void setMarkToVariant(@PathVariable(value = "idUser") int idUser,
                                 @PathVariable(value = "idVariant") int idVariant,
                                 @RequestBody int mark){
        variantService.setMarkToVariant(idVariant,mark);
    }

    @GetMapping("/profile/{idUser}/Tests/{idTest}/Documents")
    public @ResponseBody byte[] getVariants(@PathVariable(value = "idUser")int idUser,
                                            @PathVariable(value = "idTest") int idTest) throws Docx4JException, JAXBException {
        return testService.generateVariantsFile(idTest);
    }

    @GetMapping("/profile/{idUser}/Tests/{idTest}/Blanks")
    public @ResponseBody byte[] getBlanks(@PathVariable(value = "idUser")int idUser,
                                 @PathVariable(value = "idTest") int idTest) throws Exception {
        return testService.generateBlanks(idTest);
    }

    @GetMapping("/profile/{idUser}/Tests/{idTest}/Results")
    public @ResponseBody byte[] getResults(@PathVariable(value = "idUser")int idUser,
                                          @PathVariable(value = "idTest") int idTest) throws Exception {
        return testService.getResults(idTest);
    }





}
