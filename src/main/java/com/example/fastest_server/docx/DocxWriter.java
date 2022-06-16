package com.example.fastest_server.docx;
import com.example.fastest_server.answer.Answer;
import com.example.fastest_server.question.Question;
import com.example.fastest_server.variant.Variant;
import com.example.fastest_server.variantquestion.VariantQuestion;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.*;

@Service
class DocxWriter {


    private final ObjectFactory factory = Context.getWmlObjectFactory();

    public byte[] generateResults(List<Variant> variantList) throws Docx4JException {
        WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        Body body = mainDocumentPart.getContents().getBody();
        RPr defaultProperties = DocxStyler.setFontPropertyRPr("Times New Roman", 14);
        for (Variant variant: variantList) {
            String str = variant.getStudentName() + "    " + String.valueOf(variant.getMark());
            body.getContent().add(DocxContentCreator.addTextParagraph(str, defaultProperties));
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        wordPackage.save(outputStream);
        File exportFile = new File("results.docx");
        wordPackage.save(exportFile);
        return outputStream.toByteArray();
    }

    public byte[] generateBlanks(List<Variant> variantList) throws Exception {
        WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        Body body = mainDocumentPart.getContents().getBody();
        body.setSectPr(DocxStyler.setDocumentBorders(425, 1440, 425, 1440));
        RPr defaultProperties = DocxStyler.setFontPropertyRPr("Times New Roman", 14);
        defaultProperties.setB(new BooleanDefaultTrue());
        File image = new File("blank.png" );

        for (Variant variant: variantList) {
            Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            //generates QR code with Low level(L) error correction capability
            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //TODO: find simpler way of getting test name
            String name = ((VariantQuestion) (variant.getVariantQuestions().toArray()[0])).getQuestion().getTest().getTestName();

            body.getContent().add(DocxContentCreator.addTextParagraph("Тест: " + name, defaultProperties));
            body.getContent().add(DocxContentCreator.addTextParagraph("Студент: " + variant.getStudentName(), defaultProperties));

            //TODO: make picture adding more nice
            BinaryPartAbstractImage qrPart = BinaryPartAbstractImage
                    .createImagePart(wordPackage, DocxContentCreator.generateQR(String.valueOf(variant.getId()), "UTF-8", hashMap, 100, 100));
            Inline qrInline = qrPart.createImageInline(
                    "123", "Alt Text", 1, 2, false);
            P qrParagraph = DocxContentCreator.addImageParagraph(qrInline);
            body.getContent().add(qrParagraph);

            byte[] fileContent = Files.readAllBytes(image.toPath());
            BinaryPartAbstractImage imagePart = BinaryPartAbstractImage
                    .createImagePart(wordPackage, fileContent);
            Inline inline = imagePart.createImageInline(
                    "ElPlyCongroo", "Alt Text", 1, 2, 5923915, 7065250, false);

            body.getContent().add(DocxContentCreator.addImageParagraph(inline));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        File file = new File("blanks.docx");
        wordPackage.save(file);
        wordPackage.save(outputStream);
        return outputStream.toByteArray();



    }

    public byte[] generateQuestionDoc(List<Variant> variantList) throws Docx4JException {

        //TODO: find out how to separate lists from each other by Numbering without creating different AbstractNumberings
        WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        NumberingDefinitionsPart numberingDefinitionsPart = new NumberingDefinitionsPart();
        Numbering numbering = factory.createNumbering();
        numberingDefinitionsPart.setContents(numbering);
        mainDocumentPart.addTargetPart(numberingDefinitionsPart);
        Body body = factory.createBody();
        mainDocumentPart.getContent().add(body);
        RPr defaultText = DocxStyler.setFontPropertyRPr("Times New Roman", 14);
        RPr boldText = DocxStyler.setFontPropertyRPr("Times New Roman", 14);
        boldText.setB(new BooleanDefaultTrue());
        int idCounter = 1;

        for (Variant variant: variantList) {
            SortedSet<VariantQuestion> questionSet = new TreeSet<>(variant.getVariantQuestions());
            P nameParagraph = DocxContentCreator.addTextParagraph(variant.getStudentName(), boldText);
            nameParagraph.setPPr(new PPr());
            nameParagraph.getPPr().setJc(new Jc());
            nameParagraph.getPPr().getJc().setVal(JcEnumeration.CENTER);
            body.getContent().add(nameParagraph);

            Numbering.Num num = factory.createNumberingNum();
            num.setNumId(BigInteger.valueOf(idCounter));
            Numbering.Num.AbstractNumId abstractNumId = factory.createNumberingNumAbstractNumId();
            Numbering.AbstractNum abstractNum = DocxStyler.getAbstractNumbering(idCounter);
            numbering.getAbstractNum().add(abstractNum);
            abstractNumId.setVal(BigInteger.valueOf(idCounter));
            num.setAbstractNumId(abstractNumId);
            numbering.getNum().add(num);
            numbering.getAbstractNum().add(abstractNum);

            for (VariantQuestion variantQuestion: questionSet) {
                Question question = variantQuestion.getQuestion();
                P questionParagraph = DocxContentCreator.addTextParagraph(question.getText(), defaultText);
                PPr questionPPr = factory.createPPr();
                questionPPr.setNumPr(DocxStyler.setNumProperty(idCounter, 0));
                questionParagraph.setPPr(questionPPr);
                questionParagraph.getPPr().setJc(new Jc());
                questionParagraph.getPPr().getJc().setVal(JcEnumeration.BOTH);
                body.getContent().add(questionParagraph);
                for (Answer answer : question.getAnswers()) {
                    P answerParagraph = DocxContentCreator.addTextParagraph(answer.getText(), defaultText);
                    PPr answerPPr = factory.createPPr();
                    answerPPr.setNumPr(DocxStyler.setNumProperty(idCounter, 1));
                    answerParagraph.setPPr(answerPPr);
                    answerParagraph.getPPr().setJc(new Jc());
                    answerParagraph.getPPr().getJc().setVal(JcEnumeration.BOTH);
                    body.getContent().add(answerParagraph);
                }
            }
            body.getContent().add(DocxContentCreator.addPageBreak());
            idCounter++;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        File file = new File("variants.docx");
        wordPackage.save(file);
        wordPackage.save(outputStream);

        return outputStream.toByteArray();
    }

}
