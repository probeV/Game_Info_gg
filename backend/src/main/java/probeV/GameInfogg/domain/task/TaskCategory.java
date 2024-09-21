package probeV.GameInfogg.domain.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.EventType;
import lombok.Builder;

@Getter @Setter
@NoArgsConstructor
@Table(name = "TASKS_CATEGORIES")
@Entity
public class TaskCategory {
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /* Relation */
    @Column(name = "user_id", nullable = false)
    @OneToMany(mappedBy = "taskCategory")
    private List<UserTask> userTasks = new ArrayList<>();

    /* Attribute */
    @Column(name = "mode_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModeType modeType;

    @Column(name = "frequency_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Builder
    public TaskCategory(ModeType modeType, FrequencyType frequencyType, EventType eventType){
        this.modeType = modeType;
        this.frequencyType = frequencyType;
        this.eventType = eventType;
    }

    public void update(EventType eventType){
        this.eventType = eventType;
    }
}



