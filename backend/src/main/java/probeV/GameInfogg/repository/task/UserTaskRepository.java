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
    @Query("SELECT UT FROM UserTask UT JOIN FETCH UT.taskCategory WHERE UT.user.id = :userId")
    List<UserTask> findByUserId(Long userId);

    @Query("SELECT ut FROM UserTask ut JOIN FETCH ut.taskCategory tc WHERE ut.user.id = :userId AND tc.modeType = :modeType")
    List<UserTask> findByUserIdWithModeType(Long userId, ModeType modeType);

    @Query("SELECT ut FROM UserTask ut JOIN FETCH ut.taskCategory tc WHERE ut.user.id = :userId AND tc.eventType = :eventType")
    List<UserTask> findByUserIdWithEventType(Long userId, EventType eventType);

    @Query("SELECT ut FROM UserTask ut JOIN FETCH ut.taskCategory tc WHERE ut.user.id = :userId AND tc.modeType = :modeType AND tc.eventType = :eventType")
    List<UserTask> findByUserIdWithModeTypeAndEventType(Long userId, ModeType modeType, EventType eventType);

}