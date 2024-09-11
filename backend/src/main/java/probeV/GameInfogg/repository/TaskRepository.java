package probeV.GameInfogg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import probeV.GameInfogg.domain.task.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
