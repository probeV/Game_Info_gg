package probeV.GameInfogg.domain.task;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.EventType;

import java.time.DayOfWeek;

@Getter @Setter
@NoArgsConstructor
@Table(name = "TASKS")
@Entity
public class Task {
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskId;

    /* Attribute */
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mode_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModeType modeType;

    @Column(name = "frequency_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "day_of_week", nullable = true)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "time", nullable = true)
    private String time;

    @Builder
    public Task(String name, ModeType modeType, FrequencyType frequencyType, EventType eventType, DayOfWeek dayOfWeek, String time) {
        this.name = name;
        this.modeType = modeType;
        this.frequencyType = frequencyType;
        this.eventType = eventType;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }
}
