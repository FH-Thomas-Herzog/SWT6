package at.fh.ooe.swt6.em.model.view.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Thomas on 5/16/2016.
 */
@NoArgsConstructor
@AllArgsConstructor
public class TeamView extends AbstractEntityView<Long> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Integer gamesWin;
    @Getter
    @Setter
    private Integer gamesLost;
    @Getter
    @Setter
    private Integer gamesEqual;
    //</editor-fold>

    public TeamView(Long id) {
        this.id = id;
    }

    public TeamView(Long id,
                    Long version) {
        this.id = id;
        this.version = version;
    }

    public TeamView(Long id,
                    Long version,
                    String name) {
        this.id = id;
        this.version = version;
        this.name = name;
    }

}
