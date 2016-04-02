package swt6.orm.domain;

import java.util.Date;


public class PermanentEmployee extends Employee {
  private static final long serialVersionUID = 1L;
  private double salary;

  public PermanentEmployee() {  
  }
  
  public PermanentEmployee(String firstName, String lastName, Date dateOfBirth) {
    super(firstName, lastName, dateOfBirth);
  }
  
  public PermanentEmployee(String firstName, String lastName, Date dateOfBirth, Address address) {
    super(firstName, lastName, dateOfBirth, address);
  }
  
  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }
  
  public String toString() {
    return super.toString() + ", salary=" + salary;
  }
}
