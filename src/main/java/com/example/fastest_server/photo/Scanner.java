package com.example.fastest_server.photo;

import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.THRESH_OTSU;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.threshold;

import com.example.fastest_server.answer.Chars;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.RETR_TREE;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.THRESH_OTSU;
import static org.opencv.imgproc.Imgproc.approxPolyDP;
import static org.opencv.imgproc.Imgproc.arcLength;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.drawContours;
import static org.opencv.imgproc.Imgproc.findContours;
import static org.opencv.imgproc.Imgproc.threshold;

public class Scanner {
  private final static int QUESTION_QUANTITY = 20;
  private final static int QUANTITY_ANSWER_OPTIONS = 4;

  private final Mat source;

  private Mat gray, hierarchy, cut, cutGray;

  private List<MatOfPoint> contours, bubbles, sortedBubbles;

  private final QRCodeDetector qrCodeDetector;
  private int mark = 0;


  private List<Chars> answer_key;
  private List<Integer> answerFromPhoto;


  public Scanner(Mat source) {
    this.source = source;

    this.qrCodeDetector = new QRCodeDetector();

    hierarchy = new Mat();
    contours = new ArrayList<>();
    bubbles = new ArrayList<>();
    sortedBubbles = new ArrayList<>();
    answerFromPhoto = new ArrayList<>();
  }

  public void addAnswers(List<Chars> answers) {
    answer_key = new ArrayList<>();
    answer_key.addAll(answers);
  }

  public int getMark() {
    return mark;
  }

  public String detectQRCode() {
    return qrCodeDetector.detectAndDecode(source);
  }


  public Mat scan() throws Exception {

    gray = new Mat(source.size(), CV_8UC1);

    cvtColor(source, gray, COLOR_RGB2GRAY);
    GaussianBlur(gray, gray, new Size(5, 5), 0);
    Canny(gray, gray, 75, 200);

    cut = findParentRectangle();

    cutGray = new Mat(source.size(), CV_8UC1);

    cvtColor(cut,
        cutGray,
        COLOR_RGB2GRAY);
    GaussianBlur(cutGray, cutGray, new Size(5, 5), 0);
    threshold(cutGray, cutGray, 0, 255, THRESH_BINARY_INV | THRESH_OTSU);

    findBubbles();

    checkBlank();

    return cut;
  }

  private Mat findParentRectangle() throws Exception {

    findContours(gray.clone(), contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE);

    // find rectangles
    HashMap<Double, MatOfPoint> rectangles = new HashMap<>();
    for (int i = 0; i < contours.size(); i++) {
      MatOfPoint2f approxCurve = new MatOfPoint2f(contours.get(i).toArray());
      approxPolyDP(approxCurve, approxCurve, 0.02 * arcLength(approxCurve, true), true);

      if (approxCurve.toArray().length == 4) {
        rectangles.put((double) i, contours.get(i));
      }
    }


    int parentIndex = -1;

    // choose hierarchical rectangle which is our main wrapper rect
    for (Map.Entry<Double, MatOfPoint> rectangle : rectangles.entrySet()) {
      double index = rectangle.getKey();

      double[] ids = hierarchy.get(0, (int) index);
      double nextId = ids[0];
      double previousId = ids[1];

      if (nextId != -1 && previousId != -1) continue;

      int k = (int) index;
      int c = 0;

      while (hierarchy.get(0, k)[2] != -1) {
        k = (int) hierarchy.get(0, k)[2];
        c++;
      }

      if (hierarchy.get(0, k)[2] != -1) c++;

      if (c >= 3) {
        parentIndex = (int) index;
      }

    }


    if (parentIndex < 0) {
      throw new Exception("Couldn't capture main wrapper");
    }

    Rect roi = boundingRect(contours.get(parentIndex));

    int padding = 30;

    roi.x += padding;
    roi.y += padding;
    roi.width -= 2 * padding;
    roi.height -= 2 * padding;

    return source.submat(roi);
  }

