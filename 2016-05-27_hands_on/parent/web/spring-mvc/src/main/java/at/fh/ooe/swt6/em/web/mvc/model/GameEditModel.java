package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Team;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Thomas on 5/22/2016.
 */
@Data
public class GameEditModel extends AbstractEditModel<Long, Game> {

    @Min(0)
    @Max(100)
    private Integer goalsTeam1;
    @Min(0)
    @Max(100)
    private Integer goalsTeam2;
    @NotNull
    @Min(1)
    private Long team1;
    @NotNull
    @Min(1)
    private Long team2;

    private String team1Name;
    private String team2Name;

    @Override
    public void fromEntity(Game entity) {
        setEntity(entity);
        if (!isNewModel()) {
            goalsTeam1 = entity.getGoalsTeam1();
            goalsTeam2 = entity.getGoalsTeam2();
            team1 = entity.getTeam1().getId();
            team2 = entity.getTeam2().getId();
            team1Name = entity.getTeam1().getName();
            team2Name = entity.getTeam2().getName();
        }
    }

    @Override
    public Game toEntity() {
        final Game game = new Game();
        game.setId(getId());
        game.setVersion(getVersion());
        game.setGoalsTeam1(goalsTeam1);
        game.setGoalsTeam2(getGoalsTeam2());
        game.setTeam1(new Team(team1));
        game.setTeam2(new Team(team2));

        return game;
    }
}
