package com.example.fastest_server.question;

import com.example.fastest_server.answer.Answer;
import com.example.fastest_server.variant.Variant;
import com.example.fastest_server.variantquestion.VariantQuestion;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class DocxReader {

    List<Question> questionList;
    List<String> studentList;
    Question lastQuestion;
    ObjectFactory factory;

    public DocxReader() {
        factory = Context.getWmlObjectFactory();
    }

    public List<Question> readQuestions(File file) throws Docx4JException, JAXBException {
        questionList = new ArrayList<>();
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(file);
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

    public List<Question> getQuestionList() {
        return questionList;
    }

    public List<String> getStudentList() {
        return studentList;
    }

    public List<String> readStudents(File file) throws Docx4JException, JAXBException {
        studentList = new ArrayList<>();
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(file);
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

    public void generateQuestionDoc(List<Variant> variantList) throws Docx4JException {

        WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        NumberingDefinitionsPart numberingDefinitionsPart = new NumberingDefinitionsPart();
        Numbering numbering = factory.createNumbering();
        numberingDefinitionsPart.setContents(numbering);

        mainDocumentPart.addTargetPart(numberingDefinitionsPart);
        int idCounter = 1;
        /////////////////////////////////////////////////////
        for (Variant variant: variantList) {
            SortedSet<VariantQuestion> questionSet = new TreeSet<>(variant.getVariantQuestions());
            P nameParagraph = factory.createP();
            R nameRun = factory.createR();
            Text nameText = factory.createText();
            nameText.setValue(variant.getStudentName());
            PPr namePPr = factory.createPPr();
            nameRun.setRPr(gerFontProperty("Times New Roman", 14));
            nameRun.getRPr().setB(new BooleanDefaultTrue());
            nameParagraph.setPPr(namePPr);
            nameParagraph.getPPr().setJc(new Jc());
            nameParagraph.getPPr().getJc().setVal(JcEnumeration.CENTER);
            nameRun.getContent().add(nameText);
            nameParagraph.getContent().add(nameRun);
            mainDocumentPart.getContent().add(nameParagraph);

            Numbering.Num num = factory.createNumberingNum();
            num.setNumId(BigInteger.valueOf(idCounter));
            Numbering.Num.AbstractNumId abstractNumId = factory.createNumberingNumAbstractNumId();
            Numbering.AbstractNum abstractNum = getAbstractNumbering(idCounter);
            numbering.getAbstractNum().add(abstractNum);
            abstractNumId.setVal(BigInteger.valueOf(idCounter));
            num.setAbstractNumId(abstractNumId);
            numbering.getNum().add(num);
            numbering.getAbstractNum().add(abstractNum);

            for  (VariantQuestion variantQuestion: questionSet) {
                Question question = variantQuestion.getQuestion();
                P questionParagraph = factory.createP();
                R questionRun = factory.createR();
                Text questionText = factory.createText();
                questionText.setValue(question.getText());
                questionRun.setRPr(gerFontProperty("Times New Roman", 14));
                questionRun.getContent().add(questionText);
                questionParagraph.getContent().add(questionRun);
                PPr questionPPr = factory.createPPr();
                questionPPr.setNumPr(getNumProperty(idCounter, 0));
                questionParagraph.setPPr(questionPPr);
                questionParagraph.getPPr().setJc(new Jc());
                questionParagraph.getPPr().getJc().setVal(JcEnumeration.BOTH);
                mainDocumentPart.getContent().add(questionParagraph);
                for (Answer answer : question.getAnswers()) {
                    P answerParagraph = factory.createP();
                    R answerRun = factory.createR();
                    Text answerText = factory.createText();
                    answerText.setValue(answer.getText());
                    answerRun.getContent().add(answerText);
                    answerRun.setRPr(gerFontProperty("Times New Roman", 14));
                    answerParagraph.getContent().add(answerRun);
                    PPr answerPPr = factory.createPPr();
                    answerPPr.setNumPr(getNumProperty(idCounter, 1));
                    answerParagraph.setPPr(answerPPr);
                    answerParagraph.getPPr().setJc(new Jc());
                    answerParagraph.getPPr().getJc().setVal(JcEnumeration.BOTH);
                    mainDocumentPart.getContent().add(answerParagraph);
                }
            }
            P breakParagraph = factory.createP();
            R breakRun = factory.createR();
            Br breakBr = factory.createBr();
            breakBr.setType(STBrType.PAGE);
            breakRun.getContent().add(breakBr);
            breakParagraph.getContent().add(breakRun);
            mainDocumentPart.getContent().add(breakParagraph);
            idCounter++;
        }
        File exportFile = new File("welcome.docx");
        wordPackage.save(exportFile);
    }


    private Numbering.AbstractNum getAbstractNumbering(int abstractNumIdValue) {

        BigInteger listStyleId = BigInteger.valueOf(abstractNumIdValue);
        Numbering.AbstractNum abstractNum = factory.createNumberingAbstractNum();
        abstractNum.setAbstractNumId(listStyleId);
        abstractNum.setMultiLevelType(new Numbering.AbstractNum.MultiLevelType());
        abstractNum.getMultiLevelType().setVal("hybridMultilevel");
        Lvl questionLvl = factory.createLvl();
        questionLvl.setIlvl(BigInteger.ZERO);
        NumFmt questionNumFmt = factory.createNumFmt();
        questionLvl.setNumFmt(questionNumFmt);
        questionNumFmt.setVal(NumberFormat.DECIMAL);
        Lvl.LvlText questionLvlText = factory.createLvlLvlText();
        questionLvl.setLvlText(questionLvlText);
        questionLvlText.setVal("%1.");
        PPr questionPPr = factory.createPPr();
        questionLvl.setPPr(questionPPr);
        RPr questionRPr = factory.createRPr();
        questionLvl.setRPr(questionRPr);
        RFonts questionRFonts = factory.createRFonts();
        questionRPr.setRFonts(questionRFonts);
        questionRFonts.setAscii("Times New Roman");
        questionRFonts.setHint(org.docx4j.wml.STHint.DEFAULT);
        questionRFonts.setHAnsi("Times New Roman");
        questionRPr.setSz(new HpsMeasure());
        questionRPr.getSz().setVal(BigInteger.valueOf(28));
        questionRPr.setB(new BooleanDefaultTrue());
        Lvl.Start questionStart = factory.createLvlStart();
        questionLvl.setStart(questionStart);
        questionStart.setVal(BigInteger.valueOf(1));
        PPrBase.Ind questionInd = factory.createPPrBaseInd();
        questionPPr.setInd(questionInd);
        questionInd.setLeft(BigInteger.valueOf(720));
        questionInd.setHanging(BigInteger.valueOf(360));
        abstractNum.getLvl().add(questionLvl);
        Lvl answerLvl = factory.createLvl();
        answerLvl.setIlvl(BigInteger.ONE);
        NumFmt answerNumFmt = factory.createNumFmt();
        answerLvl.setNumFmt(answerNumFmt);
        answerNumFmt.setVal(NumberFormat.LOWER_LETTER);
        Lvl.LvlText answerLvlText = factory.createLvlLvlText();
        answerLvl.setLvlText(answerLvlText);
        answerLvlText.setVal("%2)");
        PPr answerPPr = factory.createPPr();
        answerLvl.setPPr(answerPPr);
        RPr answerRPr = factory.createRPr();
        answerLvl.setRPr(answerRPr);
        RFonts answerRFonts = factory.createRFonts();
        answerRPr.setRFonts(answerRFonts);
        answerRFonts.setAscii("Times New Roman");
        answerRFonts.setHint(org.docx4j.wml.STHint.DEFAULT);
        answerRFonts.setHAnsi("Times New Roman");
        answerRPr.setSz(new HpsMeasure());
        answerRPr.getSz().setVal(BigInteger.valueOf(28));
        answerRPr.setB(new BooleanDefaultTrue());
        Lvl.Start answerStart = factory.createLvlStart();
        answerLvl.setStart(answerStart);
        answerStart.setVal(BigInteger.valueOf(1));
        PPrBase.Ind answerInd = factory.createPPrBaseInd();
        answerPPr.setInd(answerInd);
        answerInd.setLeft(BigInteger.valueOf(1080));
        answerInd.setHanging(BigInteger.valueOf(360));
        abstractNum.getLvl().add(answerLvl);
        Numbering.Num.AbstractNumId abstractNumId = factory.createNumberingNumAbstractNumId();
        abstractNumId.setVal(listStyleId);
        return abstractNum;
    }

    private RPr gerFontProperty(String font, long size) {
        RPr rPr = factory.createRPr();
        RFonts rFonts = factory.createRFonts();
        rFonts.setHAnsi(font);
        rPr.setRFonts(rFonts);
        HpsMeasure hpsMeasure = factory.createHpsMeasure();
        hpsMeasure.setVal(BigInteger.valueOf(size * 2));
        rPr.setSz(hpsMeasure);
        return rPr;
    }

    private PPrBase.NumPr getNumProperty(long numId, long iLvl) {
        PPrBase.NumPr numPr = factory.createPPrBaseNumPr();
        numPr.setNumId(new PPrBase.NumPr.NumId());
        numPr.getNumId().setVal(BigInteger.valueOf(numId));
        numPr.setIlvl(new PPrBase.NumPr.Ilvl());
        numPr.getIlvl().setVal(BigInteger.valueOf(iLvl));
        return numPr;
    }

    public File createFile(String name) {
        try {
            File questionsFile = new File(name);
            questionsFile.createNewFile();
            return questionsFile;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }



}
