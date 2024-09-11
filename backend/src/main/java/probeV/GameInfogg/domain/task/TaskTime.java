package probeV.GameInfogg.domain.task;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;

@Getter @Setter
@NoArgsConstructor
@Table(name = "TAKS_TIMES")
@Entity
public class TaskTime {
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_time_id")
    private Integer taskTimeId;

    /* FK */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_category_id")
    private TaskCategory taskCategory;

    /* Attribute */
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "time")
    private String time;

    @Builder
    public TaskTime(DayOfWeek dayOfWeek, String time) {
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }

    public void setTaskCategory(TaskCategory taskCategory){
        this.taskCategory = taskCategory;
        taskCategory.getTaskTimes().add(this);
    }
}
