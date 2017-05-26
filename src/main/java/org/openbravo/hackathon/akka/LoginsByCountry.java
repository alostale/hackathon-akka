package org.openbravo.hackathon.akka;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LoginsByCountry implements Serializable {

  private Map<String, Integer> loginsByCountry = new ConcurrentHashMap<>();

  public int getNumEvents() {
    int numEvents = 0;
    for (String country : loginsByCountry.keySet()) {
      numEvents += loginsByCountry.get(country);
    }
    return numEvents;
  }

  public void updateState(OpenbravoLoginInfo c) {
    System.out.println("update state");
    if (c.getCountry() == null) {
      return;
    }
    if (loginsByCountry.containsKey(c.getCountry())) {
      loginsByCountry.put(c.getCountry(), loginsByCountry.get(c.getCountry()) + 1);
    } else {
      loginsByCountry.put(c.getCountry(), 1);
    }
  }

  public Set<String> getCountries() {
    return loginsByCountry.keySet();
  }

  public Integer getLoginsForCountry(String country) {
    return loginsByCountry.get(country);
  }

  public void printLoginsByCountry() {
    for (String country : getCountries()) {
      System.out.println(country + ": " + getLoginsForCountry(country));
    }
  }

}
