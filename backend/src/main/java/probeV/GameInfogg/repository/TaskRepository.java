package probeV.GameInfogg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import probeV.GameInfogg.controller.task.dto.response.TaskListResponseDto;
import probeV.GameInfogg.domain.task.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "SELECT T.* FROM TASKS T " +
            "JOIN (SELECT TC.*, TT.* " +
            "FROM TASKS_CATEGORYS TC " +
            "LEFT JOIN TAKS_TIMES TT ON TC.task_category_id = TT.task_category_id) AS TE " +
            "ON TE.task_id = T.task_id", nativeQuery = true)
    List<Task> findAllTaskWithDetails();
}
