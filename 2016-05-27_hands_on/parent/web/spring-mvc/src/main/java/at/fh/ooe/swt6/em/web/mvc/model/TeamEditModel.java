package at.fh.ooe.swt6.em.web.mvc.model;

import lombok.Data;

/**
 * Created by Thomas on 5/22/2016.
 */
@Data
public class TeamEditModel {

    Long id;
    String name;

    public boolean isNewModel() {
        return id == null;
    }
}
