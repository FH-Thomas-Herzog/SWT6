package at.fh.ooe.swt6.em.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.UserLogic;
import at.fh.ooe.swt6.em.logic.api.exception.LogicException;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Tip;
import at.fh.ooe.swt6.em.model.jpa.model.User;
import at.fh.ooe.swt6.em.model.view.team.UserScoreView;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 5/27/2016.
 */
@Named
@Transactional
public class UserLogicImpl implements UserLogic {

    //<editor-fold desc="Injections Dao/Logic">
    @Inject
    private UserDao userDao;
    @Inject
    private GameDao gameDao;
    //</editor-fold>

    //<editor-fold desc="Predicates">
    /**
     * Predicate for getting open tips which games result is not set yet
     */
    private static final Predicate<Tip> IS_TIP_OPEN = (tip) ->
            tip.getGame().getGoalsTeam1() == null || tip.getGame().getGoalsTeam2() == null;

    /**
     * Predicate for getting the won tips
     */
    private static final Predicate<Tip> IS_TIP_WIN = (tip) ->
            tip.getGame().getGoalsTeam1() == tip.getTipGoalsTeam1()
                    && tip.getGame().getGoalsTeam2() == tip.getTipGoalsTeam2();

    /**
     * Predicate for getting the lost tips
     */
    private static final Predicate<Tip> IS_TIP_LOS = (tip) ->
            tip.getGame().getGoalsTeam1() != tip.getTipGoalsTeam1()
                    || tip.getGame().getGoalsTeam2() != tip.getTipGoalsTeam2();
    //</editor-fold>

    //<editor-fold desc="UserLogic Implemented Methods">
    @Override
    public User create(String _email) {
        Objects.requireNonNull(_email, "User must have an email");
        final String email = _email.toLowerCase().trim();
        if (email.isEmpty()) {
            throw new LogicException("Email must not be empty", LogicException.ServiceCode.INVALID_DATA);
        }

        User userDb = userDao.findByEmailIgnoreCase(email);
        if (userDb != null) {
            throw new LogicException("User already exist with this email", LogicException.ServiceCode.ENTITY_EXISTS);
        }

        userDb = new User(email);

        return userDao.save(userDb);
    }

    @Override
    public List<User> create(String... emails) {
        Objects.requireNonNull(emails, "Users must have emails");
        return Arrays.stream(emails).map(email -> create(email)).collect(Collectors.toList());
    }

    @Override
    public int deleteUserWithNoTips() {
        // Cleanup user with no tips
        final List<User> users = userDao.findByEmptyTips();
        if (!users.isEmpty()) {
            userDao.delete(users);
        }

        return users.size();
    }

    @Override
    public List<UserScoreView> findAllUserScores() {
        final List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            return Collections.emptyList();
        }
        final List<Game> games = gameDao.findAllFinishedGamesWithTips();
        final Map<User, List<Tip>> userOpenTipsMap = games.stream()
                                                          .flatMap(game -> game.getTips().stream())
                                                          .filter(IS_TIP_OPEN)
                                                          .collect(Collectors.groupingBy(Tip::getUser));
        final Map<User, List<Tip>> userWonTipsMap = games.stream()
                                                         .flatMap(game -> game.getTips().stream())
                                                         .filter(IS_TIP_WIN)
                                                         .collect(Collectors.groupingBy(Tip::getUser));
        final Map<User, List<Tip>> userLostTipsMap = games.stream()
                                                          .flatMap(game -> game.getTips().stream())
                                                          .filter(IS_TIP_WIN)
                                                          .collect(Collectors.groupingBy(Tip::getUser));

        final List<UserScoreView> views = new ArrayList<>(users.size());
        for (User user : users) {
            final int openTips = (userOpenTipsMap.containsKey(user)) ? userOpenTipsMap.get(user).size() : 0;
            final int wonTips = (userWonTipsMap.containsKey(user)) ? userWonTipsMap.get(user).size() : 0;
            final int lostTips = (userLostTipsMap.containsKey(user)) ? userLostTipsMap.get(user).size() : 0;
            final int totalTips = openTips + wonTips + lostTips;
            final UserScoreView view = new UserScoreView(user.getEmail(), totalTips, openTips, wonTips, lostTips);
            view.setId(user.getId());
            view.setVersion(user.getVersion());

            views.add(view);
        }

        return views;
    }
    //</editor-fold>
}
