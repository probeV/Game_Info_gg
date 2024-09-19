package probeV.GameInfogg.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import probeV.GameInfogg.domain.task.DefaultTask;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.ModeType;

import java.util.List;

@Repository
public interface DefaultTaskRepository extends JpaRepository<DefaultTask, Integer> {
    List<DefaultTask> findByModeType(ModeType modeType);
    List<DefaultTask> findByEventType(EventType eventType);
    List<DefaultTask> findByModeTypeAndEventType(ModeType modeType, EventType eventType);
}
