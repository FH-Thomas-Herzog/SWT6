package at.fh.ooe.swt6.em.data.dao.api;

import at.fh.ooe.swt6.em.model.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Thomas on 5/16/2016.
 */
public interface UserDao extends JpaRepository<User, Long> {

    User findByEmailIgnoreCase(String email);

    @Query("SELECT DISTINCT user FROM User user WHERE size(user.tips) = 0")
    List<User> findByEmptyTips();
}
