package org.openbravo.hackathon.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.SnapshotOffer;

public class LoginProcessorActor extends AbstractPersistentActor {

  private LoginsByCountry loginsByCountry = new LoginsByCountry();
  private int snapShotInterval = 25;
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
    System.out.println("LoginProcessorActor.createReceive");
    return receiveBuilder().match(OpenbravoLoginInfo.class, c -> {
      persist(c, (OpenbravoLoginInfo evt) -> {
        loginsByCountry.updateState(c);
        getContext().getSystem().eventStream().publish(evt);
        readerActor.tell(loginsByCountry, readerActor);
        if (lastSequenceNr() % snapShotInterval == 0 && lastSequenceNr() != 0) {
          saveSnapshot(loginsByCountry);
        }

      });
    }).matchEquals("print", s -> loginsByCountry.printLoginsByCountry()).build();
  }

  // @Override
  // public <A> void persist(A event, Procedure<A> handler) {
  // System.out.println("persisting...");
  // System.out.println("\t" + event.toString());
  // try {
  // handler.apply(event);
  // } catch (Exception e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // }

  @Override
  public Receive createReceiveRecover() {
    return receiveBuilder().match(SnapshotOffer.class, ss -> {
      System.out.println("Recovering snapshot...");
      loginsByCountry = (LoginsByCountry) ss.snapshot();
      System.out.println("Snapshot recovered: ");
      loginsByCountry.printLoginsByCountry();
    }).match(OpenbravoLoginInfo.class, loginsByCountry::updateState).build();
  }

}
