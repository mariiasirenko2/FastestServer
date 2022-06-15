package com.example.fastest_server.test;

import com.example.fastest_server.answer.Answer;
import com.example.fastest_server.answer.Chars;
import com.example.fastest_server.question.DocxReader;
import com.example.fastest_server.question.Question;
import com.example.fastest_server.user.User;
import com.example.fastest_server.variant.Variant;
import com.example.fastest_server.variant.VariantRepository;
import com.example.fastest_server.variantquestion.VariantQuestion;
import com.example.fastest_server.variantquestion.VariantQuestionKey;
import com.example.fastest_server.variantquestion.VariantQuestionRepository;
import lombok.AllArgsConstructor;
import org.apache.http.entity.ContentType;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
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
            for (Question question: questionSet) {
                System.out.println(question.getText());
            }
            int i = 1;
            for (Question question: questionSet) {
                System.out.println("Iteration " + i);
                VariantQuestion variantQuestion = new VariantQuestion(variant, question);
                variantQuestion.setQuestionNumber(i++);
                Chars[] chars = Chars.values();
                int letter;
                //variantQuestion.setLetterAnswer(chars[(int) 1 + (Math.random() * 3.5)]);
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
        List<Test> t = testRepository.findByOwner(owner);
        List<TestNameId> tmi= new ArrayList<>();
        for(Test i:t){
            tmi.add(new TestNameId(i.getTestName(),i.getId()));
        }
        return tmi;
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


    public byte[] generateVariantsFile(int idTest) throws Docx4JException {
        List<Variant> variantList = variantRepository.findByTestId(idTest);
        DocxReader docxReader = new DocxReader();
        return  docxReader.generateQuestionDoc(variantList);

    }

    public  byte[] generateBlanks(int idTest) throws Exception {
        List<Variant> variantList = variantRepository.findByTestId(idTest);
        DocxReader docxReader = new DocxReader();
        return docxReader.generateBlanks(variantList);
    }
}
