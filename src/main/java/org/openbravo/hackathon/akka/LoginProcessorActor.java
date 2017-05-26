package org.openbravo.hackathon.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.persistence.AbstractPersistentActor;

public class LoginProcessorActor extends AbstractPersistentActor {

  private LoginsByCountry loginsByCountry = new LoginsByCountry();
  private int snapShotInterval = 1000;
  private ActorRef readerActor;

  @Override
  public String persistenceId() {
    return "sample-id-1";
  }

  static public Props props(ActorRef readerActor) {
    return Props.create(LoginProcessorActor.class, () -> new LoginProcessorActor(readerActor));
  }

  public LoginProcessorActor(ActorRef readerActor) {
    this.readerActor = readerActor;
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder().match(OpenbravoLoginInfo.class, c -> {
      persist(c, (OpenbravoLoginInfo evt) -> {
        loginsByCountry.updateState(c);
        getContext().getSystem().eventStream().publish(evt);
        if (lastSequenceNr() % snapShotInterval == 0 && lastSequenceNr() != 0)
          // IMPORTANT: create a copy of snapshot because ExampleState is mutable
          readerActor.tell(loginsByCountry, readerActor);
        saveSnapshot(loginsByCountry);
      });
    }).matchEquals("print", s -> System.out.println(loginsByCountry)).build();
  }

  @Override
  public Receive createReceiveRecover() {
    // TODO Auto-generated method stub
    return null;
  }

}
