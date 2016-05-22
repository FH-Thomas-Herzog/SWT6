package at.fh.ooe.swt6.em.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.logic.api.TeamLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Team;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Thomas on 5/19/2016.
 */
@Named
public class TeamLogicImpl implements TeamLogic {

    @Inject
    private TeamDao teamDao;

    @Override
    public Team save(String name) {
        Objects.requireNonNull(name, "A team must have a name");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("String name is empty");
        }

        return teamDao.save(new Team(name));
    }

    @Override
    public List<Team> save(String... names) {
        Objects.requireNonNull(names, "No names are given");
        if (names.length == 0) {
            throw new IllegalArgumentException("No names are given");
        }

        final List<Team> teams = new ArrayList<>(names.length);
        for (String name : names) {
            teams.add(save(name));
        }

        return teams;
    }
}
