package at.fh.ooe.swt6.em.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.TipDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.TipLogic;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

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
}
