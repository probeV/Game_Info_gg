package probeV.GameInfogg.domain.task;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.EventType;



@Getter @Setter
@NoArgsConstructor
@Table(name = "DEFAULTS_TASKS")
@Entity
public class DefaultTask {
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

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

    @Builder
    public DefaultTask(String name, ModeType modeType, FrequencyType frequencyType, EventType eventType) {
        this.name = name;
        this.modeType = modeType;
        this.frequencyType = frequencyType;
        this.eventType = eventType;
    }

    public void update(String name, ModeType modeType, FrequencyType frequencyType, EventType eventType) {
        this.name = name;
        this.modeType = modeType;
        this.frequencyType = frequencyType;
        this.eventType = eventType;
    }
}
