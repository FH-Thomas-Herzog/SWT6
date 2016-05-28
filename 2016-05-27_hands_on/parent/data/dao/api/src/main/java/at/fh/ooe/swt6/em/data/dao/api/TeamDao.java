package at.fh.ooe.swt6.em.data.dao.api;

import at.fh.ooe.swt6.em.model.jpa.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Thomas on 5/16/2016.
 */
public interface TeamDao extends JpaRepository<Team, Long> {

    @Query("SELECT DISTINCT team FROM Team team " +
            " LEFT OUTER JOIN FETCH team.gamesAsTeam1 "
            + " LEFT OUTER JOIN FETCH team.gamesAsTeam2 "
            + " ORDER BY team.name ASC ")
    List<Team> findAllWithAllGamesByOrderByNameAsc();

    List<Team> findAllByOrderByNameAsc();

    List<Team> findAllByNameIgnoreCase(String name);
}
