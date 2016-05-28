package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.model.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.function.Predicate;

/**
 * Created by Thomas on 5/28/2016.
 */
@NoArgsConstructor
public class GamesFilterModel implements Serializable {

    public static final int FINISHED_VAL = 0;
    public static final int OPEN_VAL = 1;
    public static final int ALL_VAL = 2;

    @Getter
    @Setter
    @NotNull
    @Min(0)
    @Max(3)
    private int currentVal;

    public GamesFilterModel(int val) {
        this.currentVal = val;
    }

    public boolean isFinished() {
        return FINISHED_VAL == currentVal;
    }

    public boolean isOpen() {
        return OPEN_VAL == currentVal;
    }

    public boolean isAll() {
        return ALL_VAL == currentVal;
    }

    public int getFinishedVal() {
        return FINISHED_VAL;
    }

    public int getOpenVal() {
        return OPEN_VAL;
    }

    public int getAllVal() {
        return ALL_VAL;
    }

    public final Predicate<Game> getFilterPredicate() {
        return game -> {
            switch (currentVal) {
                case FINISHED_VAL:
                    return (game.getGoalsTeam1() != null) && (game.getGoalsTeam2() != null);
                case OPEN_VAL:
                    return (game.getGoalsTeam1() == null) && (game.getGoalsTeam2() == null);
                default:
                    return Boolean.TRUE;
            }
        };
    }
}
