package org.openbravo.hackathon.akka;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LogReceiver {

  public static void main(String[] args) {
    Path path = Paths.get("/tmp");

    try {

      Files.lines(path).forEach(System.out::println);// print each line

    } catch (IOException ex) {

      ex.printStackTrace();// handle exception here

    }

  }

}
