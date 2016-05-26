package at.fh.ooe.swt6.em.model.view.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by Thomas on 5/24/2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameView extends EntityView<Long> {

    //<editor-fold desc="Properties">
    String team1Name;
    String team2Name;
    Integer goalsTeam1;
    Integer goalsTeam2;
    Integer tipTeam1Count;
    Integer tipTeam2Count;
    Integer tipEventCount;
    Integer totalTipCount;
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
