package com.example.fastest_server.question;

import com.example.fastest_server.answer.Answer;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.*;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class DocxReader {

    List<Question> questionList;
    List<String> studentList;
    Question lastQuestion;

    public List<Question> readQuestions(String file) throws Docx4JException, JAXBException {
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
        return questionList;
    }


    public List<String> readStudents(String file) throws Docx4JException, JAXBException {
        studentList = new ArrayList<>();
        File doc = new File(file);
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(doc);
        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
        String textNodesXPath = "//w:p";
        List<Object> textNodes = mainDocumentPart
                .getJAXBNodesViaXPath(textNodesXPath, true);

        for (Object obj : textNodes) {
            P paragraph = (P) obj;
            studentList.add(paragraph.toString());
        }
        return studentList;
    }

    public void generateVariants(List<Question> questions, String[] students) throws Docx4JException, JAXBException {
        WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        ObjectFactory factory = Context.getWmlObjectFactory();
        //////////////////////////////////////////////////////////
        NumberingDefinitionsPart numberingDefinitionsPart = new NumberingDefinitionsPart();
        numberingDefinitionsPart.setJaxbElement(numberingDefinitionsPart.unmarshalDefaultNumbering());
        mainDocumentPart.addTargetPart(numberingDefinitionsPart);

        StyleDefinitionsPart styleDefinitionsPart = new StyleDefinitionsPart();
        styleDefinitionsPart.setJaxbElement((Styles) styleDefinitionsPart.unmarshalDefaultStyles());
        mainDocumentPart.addTargetPart(styleDefinitionsPart);
        /////////////////////////////////////////////////////
        for (Question question: questions) {
            P paragraph = factory.createP();
            R run = factory.createR();
            Text text = factory.createText();
            text.setValue(question.getText());
            RPr rpr = factory.createRPr();
            RFonts rfonts = factory.createRFonts();
            rfonts.setHAnsi("Times New Roman");
            rpr.setRFonts(rfonts);
            HpsMeasure hpsMeasure = factory.createHpsMeasure();
            hpsMeasure.setVal(BigInteger.valueOf(28));
            rpr.setSz(hpsMeasure);
            run.setRPr(rpr);
            run.getContent().add(text);
            paragraph.getContent().add(run);
            PPrBase.NumPr numPr = factory.createPPrBaseNumPr();
            PPrBase.NumPr.NumId numId = factory.createPPrBaseNumPrNumId();
            numId.setVal(BigInteger.valueOf(2));
            PPrBase.NumPr.Ilvl iLvl = factory.createPPrBaseNumPrIlvl();
            iLvl.setVal(BigInteger.valueOf(0));
            PPr ppr = factory.createPPr();
            numPr.setIlvl(iLvl);
            numPr.setNumId(numId);
            ppr.setNumPr(numPr);
            paragraph.setPPr(ppr);
            mainDocumentPart.getContent().add(paragraph);
            for (Answer answer : question.getAnswers()) {
                P paragraphAnswer = factory.createP();
                R runAnswer = factory.createR();
                Text textAnswer = factory.createText();
                textAnswer.setValue(answer.getText());
                runAnswer.getContent().add(textAnswer);
                paragraphAnswer.getContent().add(runAnswer);
                mainDocumentPart.getContent().add(paragraphAnswer);
            }
        }
        File exportFile = new File("welcome.docx");
        wordPackage.save(exportFile);
    }
}
