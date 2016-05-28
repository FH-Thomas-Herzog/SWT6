package at.fh.ooe.swt6.em.model.view.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Created by Thomas on 5/24/2016.
 */
@AllArgsConstructor
@NoArgsConstructor
public class GameView extends AbstractEntityView<Long> {

    //<editor-fold desc="Properties">@Getter
    @Getter
    @Setter
    private String team1Name;
    @Getter
    @Setter
    private String team2Name;
    @Getter
    @Setter
    private Integer goalsTeam1;
    @Getter
    @Setter
    private Integer goalsTeam2;
    @Getter
    @Setter
    private Integer tipTeam1Count;
    @Getter
    @Setter
    private Integer tipTeam2Count;
    @Getter
    @Setter
    private Integer tipEventCount;
    @Getter
    @Setter
    private Integer totalTipCount;
    @Getter
    @Setter
    LocalDateTime gameDate;
    //</editor-fold>

    public GameView(Long id) {
        this.id = id;
    }

    public boolean isTeam1Winner() {
        return (goalsTeam1 != null && goalsTeam2 != null && goalsTeam1 >= goalsTeam2);
    }

    public boolean isTeam2Winner() {
        return (goalsTeam1 != null && goalsTeam2 != null && goalsTeam2 >= goalsTeam1);
    }

    public boolean isFinished() {
        return (goalsTeam1 != null && goalsTeam2 != null);
    }
}
