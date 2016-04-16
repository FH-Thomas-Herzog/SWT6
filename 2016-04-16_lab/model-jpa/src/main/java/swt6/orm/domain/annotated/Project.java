package swt6.orm.domain.annotated;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    // private Employee manager;
    private Set<Employee> members = new HashSet<>();

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
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
        if (empl == null) {
            throw new IllegalArgumentException("Null Employee");
        }
        empl.getProjects().add(this);
        members.add(empl);
    }

    @Override
    public String toString() {
        return name;
    }
}
