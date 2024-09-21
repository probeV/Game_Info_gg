package probeV.GameInfogg.repository.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import probeV.GameInfogg.domain.task.UserTask;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {

    // User Task 필터링
    List<UserTask> findByUserId(Long userId);
    List<UserTask> findByUserIdAndModeType(Long userId, ModeType modeType);
    List<UserTask> findByUserIdAndEventType(Long userId, EventType eventType);
    List<UserTask> findByUserIdAndModeTypeAndEventType(Long userId, ModeType modeType, EventType eventType);

}