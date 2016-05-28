package at.fh.ooe.swt6.em.data.dao.api;

import at.fh.ooe.swt6.em.model.jpa.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Thomas on 5/16/2016.
 */
public interface GameDao extends JpaRepository<Game, Long> {

    @Query("SELECT DISTINCT game FROM Game game LEFT OUTER JOIN FETCH game.tips ")
    List<Game> findAllFinishedGamesWithTips();

    List<Game> findAllByGameDate(LocalDateTime gameDate);
}
