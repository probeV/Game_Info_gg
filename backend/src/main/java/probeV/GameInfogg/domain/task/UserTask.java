package probeV.GameInfogg.domain.task;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Setter;
import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;



@Getter @Setter
@NoArgsConstructor
@Table(name = "USERS_TASKS")
@Entity
public class UserTask{
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /* Relation */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
    
    @Column(name = "reset_dayofweek", nullable = true)
    @Enumerated(EnumType.STRING)
    private DayOfWeek resetDayOfWeek;

    @Column(name = "reset_time", nullable = true)
    private LocalTime resetTime;

    @Column(name = "sort_priority", nullable = true)
    private Integer sortPriority;
    
    @Builder
    public UserTask(String name, ModeType modeType, FrequencyType frequencyType, EventType eventType, DayOfWeek resetDayOfWeek, LocalTime resetTime, Integer sortPriority){
        this.name = name;
        this.modeType = modeType;
        this.frequencyType = frequencyType;
        this.eventType = eventType;
        this.resetDayOfWeek = resetDayOfWeek;
        this.resetTime = resetTime;
        this.sortPriority = sortPriority;
    }

    public void update(String name, DayOfWeek resetDayOfWeek, LocalTime resetTime, Integer sortPriority, EventType eventType){
        this.name = name;
        this.eventType = eventType;
        this.resetDayOfWeek = resetDayOfWeek;
        this.resetTime = resetTime;
        this.sortPriority = sortPriority;
    }

    public void setUser(User user){
        this.user = user;
        user.getUserTasks().add(this);
    }
}




