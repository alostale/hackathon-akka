package org.openbravo.hackathon.akka;

import java.io.Serializable;
import java.util.Date;

public class OpenbravoLoginInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String ip;
  private String country;
  private Date date;

  public OpenbravoLoginInfo(String ip, String country, Date date) {
    this.ip=ip;
    this.country=country;
    this.date=date;
  }
  
  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

}