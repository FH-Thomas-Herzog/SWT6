package swt6.orm.domain.annotated;

import java.util.Date;

public class PermanentEmployee extends Employee {
    private static final long serialVersionUID = 1L;
    private double salary;

    public PermanentEmployee() {
    }

    public PermanentEmployee(String firstName, String lastName, Date dateOfBirth) {
        super(firstName, lastName, dateOfBirth);
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return super.toString() + ", salary=" + salary;
    }
}
