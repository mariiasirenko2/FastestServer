package com.example.fastest_server.docx;

import org.docx4j.jaxb.Context;
import org.docx4j.wml.*;

import java.math.BigInteger;

public class DocxStyler {

    private static final ObjectFactory factory = Context.getWmlObjectFactory();

    protected static RPr setFontPropertyRPr(String font, long size) {
        RPr rPr = factory.createRPr();
        RFonts rFonts = factory.createRFonts();
        rFonts.setHAnsi(font);
        rFonts.setAscii(font);
        rPr.setRFonts(rFonts);
        HpsMeasure hpsMeasure = factory.createHpsMeasure();
        hpsMeasure.setVal(BigInteger.valueOf(size * 2));
        rPr.setSz(hpsMeasure);
        return rPr;
    }

    static PPrBase.NumPr setNumProperty(long numId, long iLvl) {
        PPrBase.NumPr numPr = factory.createPPrBaseNumPr();
        numPr.setNumId(new PPrBase.NumPr.NumId());
        numPr.getNumId().setVal(BigInteger.valueOf(numId));
        numPr.setIlvl(new PPrBase.NumPr.Ilvl());
        numPr.getIlvl().setVal(BigInteger.valueOf(iLvl));
        return numPr;
    }

    static SectPr setDocumentBorders(long top, long right, long bottom, long left) {
        SectPr sectPr = factory.createSectPr();
        SectPr.PgMar pgMar = factory.createSectPrPgMar();
        sectPr.setPgMar(pgMar);
        pgMar.setTop( BigInteger.valueOf(top));
        pgMar.setRight( BigInteger.valueOf(right));
        pgMar.setBottom( BigInteger.valueOf(bottom));
        pgMar.setLeft( BigInteger.valueOf(left));
        return sectPr;
    }

    static Numbering.AbstractNum getAbstractNumbering(int abstractNumIdValue) {
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
        questionRFonts.setHint(STHint.DEFAULT);
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
        answerRFonts.setHint(STHint.DEFAULT);
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
}
