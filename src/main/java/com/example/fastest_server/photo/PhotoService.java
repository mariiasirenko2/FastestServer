package com.example.fastest_server.photo;

import static org.opencv.highgui.Highgui.CV_LOAD_IMAGE_UNCHANGED;
import static org.opencv.imgcodecs.Imgcodecs.imdecode;

import com.example.fastest_server.answer.Chars;
import com.example.fastest_server.variantquestion.VariantQuestionService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoService {

  private Scanner scanner;
  private int variantNumber;
  @Autowired
  private VariantQuestionService variantQuestionService;

  public byte[] checkPhoto(MultipartFile file) throws IOException {

    byte[] byteArr = file.getBytes();
    Mat mat = Imgcodecs.imdecode(new MatOfByte(byteArr), Imgcodecs.IMREAD_UNCHANGED);


    scanner = new Scanner(mat);
    String qr = scanner.detectQRCode();
    if (qr.isEmpty()) {
      variantNumber = 0;
    } else {
      variantNumber = Integer.parseInt(qr);
    }

    List<Chars> answers = new ArrayList<>();
    answers = variantQuestionService.getAnswers(variantNumber);
    scanner.addAnswers(answers);

    Mat processed  = null;
    try {
      processed = scanner.scan();
    } catch (Exception e) {
      e.printStackTrace();
    }

    byte [] image = new byte[(int) (processed.total() * processed.channels())];
    processed.get(0,0,image);
    return  image;
  }

}
