package probeV.GameInfogg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import probeV.GameInfogg.controller.task.dto.response.TaskListResponseDto;
import probeV.GameInfogg.domain.task.Task;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.ModeType;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByModeType(ModeType modeType);
    List<Task> findByEventType(EventType eventType);
    List<Task> findByModeTypeAndEventType(ModeType modeType, EventType eventType);
}
