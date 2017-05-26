package org.openbravo.hackathon.akka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class LogReceiver {

  public static void main(String[] args) throws IOException {
    final ActorSystem system = ActorSystem.create("log");

    ActorRef readerActor = system.actorOf(ReaderActor.props());
    ActorRef processorActor = system.actorOf(LoginProcessorActor.props(readerActor));
    try (BufferedReader br = new BufferedReader(new FileReader("/tmp/log"))) {
      String line;
      while (true) {
        while ((line = br.readLine()) == null) {
        }

        if (!line.contains("INFO:")) {
          continue;
        }

        String[] data = line.substring(line.indexOf("INFO:") + 6).split(":");
        String ip = data[0];
        String country = data[1].trim().substring(0, 2);
        processorActor.tell(new OpenbravoLoginInfo(ip, country, new Date()), ActorRef.noSender());

        System.out.println(line);
      }
    }
  }

}
