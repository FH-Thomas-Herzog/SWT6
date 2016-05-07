package swt6.spring.worklog.dao.jpa.repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swt6.spring.worklog.domain.LogbookEntry;

/**
 * Created by Thomas on 5/7/2016.
 */
@Repository
public interface LogbookEntryReporitory extends JpaRepository<LogbookEntry, Long> {

}
