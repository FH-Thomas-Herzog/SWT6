package at.fh.ooe.swt6.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Replace @Transactional and there is no spring dependency anymore.
 * <p>
 * Created by Thomas on 5/16/2016.
 */
@Named
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GameLogicImpl implements GameLogic {

    @Inject
    private UserDao userDao;
    @Inject
    private GameDao gameDao;

    @Override
    public List<Game> findAllGames() {
        return gameDao.findAll();
    }
}
