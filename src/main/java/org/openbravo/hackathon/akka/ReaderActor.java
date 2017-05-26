package org.openbravo.hackathon.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ReaderActor extends AbstractActor {
  static public Props props() {
    return Props.create(ReaderActor.class, () -> new ReaderActor());
  }

  static public class LoginByCountry {
    public final String message;

    public LoginByCountry(String message) {
      this.message = message;
    }
  }

  private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public ReaderActor() {
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(LoginByCountry.class, loginByCountry -> {
            log.info(loginByCountry.message);
        })
        .build();
  }
}
