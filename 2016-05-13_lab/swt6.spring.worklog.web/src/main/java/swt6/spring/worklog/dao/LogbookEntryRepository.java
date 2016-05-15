package swt6.spring.worklog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import swt6.spring.worklog.domain.LogbookEntry;

public interface LogbookEntryRepository extends JpaRepository<LogbookEntry, Long> {
  
}