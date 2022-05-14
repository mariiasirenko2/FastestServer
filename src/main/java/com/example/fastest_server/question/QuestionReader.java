package com.example.fastest_server.question;

import com.example.fastest_server.answer.Answer;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuestionReader {

    List<Question> questionList;
    Question lastQuestion;

    //TODO: uncomment to work with file names instead of files
    /*
    public QuestionReader(String file) throws Docx4JException, JAXBException {
        questionList = new ArrayList<>();
        File doc = new File(file);
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(doc);
        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
        String textNodesXPath = "//w:p";
        List<Object> textNodes = mainDocumentPart
                .getJAXBNodesViaXPath(textNodesXPath, true);

        for (Object obj : textNodes) {
            P paragraph = (P) obj;
            if (paragraph.getPPr().getNumPr().getIlvl() != null) {
                if (paragraph.getPPr().getNumPr().getIlvl().getVal().intValue() == 0) {
                    if (lastQuestion != null) {
                        questionList.add(lastQuestion);
                    }
                    lastQuestion = new Question(paragraph.toString(), new ArrayList<Answer>());
                    System.out.println(lastQuestion.getText());
                } else {
                    boolean truth = false;
                    if (paragraph.getPPr().getRPr() != null) {
                        if (paragraph.getPPr().getRPr().getB() != null) {
                            if (paragraph.getPPr().getRPr().getB().isVal()) {
                                truth = true;
                            }
                        }
                    }
                    Answer answer = new Answer(paragraph.toString(), truth, lastQuestion);
                    System.out.println(answer.getText());
                    lastQuestion.addAnswer(answer);
                }
            }
            questionList.add(lastQuestion);
        }

    }
    */

    public QuestionReader(File doc) throws Docx4JException, JAXBException {
        questionList = new ArrayList<>();
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(doc);
        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
        String textNodesXPath = "//w:p";
        List<Object> textNodes = mainDocumentPart
                .getJAXBNodesViaXPath(textNodesXPath, true);

        for (Object obj : textNodes) {
            P paragraph = (P) obj;
            if (paragraph.getPPr().getNumPr().getIlvl() != null) {
                if (paragraph.getPPr().getNumPr().getIlvl().getVal().intValue() == 0) {
                    if (lastQuestion != null) {
                        questionList.add(lastQuestion);
                    }
                    lastQuestion = new Question(paragraph.toString(), new ArrayList<Answer>());
                    System.out.println(lastQuestion.getText());
                } else {
                    boolean truth = false;
                    if (paragraph.getPPr().getRPr() != null) {
                        if (paragraph.getPPr().getRPr().getB() != null) {
                            if (paragraph.getPPr().getRPr().getB().isVal()) {
                                truth = true;
                            }
                        }
                    }
                    Answer answer = new Answer(paragraph.toString(), truth, lastQuestion);
                    System.out.println(answer.getText());
                    lastQuestion.addAnswer(answer);
                }
            }
            questionList.add(lastQuestion);
        }

    }


    public List<Question> getQuestionList() {
        return questionList;
    }
}
