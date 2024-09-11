package probeV.GameInfogg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import probeV.GameInfogg.domain.task.TaskTime;

@Repository
public interface TaskTimeRepository extends JpaRepository<TaskTime, Integer> {
}
