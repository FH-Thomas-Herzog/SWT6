package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Team;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Thomas on 5/22/2016.
 */
@Data
@NoArgsConstructor
public class GameEntityEditModel extends AbstractEntityEditModel<Long, Game> {

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
    @NotNull
    private String gameDate;

    private String team1Name;
    private String team2Name;
    private LocalDateTime gameDateTime;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm";

    @Override
    public void fromEntity(Game entity) {
        setEntity(entity);
        if (!isNewModel()) {
            gameDateTime = entity.getGameDate();
            gameDate = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(gameDateTime);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        LocalDateTime gameDate = LocalDateTime.parse(this.gameDate, formatter);
        final Game game = new Game();
        game.setId(getId());
        game.setVersion(getVersion());
        game.setGameDate(gameDate);
        game.setGoalsTeam1(goalsTeam1);
        game.setGoalsTeam2(goalsTeam2);
        game.setTeam1(new Team(team1));
        game.setTeam2(new Team(team2));

        return game;
    }

    public String getDateTimePattern() {
        return DATE_TIME_PATTERN;
    }

    public boolean isFinished() {
        return (getId() != null) && (getGoalsTeam1() != null) && (getGoalsTeam2() != null);
    }
}
