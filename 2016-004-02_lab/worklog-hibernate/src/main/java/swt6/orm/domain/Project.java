package swt6.orm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Project implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
  // private Employee manager;
  private Set<Employee> members = new HashSet<>();

  public Long getId() {
    return id;
  }

  public Project() {
  }

  public Project(String name) {
    this.name = name;
  }

  @SuppressWarnings("unused")
  private void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Employee> getMembers() {
    return members;
  }

  public void setMembers(Set<Employee> members) {
    this.members = members;
  }

  public void addMember(Employee empl) {
    // TODO add code
  }

  public String toString() {
    return name;
  }
}
