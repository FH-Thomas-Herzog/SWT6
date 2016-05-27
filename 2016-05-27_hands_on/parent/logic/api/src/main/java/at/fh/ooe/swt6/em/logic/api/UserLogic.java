package at.fh.ooe.swt6.em.logic.api;

import at.fh.ooe.swt6.em.model.jpa.model.User;
import at.fh.ooe.swt6.em.model.view.team.UserScoreView;

import java.io.Serializable;
import java.util.List;

/**
 * The logic interface for the User data.
 * <p>
 * Created by Thomas on 5/27/2016.
 */
public interface UserLogic extends Serializable {

    /**
     * Creates a user with this email
     *
     * @param email the users email
     * @return the created user
     * @throws at.fh.ooe.swt6.em.logic.api.exception.LogicException if a user with this email already exists
     * @throws NullPointerException                                 if the email is null
     * @see at.fh.ooe.swt6.em.logic.api.exception.LogicException.ServiceCode#ENTITY_EXISTS
     */
    User create(String email);

    /**
     * Creates users for the given emails
     *
     * @param emails the users emails
     * @return the created users
     * @throws at.fh.ooe.swt6.em.logic.api.exception.LogicException if a user with a given email already exists
     * @throws NullPointerException                                 is emails is null or one of its contents
     * @see at.fh.ooe.swt6.em.logic.api.exception.LogicException.ServiceCode#ENTITY_EXISTS
     */
    List<User> create(String... emails);

    /**
     * Deletes all user which have no tips anymore
     *
     * @return the count of deleted users
     */
    int deleteUserWithNoTips();

    /**
     * Finds all users, calculates their scores and return the create UserScoreView models.
     *
     * @return the create view models
     * @see UserScoreView
     */
    List<UserScoreView> findAllUserScores();

}
