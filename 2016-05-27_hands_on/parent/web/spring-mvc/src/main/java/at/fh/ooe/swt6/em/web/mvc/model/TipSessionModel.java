package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.model.Tip;
import at.fh.ooe.swt6.em.model.view.team.TipView;
import lombok.NoArgsConstructor;

import java.util.Comparator;

/**
 * Created by Thomas on 5/23/2016.
 */
@NoArgsConstructor
public class TipSessionModel extends SessionModel<Long, Tip, TipView> {

    private static final Comparator<TipView> TIP_COMPARATOR = (o1, o2) -> {
        int result = 0;
        if (o1.getGoalsTeam1() == null) {
            Integer highGoalsO1 = (o1.getGoalsTeam1() > o1.getGoalsTeam2()) ? o1.getGoalsTeam1() : o1.getGoalsTeam2();
            Integer highGoalsO2 = (o1.getGoalsTeam1() > o2.getGoalsTeam2()) ? o2.getGoalsTeam1() : o2.getGoalsTeam2();
            if ((result = highGoalsO1.compareTo(highGoalsO2)) == 0) {
                // If one name set both are, if not bot are not
                if ((result = o1.getTeam1Name().compareTo(o2.getTeam1Name())) == 0) {
                    if ((result = o1.getTeam2Name().compareTo(o2.getTeam2Name())) == 0) {
                        result = o1.getId().compareTo(o2.getId());
                    }
                }
            }
        } else {
            result = o1.getId().compareTo(o2.getId());
        }

        return result;
    };

    @Override
    public TipView createViewFromEntity(Tip entity) {
        final TipView view = new TipView(entity.getId(), entity.getVersion());
        view.setTeam1Name(entity.getGame().getTeam1().getName());
        view.setTeam2Name(entity.getGame().getTeam2().getName());
        view.setGoalsTeam1(entity.getTipGoalsTeam1());
        view.setGoalsTeam2(entity.getTipGoalsTeam2());
        view.setGameDate(entity.getGame().getGameDate());

        return view;
    }

    @Override
    public TipView createViewFromId(Long id) {
        return new TipView(id);
    }

    @Override
    public Comparator<TipView> createComparator() {
        return TIP_COMPARATOR;
    }

    public TipSessionModel(String errorAction,
                           String method) {
        super(errorAction, method);
    }
}
