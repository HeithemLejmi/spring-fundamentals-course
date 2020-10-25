package com.pluralsight.model;

public class Speaker {

  private String firstName;
  private String lastName;
  private double age;

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public double getAge() {
    return age;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setAge(double age) {
    this.age = age;
  }
}
