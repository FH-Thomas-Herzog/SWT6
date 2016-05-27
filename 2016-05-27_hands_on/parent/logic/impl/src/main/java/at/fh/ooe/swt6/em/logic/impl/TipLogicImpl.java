package at.fh.ooe.swt6.em.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.TipDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.TipLogic;
import at.fh.ooe.swt6.em.logic.api.UserLogic;
import at.fh.ooe.swt6.em.logic.api.exception.LogicException;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Tip;
import at.fh.ooe.swt6.em.model.jpa.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;

/**
 * Replace @Transactional and there is no spring dependency anymore.
 * <p>
 * Created by Thomas on 5/16/2016.
 */
@Named
@Transactional(propagation = Propagation.REQUIRED)
public class TipLogicImpl implements TipLogic {

    //<editor-fold desc="Injections Dao/Logic">
    @Inject
    private UserDao userDao;
    @Inject
    private GameDao gameDao;
    @Inject
    private TipDao tipDao;
    @Inject
    private UserLogic userLogic;
    //</editor-fold>

    //<editor-fold desc="TipLogic Implemented Methods">
    @Override
    public Tip save(Tip tip) {
        Objects.requireNonNull(tip, "Tip must not be null");
        Objects.requireNonNull(tip.getGame(), "Tip#game must not be null");
        Objects.requireNonNull(tip.getGame().getId(), "Tip#game#id must not be null");
        Objects.requireNonNull(tip.getUser(), "Tip#user must not be null");
        Objects.requireNonNull(tip.getUser().getEmail(), "Tip#user#email must not be null");

        if (tip.getId() != null) {
            throw new LogicException("Tip cannot be updated", LogicException.ServiceCode.INVALID_ACTION);
        }

        // Validate game
        final Game game = gameDao.findOne(tip.getGame().getId());
        if (game == null) {
            throw new LogicException("Game tipped for not found", LogicException.ServiceCode.ENTITY_NOT_FOUND);
        }

        // User creation if necessary
        User user = userDao.findByEmailIgnoreCase(tip.getUser().getEmail());
        if (user == null) {
            user = userLogic.create(tip.getUser().getEmail());
        }

        // Check for already given tip
        Tip tipDB = tipDao.findByUserAndGameAndTipGoalsTeam1AndTipGoalsTeam2(user,
                                                                             game,
                                                                             tip.tipGoalsTeam1,
                                                                             tip.tipGoalsTeam2);
        if (tipDB != null) {
            throw new LogicException("A tip exists already for this game with the goals set",
                                     LogicException.ServiceCode.ENTITY_EXISTS);
        }

        tip.setGame(game);
        tip.setUser(user);

        tipDB = tipDao.save(tip);
        user.getTips().add(tip);

        return tipDB;
    }

    @Override
    public void delete(long id) {
        final Tip tip = tipDao.findOne(id);
        if (tip == null) {
            throw new LogicException("Tip could not be found", LogicException.ServiceCode.ENTITY_NOT_FOUND);
        }
        tipDao.delete(tip);
        userLogic.deleteUserWithNoTips();
    }
    //</editor-fold>
}
