package com.example.fastest_server.test;


import com.example.fastest_server.answer.AnswerService;
import com.example.fastest_server.answer.Chars;
import com.example.fastest_server.photo.PhotoService;
import com.example.fastest_server.question.Question;
import com.example.fastest_server.question.QuestionService;
import com.example.fastest_server.user.UserService;
import com.example.fastest_server.variant.Variant;
import com.example.fastest_server.variant.VariantService;
import com.example.fastest_server.variantquestion.VariantQuestionService;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.ws.rs.Produces;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/fastest")
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
  @Autowired
  PhotoService photoService;


  @GetMapping("/profile/{idUser}/GetTests")
  public List<TestNameId> getTests(@PathVariable(value = "idUser") Long idUser) {
    return testService.getTestByOwner(userService.getUserById(idUser));

  }

  @GetMapping("/profile/{idUser}/Tests/{idTest}/V")
  public List<Variant> getVariantsOfTest(@PathVariable(value = "idUser") Long idUser,
      @PathVariable(value = "idTest") Long idTest) {
    return variantService.getVariantsList(idTest);
  }

  @PostMapping("/profile/{idUser}/GenerateTest")
  public Collection<Question> generateTest(@RequestParam(value = "testName") String testName,
      @RequestParam(value = "questionFile") MultipartFile questionMultipartFile,
      @RequestParam(value = "studentFile") MultipartFile studentMultipartFile,
      @PathVariable(value = "idUser") Long idUser)
      throws JAXBException, Docx4JException, IOException {
    Test test = new Test("Еуые", userService.getUserById(idUser));
    test.setQuestions(testService.readQuestions(questionMultipartFile));
    test.setQuestionsKeys();
    test.setStudents(testService.readStudents(studentMultipartFile));
    testService.addTest(test);
    testService.generateVariants(test);
    return test.getQuestions();
  }

  @GetMapping("/profile/{idUser}/Variant/{idVariant}/T")
  public List<Chars> getAnswer(@PathVariable(value = "idUser") Long idUser,
      @PathVariable(value = "idVariant") int idVariant) {
    return variantQuestionService.getAnswers(idVariant);
  }

  @PostMapping("/profile/{idUser}/Variant/{idVariant}")
  public void setMarkToVariant(@PathVariable(value = "idUser") Long idUser,
      @PathVariable(value = "idVariant") Long idVariant,
      @RequestBody Integer mark) {
    variantService.setMarkToVariant(idVariant, mark);
  }

  @GetMapping("/profile/{idUser}/Tests/{idTest}/Documents")
  public ResponseEntity<?> getVariants(@PathVariable(value = "idUser") Long idUser,
      @PathVariable(value = "idTest") Long idTest)
      throws Docx4JException, JAXBException, IOException {
    byte[] data = testService.generateVariantsFile(idTest);
    InputStream inputStream = new ByteArrayInputStream(data);

    File file = new File("Variants.docx");
    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document")).body(data);
  }

  @GetMapping("/profile/{idUser}/Tests/{idTest}/Blanks")
  public ResponseEntity<?> getBlanks(@PathVariable(value = "idUser") Long idUser,
      @PathVariable(value = "idTest") Long idTest) throws Exception {

    byte[] data = testService.generateBlanks(idTest);
    InputStream inputStream = new ByteArrayInputStream(data);

    File file = new File("Blanks.docx");
    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document")).body(data);

  }

  @GetMapping("/profile/{idUser}/Tests/{idTest}/Results")
  public @ResponseBody byte[] getResults(@PathVariable(value = "idUser") Long idUser,
      @PathVariable(value = "idTest") Long idTest) throws Exception {
    return testService.getResults(idTest);
  }

  @GetMapping("/profile/process")
  public ResponseEntity<?> processBlank(
      @RequestParam(value = "questionFile") MultipartFile blankPhoto) throws Exception {

    byte[] processed = photoService.checkPhoto(blankPhoto);

    InputStream inputStream = new ByteArrayInputStream(processed);

    File file = new File("Blank.jpeg");
    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(
        "image/jpeg")).body(processed);

  }


}
