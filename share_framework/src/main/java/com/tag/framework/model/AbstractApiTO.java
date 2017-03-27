package com.tag.framework.model;

public class AbstractApiTO extends AbstractObject
{
  private String app;
  private String func;
  private String sign;
  private Integer userid;

  public String getApp()
  {
    return this.app;
  }
  public void setApp(String app) {
    this.app = app;
  }
  public String getFunc() {
    return this.func;
  }
  public void setFunc(String func) {
    this.func = func;
  }
  public String getSign() {
    return this.sign;
  }
  public void setSign(String sign) {
    this.sign = sign;
  }
  public Integer getUserid() {
    return this.userid;
  }
  public void setUserid(Integer userid) {
    this.userid = userid;
  }
}