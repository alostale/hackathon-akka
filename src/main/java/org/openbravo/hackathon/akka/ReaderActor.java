package org.openbravo.hackathon.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class ReaderActor extends AbstractActor {
  static public Props props() {
    return Props.create(ReaderActor.class, () -> new ReaderActor());
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder().match(LoginsByCountry.class, loginByCountry -> {
      loginByCountry.printLoginsByCountry();
    }).build();
  }
}
