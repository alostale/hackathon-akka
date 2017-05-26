package org.openbravo.hackathon.akka;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.persistence.AbstractPersistentActor;

public class LoginProcessorActor extends AbstractPersistentActor {

  private Map<String, Integer> loginsByCountry = new HashMap<String, Integer>();
  private int snapShotInterval = 1000;
  private ActorRef readerActor;

  @Override
  public String persistenceId() {
    return "sample-id-1";
  }

  public int getNumEvents() {
    int numEvents = 0;
    for (String country : loginsByCountry.keySet()) {
      numEvents += loginsByCountry.get(country);
    }
    return numEvents;
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
        updateState(c);
        getContext().getSystem().eventStream().publish(evt);
        if (lastSequenceNr() % snapShotInterval == 0 && lastSequenceNr() != 0)
          // IMPORTANT: create a copy of snapshot because ExampleState is mutable
          saveSnapshot(loginsByCountry);
      });
    }).matchEquals("print", s -> System.out.println(loginsByCountry)).build();
  }

  private void updateState(OpenbravoLoginInfo c) {
    if (loginsByCountry.containsKey(c.getCountry())) {
      loginsByCountry.put(c.getCountry(), loginsByCountry.get(c.getCountry() + 1));
    } else {
      loginsByCountry.put(c.getCountry(), 1);
    }

  }

  @Override
  public Receive createReceiveRecover() {
    // TODO Auto-generated method stub
    return null;
  }

}

class Cmd implements Serializable {
  private static final long serialVersionUID = 1L;
  private final String data;

  public Cmd(String data) {
    this.data = data;
  }

  public String getData() {
    return data;
  }
}

class Evt implements Serializable {
  private static final long serialVersionUID = 1L;
  private final String data;

  public Evt(String data) {
    this.data = data;
  }

  public String getData() {
    return data;
  }
}

class ExampleState implements Serializable {
  private static final long serialVersionUID = 1L;
  private final ArrayList<String> events;

  public ExampleState() {
    this(new ArrayList<>());
  }

  public ExampleState(ArrayList<String> events) {
    this.events = events;
  }

  public ExampleState copy() {
    return new ExampleState(new ArrayList<>(events));
  }

  public void update(Evt evt) {
    events.add(evt.getData());
  }

  public int size() {
    return events.size();
  }

  @Override
  public String toString() {
    return events.toString();
  }
}