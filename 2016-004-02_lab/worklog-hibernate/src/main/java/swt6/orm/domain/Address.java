package swt6.orm.domain;

import java.io.Serializable;

public class Address implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;
  private String zipCode;
  private String city;
  private String street;

  // classes persisted by Hibernate must have a default constructor
  // (newInstance of reflection API)
  public Address() {
  }

  public Address(String zipCode, String city, String street) {
    this.zipCode = zipCode;
    this.city = city;
    this.street = street;
  }

  public Long getId() {
    return id;
  }

  @SuppressWarnings("unused")
  private void setId(Long id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String toString() {
    return zipCode + " " + city + ", " + street;
  }

}
