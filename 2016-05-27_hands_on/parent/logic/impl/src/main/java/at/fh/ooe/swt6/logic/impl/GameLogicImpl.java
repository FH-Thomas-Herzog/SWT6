package at.fh.ooe.swt6.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Thomas on 5/16/2016.
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED)
public class GameLogicImpl implements GameLogic {

    @Autowired
    private UserDao userDao;
    @Autowired
    private GameDao gameDao;

    @Override
    public List<Game> findAllGames() {
        return gameDao.findAll();
    }
}
