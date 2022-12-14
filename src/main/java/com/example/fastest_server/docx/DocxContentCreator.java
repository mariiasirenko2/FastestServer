package com.example.fastest_server.docx;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.wml.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

class DocxContentCreator {

    private static final ObjectFactory factory = Context.getWmlObjectFactory();

    static P addImageParagraph(Inline inline) {
        P p = factory.createP();
        R r = factory.createR();
        p.getContent().add(r);
        Drawing drawing = factory.createDrawing();
        r.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return p;
    }

    static P addTextParagraph(String paragraphText, RPr fontProperty) {
        P p = factory.createP();
        R r = factory.createR();
        Text text = factory.createText();
        text.setValue(paragraphText);
        r.getContent().add(text);
        r.setRPr(fontProperty);
        p.getContent().add(r);
        return p;
    }

    static P addPageBreak() {
        P breakParagraph = factory.createP();
        R breakRun = factory.createR();
        Br breakBr = factory.createBr();
        breakBr.setType(STBrType.PAGE);
        breakRun.getContent().add(breakBr);
        breakParagraph.getContent().add(breakRun);
        return breakParagraph;
    }

    static byte[] generateQR(String data, String charset, Map map, int h, int w) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(MatrixToImageWriter.toBufferedImage(matrix), "png", baos);
        return baos.toByteArray();
    }
}
