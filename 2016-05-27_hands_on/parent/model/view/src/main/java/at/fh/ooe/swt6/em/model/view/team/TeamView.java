package at.fh.ooe.swt6.em.model.view.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Thomas on 5/16/2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamView implements Serializable {

    //<editor-fold desc="Properties">
    Long id;
    String name;
    Long gamesWin;
    Long gamesLost;
    Long gamesEqual;
    //</editor-fold>

    public TeamView(Long id) {
        this.id = id;
    }

}
