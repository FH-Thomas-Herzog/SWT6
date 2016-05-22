package at.fh.ooe.swt6.em.model.view.team;

import lombok.Value;

import java.io.Serializable;

/**
 * Created by Thomas on 5/16/2016.
 */
@Value
public class TeamView implements Serializable {

    //<editor-fold desc="Properties">
    String name;
    Long gamesWin;
    Long gamesLost;
    Long gamesEqual;
    //</editor-fold>

}
