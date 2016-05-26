package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Tip;
import at.fh.ooe.swt6.em.model.jpa.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by Thomas on 5/22/2016.
 */
@Data
@NoArgsConstructor
public class TipEditModel extends AbstractEditModel<Long, Tip> {

    @NotNull
    @Min(0)
    @Max(100)
    private Integer goalsTeam1;
    @NotNull
    @Min(0)
    @Max(100)
    private Integer goalsTeam2;
    @NotNull
    @Length(min = 5, max = 100)
    @Email
    private String email;
    @NotNull
    @Min(0)
    private Long gameId;

    private String team1Name;
    private String team2Name;
    private LocalDateTime gameDate;

    @Override
    public void fromEntity(Tip entity) {
        setEntity(entity);
        gameId = entity.getGame().getId();
        gameDate = entity.getGame().getGameDate();
        goalsTeam1 = entity.getTipGoalsTeam1();
        goalsTeam2 = entity.getTipGoalsTeam2();
        team1Name = entity.getGame().getTeam1().getName();
        team2Name = entity.getGame().getTeam2().getName();
        if (entity.getUser() != null) {
            email = entity.getUser().getEmail();
        }
    }

    @Override
    public Tip toEntity() {
        final Tip tip = new Tip();
        tip.setId(getId());
        tip.setVersion(getVersion());
        tip.setTipGoalsTeam1(goalsTeam1);
        tip.setTipGoalsTeam2(goalsTeam2);
        tip.setUser(new User(email));
        tip.setGame(new Game(gameId));

        return tip;
    }
}
