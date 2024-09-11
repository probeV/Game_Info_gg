package probeV.GameInfogg.domain.task;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.TaskType;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Table(name = "TASKS_CATEGORYS")
@Entity
public class TaskCategory {
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_category_id")
    private Integer taskCategoryId;

    /* FK */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    /* Relation */
    @OneToMany(mappedBy = "taskCategory", fetch = FetchType.LAZY)
    private List<TaskTime> taskTimes = new ArrayList<TaskTime>();

    /* Attribute */
    @Column(name = "mode_type")
    @Enumerated(EnumType.STRING)
    private ModeType modeType;

    @Column(name = "frequency_type")
    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType;

    @Column(name = "task_type")
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Builder
    public TaskCategory(ModeType modeType, FrequencyType frequencyType, TaskType taskType) {
        this.modeType = modeType;
        this.frequencyType = frequencyType;
        this.taskType = taskType;
    }

    public void setTask(Task task){
        this.task = task;
        task.getTaskCategories().add(this);
    }
}
