package at.fh.ooe.swt6.em.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Team;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Replace @Transactional and there is no spring dependency anymore.
 * <p>
 * Created by Thomas on 5/16/2016.
 */
@Named
public class GameLogicImpl implements GameLogic {

    @Inject
    private GameDao gameDao;
    @Inject
    private UserDao userDao;
    @Inject
    private TeamDao teamDao;


    @Override
    public List<Game> findAllGames() {
        return gameDao.findAll();
    }

    @Override
    public Team saveTeam(Team team){
        return teamDao.save(team);
    }
}
