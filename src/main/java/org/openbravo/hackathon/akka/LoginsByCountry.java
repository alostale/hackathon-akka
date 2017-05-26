package org.openbravo.hackathon.akka;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoginsByCountry {

  private Map<String, Integer> loginsByCountry = new HashMap<String, Integer>();

  public int getNumEvents() {
    int numEvents = 0;
    for (String country : loginsByCountry.keySet()) {
      numEvents += loginsByCountry.get(country);
    }
    return numEvents;
  }

  public void updateState(OpenbravoLoginInfo c) {
    if (loginsByCountry.containsKey(c.getCountry())) {
      loginsByCountry.put(c.getCountry(), loginsByCountry.get(c.getCountry() + 1));
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

}