  private void findBubbles() throws Exception {

    contours.clear();

    findContours(cutGray.clone(), contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

    Rect tmp;
    hierarchy.release();

    //Determining the contours of the bubble
    for (int c = 0; c < contours.size(); c++) {
      tmp = Imgproc.boundingRect(contours.get(c));
      double ar = tmp.width / (float) tmp.height;

      if (tmp.width >= 20 && tmp.height >= 20 && ar >= 0.9 && ar <= 1.2) {
        bubbles.add(contours.get(c));
      }
    }

    if (bubbles.size() != QUESTION_QUANTITY * QUANTITY_ANSWER_OPTIONS) {
      throw new Exception("Couldn't capture all bubbles.");
    }


    // order bubbles on coordinate system
    Util.sortTopLeftToBottomRight(bubbles);

    //order bubbles for each question
    for (int j = 0; j < bubbles.size(); j += QUANTITY_ANSWER_OPTIONS * 2) {
      List<MatOfPoint> row = bubbles.subList(j, j + QUANTITY_ANSWER_OPTIONS * 2);
      Util.sortLeftToRight(row);
      sortedBubbles.addAll(row);
    }
  }

  private void checkBlank() {
    List<Integer> checkAnswerHelper = new ArrayList<>();

    //make array with answers of questions left to right
    List<Chars> answersInLeftRightOrder = new ArrayList<>();
    for (int i = 0; i < answer_key.size() / 2; i++) {
      answersInLeftRightOrder.add(answer_key.get(i));
      answersInLeftRightOrder.add(answer_key.get(answer_key.size() / 2 + i));

    }

    //go though question
    for (int i = 0; i < sortedBubbles.size(); i += QUANTITY_ANSWER_OPTIONS) {
      //get question number
      int question = ((i + 1) / QUANTITY_ANSWER_OPTIONS + 1);

      //get though bubble in question
      List<MatOfPoint> rows = sortedBubbles.subList(i, i + QUANTITY_ANSWER_OPTIONS);
      for (int j = 0; j < rows.size(); j++) {

        //make a mask to detect colored bubbles
        MatOfPoint col = rows.get(j);
        List<MatOfPoint> list = Collections.singletonList(col);
        Mat mask = new Mat(cutGray.size(), CvType.CV_8UC1);

        //use mask
        drawContours(mask, list, -1, new Scalar(1, 1, 1), -1);
        Mat conjunction = new Mat(cutGray.size(), CvType.CV_8UC1);
        Core.bitwise_and(cutGray, mask, conjunction);

        //get mask result
        int countNonZero = Core.countNonZero(conjunction);
        Chars letter = Chars.valueOf(j + 1);

        //first bubble has always detected as colored cause the imperfection
        //of algorithm. This code helps to solve this problem
        if (i == 0 && j == 0) countNonZero -= 9500;

        //check is the bubble marked
        if (countNonZero > 600) {
          checkAnswerHelper.add(letter.ordinal());

          //check if the colored bubble is right answer
          if (answersInLeftRightOrder.get(question - 1).equals(letter)) {
            //draw the GREEN contour
            Imgproc.drawContours(cut, sortedBubbles, i + j, new Scalar(0, 255, 0), 2,
                Imgproc.INTER_LINEAR, hierarchy, 2, new Point());
            //count as right answer
            mark++;
          } else {
            //draw the RED contour on right answer
            Imgproc.drawContours(cut, sortedBubbles, i + answersInLeftRightOrder.get(question - 1).ordinal() - 1, new Scalar(255, 0, 255), 2,
                Imgproc.INTER_LINEAR, hierarchy, 2, new Point());
          }
          break;
        }

      }
      if (checkAnswerHelper.size() != question) {
        checkAnswerHelper.add(Chars.EMPTY.ordinal());
        //current question dont have colored bubbles
        //mark right answer with BLUE
        Imgproc.drawContours(cut, sortedBubbles, i + answersInLeftRightOrder.get(question - 1).ordinal() - 1, new Scalar(0, 0, 255), 2,
            Imgproc.INTER_LINEAR, hierarchy, 2, new Point());

      }

    }

    //save answers in right order
    List<Integer> odds = new ArrayList<>();
    List<Integer> evens = new ArrayList<>();
    for (int i = 0; i < answerFromPhoto.size(); i++) {
      if (i % 2 == 0) odds.add(answerFromPhoto.get(i));
      if (i % 2 == 1) evens.add(answerFromPhoto.get(i));
    }

    answerFromPhoto.clear();
    answerFromPhoto.addAll(odds);
    answerFromPhoto.addAll(evens);

    //make a bitmap from processed Mat cut (added contours)

    //Utils.matToBitmap(cut, bitmap);
  }

}
