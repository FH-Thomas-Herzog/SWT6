package at.fh.ooe.swt6.em.data.dao.api;

import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Tip;
import at.fh.ooe.swt6.em.model.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Thomas on 5/16/2016.
 */
public interface TipDao extends JpaRepository<Tip, Long> {

    Tip findByUserAndGameAndTipGoalsTeam1AndTipGoalsTeam2(User user,
                                                          Game game,
                                                          int goalsTeam1,
                                                          int goalsTeam2);
}
