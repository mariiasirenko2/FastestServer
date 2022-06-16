package com.example.fastest_server.test;

import com.example.fastest_server.answer.Answer;
import com.example.fastest_server.answer.Chars;
import com.example.fastest_server.docx.DocxService;
import com.example.fastest_server.question.Question;
import com.example.fastest_server.user.User;
import com.example.fastest_server.variant.Variant;
import com.example.fastest_server.variant.VariantRepository;
import com.example.fastest_server.variantquestion.VariantQuestion;
import com.example.fastest_server.variantquestion.VariantQuestionRepository;
import lombok.AllArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;

@Service
@AllArgsConstructor
public class TestService {

    @Autowired
    private final TestRepository testRepository;

    @Autowired
    private final VariantQuestionRepository variantQuestionRepository;

    @Autowired
    private final VariantRepository variantRepository;

    @Autowired
    private DocxService docxService;
    public void addTest(Test test) {
        testRepository.saveAndFlush(test);
    }

    public void generateVariants(Test test) {
        List<Question> questionList = (List) test.getQuestions();

        List<String> studentList = new ArrayList<>(Arrays.asList(test.getStudents()));
        for (String student: studentList) {
            Variant variant = new Variant(student);
            variant = variantRepository.saveAndFlush(variant);
            Set<Question> questionSet = new HashSet<>(20);
            while (questionSet.size() < 20) {
                questionSet.add(questionList.get((int) (Math.random() * (questionList.size() - 1))));
            }
            int i = 1;
            for (Question question: questionSet) {
                VariantQuestion variantQuestion = new VariantQuestion(variant, question);
                variantQuestion.setQuestionNumber(i++);
                Chars[] chars = Chars.values();
                List<Answer> answerList = (List) question.getAnswers();
                variantQuestion.setLetterAnswer(chars[answerList.indexOf(new Answer(true)) + 1]);
                System.out.println(chars[answerList.indexOf(new Answer(true)) + 1]);
                variantQuestionRepository.saveAndFlush(variantQuestion);
            }
        }
    }

    public Test getTestById(int id) {
        return testRepository.getById(id);
    }

    public List<TestNameId> getTestByOwner(User owner) {
        List<Test> testList = testRepository.findByOwner(owner);
        List<TestNameId> tni= new ArrayList<>();
        for(Test test: testList){
            tni.add(new TestNameId(test.getTestName(), test.getId()));
        }
        return tni;
    }

    public List<Question> readQuestions(MultipartFile questionsMultipartFile) throws IOException {
        InputStream questionsFile = questionsMultipartFile.getInputStream();
        return docxService.readQuestions(questionsFile);
    }

    //TODO: change file to the input stream
    public List<String> readStudents(MultipartFile studentsMultipartFile) throws IOException {
        InputStream studentsFile = studentsMultipartFile.getInputStream();
        return docxService.readStudents(studentsFile);
    }


    public byte[] generateVariantsFile(Long idTest)  {
        List<Variant> variantList = variantRepository.findByTestId(idTest);
        return  docxService.generateQuestionDoc(variantList);

    }


    public  byte[] generateBlanks(Long idTest) throws Exception {
        List<Variant> variantList = variantRepository.findByTestId(idTest);
        return docxService.generateBlanks(variantList);
    }

    public byte[] getResults(Long idTest) throws Exception {
        List<Variant> variantList = variantRepository.findByTestId(idTest);
        return docxService.generateResults(variantList);
    }
}
