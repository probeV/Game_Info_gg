package probeV.GameInfogg.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import probeV.GameInfogg.domain.task.TaskCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer> {
    
}


