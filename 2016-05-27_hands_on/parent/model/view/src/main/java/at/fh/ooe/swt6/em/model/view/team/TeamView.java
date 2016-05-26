package at.fh.ooe.swt6.em.model.view.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Thomas on 5/16/2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamView implements EntityView<Long> {

    //<editor-fold desc="Properties">
    private Long id;
    private Long version;
    private String name;
    private Integer gamesWin;
    private Integer gamesLost;
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
