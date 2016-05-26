package at.fh.ooe.swt6.em.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.TipDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.TipLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Tip;
import at.fh.ooe.swt6.em.model.jpa.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Objects;

/**
 * Replace @Transactional and there is no spring dependency anymore.
 * <p>
 * Created by Thomas on 5/16/2016.
 */
@Named
@Transactional(propagation = Propagation.REQUIRED)
public class TipLogicImpl implements TipLogic {

    @Inject
    private UserDao userDao;
    @Inject
    private GameDao gameDao;
    @Inject
    private TipDao tipDao;

    @Override
    public Tip save(Tip tip) {
        Objects.requireNonNull(tip, "Tip must not be null");
        Objects.requireNonNull(tip.getGame(), "Tip#game must not be null");
        Objects.requireNonNull(tip.getGame().getId(), "Tip#game#id must not be null");
        Objects.requireNonNull(tip.getUser(), "Tip#user must not be null");
        Objects.requireNonNull(tip.getUser().getEmail(), "Tip#user#email must not be null");

        if (tip.getId() != null) {
            throw new RuntimeException("Tip cannot be updated");
        }

        final Game game = gameDao.findOne(tip.getGame().getId());
        if (game == null) {
            throw new RuntimeException("Game tipped for not found");
        }
        User user = userDao.findByEmailIgnoreCase(tip.getUser().getEmail());
        if (user == null) {
            user = userDao.save(tip.getUser());
        }
        tip.setGame(game);
        tip.setUser(user);

        final Tip tipDB = tipDao.save(tip);
        user.getTips().add(tip);

        return tipDB;
    }

    @Override
    public void delete(long id) {
        final Tip tip = tipDao.findOne(id);
        if (tip == null) {
            throw new RuntimeException("Tip could not be found");
        }
        tipDao.delete(tip);

        // Cleanup user with no tips
        final List<User> users = userDao.findByEmptyTips();
        if (!users.isEmpty()) {
            userDao.delete(users);
        }
    }
}
