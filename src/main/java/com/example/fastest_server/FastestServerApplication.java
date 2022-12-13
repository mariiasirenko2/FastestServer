package com.example.fastest_server;

import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FastestServerApplication {


  static {
    nu.pattern.OpenCV.loadLocally();
   // System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
  }

  public static void main(String[] args) {
    SpringApplication.run(FastestServerApplication.class, args);

  }

}
