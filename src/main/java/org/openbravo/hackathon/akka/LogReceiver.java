package org.openbravo.hackathon.akka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogReceiver {

  public static void main(String[] args) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("/tmp/log"))) {
      String line;
      while (true) {
        while ((line = br.readLine()) == null) {
        }

        System.out.println(line);
      }
    }
  }

}
