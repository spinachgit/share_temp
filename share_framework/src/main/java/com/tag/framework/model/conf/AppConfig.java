package com.tag.framework.model.conf;

public class AppConfig extends AbstractObject
{
  private int id;
  private String name;
  private String description;
  private int pfId = 10;

  private int zoneId = 1;
  private int shutdownPort;
  private String secretKey;
  private long tokenExpire;

  public int getId()
  {
    return this.id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return this.description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public int getPfId() {
    return this.pfId;
  }
  public void setPfId(int pfId) {
    this.pfId = pfId;
  }
  public int getZoneId() {
    return this.zoneId;
  }
  public void setZoneId(int zoneId) {
    this.zoneId = zoneId;
  }
  public int getShutdownPort() {
    return this.shutdownPort;
  }
  public void setShutdownPort(int shutdownPort) {
    this.shutdownPort = shutdownPort;
  }
  public String getSecretKey() {
    return this.secretKey;
  }
  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }
  public long getTokenExpire() {
    return this.tokenExpire;
  }
  public void setTokenExpire(long tokenExpire) {
    this.tokenExpire = tokenExpire;
  }
}