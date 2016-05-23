package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.model.Team;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by Thomas on 5/22/2016.
 */
@Data
public class TeamEditModel extends AbstractEditModel<Long, Team> {

    @NotNull
    @Length(min = 3, max = 50)
    String name;

    @Override
    public void fromEntity(Team entity) {
        setEntity(entity);
        name = entity.getName();
    }

    @Override
    public Team toEntity() {
        final Team team = new Team();
        team.setId(getId());
        team.setName(name);
        team.setVersion(getVersion());

        return team;
    }
}
