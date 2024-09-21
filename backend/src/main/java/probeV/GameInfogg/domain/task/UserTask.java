package probeV.GameInfogg.domain.task;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Setter;
import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.domain.task.TaskCategory;




@Getter @Setter
@NoArgsConstructor
@Table(name = "USERS_TASKS")
@Entity
public class UserTask {
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /* Relation */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_category_id", nullable = false)
    private TaskCategory taskCategory;

    /* Attribute */
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "reset_dayofweek", nullable = true)
    private DayOfWeek resetDayOfWeek;

    @Column(name = "reset_time", nullable = true)
    private LocalTime resetTime;

    @Column(name = "sort_priority", nullable = true)
    private Integer sortPriority;
    


    @Builder
    public UserTask(String name, DayOfWeek resetDayOfWeek, LocalTime resetTime, Integer sortPriority, TaskCategory taskCategory){
        this.name = name;
        this.resetDayOfWeek = resetDayOfWeek;
        this.taskCategory = taskCategory;
        this.resetTime = resetTime;
        this.sortPriority = sortPriority;
    }

    public void update(String name, DayOfWeek resetDayOfWeek, LocalTime resetTime, Integer sortPriority){
        this.name = name;
        this.resetDayOfWeek = resetDayOfWeek;
        this.resetTime = resetTime;
        this.sortPriority = sortPriority;
    }



}

