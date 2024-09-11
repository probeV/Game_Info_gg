package probeV.GameInfogg.domain.task;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Table(name = "TASKS")
@Entity
public class Task {
    /* PK */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskId;

    /* Relation */
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<TaskCategory> taskCategories = new ArrayList<TaskCategory>();

    /* Attribute */
    @Column(name = "name")
    private String name;

    @Builder
    public Task(String name) {
        this.name = name;
    }
}
