package probeV.GameInfogg.service.tast;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import probeV.GameInfogg.controller.task.dto.response.TaskListResponseDto;
import probeV.GameInfogg.domain.task.Task;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.repository.TaskRepository;

import java.time.DayOfWeek;
import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
class TaskServiceImplTest {

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    Task task;


    @BeforeEach
    void add(){
        /**/
        log.debug("debug : task 저장");
        task = Task.builder()
                .name("일일 필드 이벤트(3회)")
                .modeType(ModeType.PVE)
                .frequencyType(FrequencyType.DAILY)
                .eventType(EventType.NORMAL)
                .build();
        taskRepository.save(task);

        /**/
        log.debug("debug : task 저장");
        task = Task.builder()
                .name("필드 안개 이벤트(3회)")
                .modeType(ModeType.PVE)
                .frequencyType(FrequencyType.DAILY)
                .eventType(EventType.NORMAL)
                .build();
        taskRepository.save(task);

        /**/
        log.debug("debug : task 저장");
        task = Task.builder()
                .name("영지 정화")
                .modeType(ModeType.PVE)
                .frequencyType(FrequencyType.WEEKLY)
                .eventType(EventType.NORMAL)
                .build();
        taskRepository.save(task);

        /**/
        log.debug("debug : task 저장");
        task = Task.builder()
                .name("01:00 파밍")
                .modeType(ModeType.PVE)
                .frequencyType(FrequencyType.DAILY)
                .eventType(EventType.TIME)
                .dayOfWeek(null)
                .time("01:00")
                .build();
        taskRepository.save(task);

        /**/
        log.debug("debug : task 저장");
        task = Task.builder()
                .name("화요일 16:00 포자쟁탈")
                .modeType(ModeType.PVP)
                .frequencyType(FrequencyType.WEEKLY)
                .eventType(EventType.TIME)
                .dayOfWeek(DayOfWeek.TUESDAY)
                .time("16:00")
                .build();
        taskRepository.save(task);
    }

    @AfterEach
    void clear(){
        log.debug("debug : db clear");
        taskRepository.deleteAll();
    }

    @Test
    void 숙제_전체_조회(){
        log.debug("debug : 숙제 조회");

        //given

        //when
        List<TaskListResponseDto> responseDto = taskService.getAllTaskList();

        //then
        Assertions.assertThat(responseDto.get(0).getName()).isEqualTo("일일 필드 이벤트(3회)");
    }

    @Test
    void 숙제_모드_조회(){
        log.debug("debug : 숙제 모드 조회");

        //given

        //when
        List<TaskListResponseDto> responseDto = taskService.getFilteredByModeTaskList("pve");

        //then
        Assertions.assertThat(responseDto.get(0).getName()).isEqualTo("일일 필드 이벤트(3회)");
        Assertions.assertThat(responseDto.size()).isEqualTo(4);
    }

}