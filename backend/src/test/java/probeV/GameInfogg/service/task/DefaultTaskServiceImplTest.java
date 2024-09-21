package probeV.GameInfogg.service.task;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import probeV.GameInfogg.controller.task.dto.response.DefaultTaskListResponseDto;
import probeV.GameInfogg.domain.task.DefaultTask;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.repository.task.DefaultTaskRepository;

import java.util.List;

@Slf4j
@SpringBootTest
@Transactional     
class DefaultTaskServiceImplTest {

    @Autowired
    DefaultTaskService defaultTaskService;

    @Autowired
    DefaultTaskRepository defaultTaskRepository;

    DefaultTask defaultTask;


    @BeforeEach
    void add(){
        /**/
        log.debug("debug : task 저장");
        defaultTask = DefaultTask.builder()
                .name("일일 필드 이벤트(3회)")
                .modeType(ModeType.PVE)
                .frequencyType(FrequencyType.DAILY)
                .eventType(EventType.NORMAL)
                .build();
        defaultTaskRepository.save(defaultTask);

        /**/
        log.debug("debug : task 저장");
        defaultTask = DefaultTask.builder()
                .name("필드 안개 이벤트(3회)")
                .modeType(ModeType.PVE)
                .frequencyType(FrequencyType.DAILY)
                .eventType(EventType.NORMAL)
                .build();
        defaultTaskRepository.save(defaultTask);

        /**/
        log.debug("debug : task 저장");
        defaultTask = DefaultTask.builder()
                .name("영지 정화")
                .modeType(ModeType.PVE)
                .frequencyType(FrequencyType.WEEKLY)
                .eventType(EventType.NORMAL)
                .build();
        defaultTaskRepository.save(defaultTask);

        /**/
        log.debug("debug : task 저장");
        defaultTask = DefaultTask.builder()
                .name("01:00 파밍")
                .modeType(ModeType.PVE)
                .frequencyType(FrequencyType.DAILY)
                .eventType(EventType.TIME)
                .build();
        defaultTaskRepository.save(defaultTask);

        /**/
        log.debug("debug : task 저장");
        defaultTask = DefaultTask.builder()
                .name("화요일 16:00 포자쟁탈")
                .modeType(ModeType.PVP)
                .frequencyType(FrequencyType.WEEKLY)
                .eventType(EventType.TIME)
                .build();
        defaultTaskRepository.save(defaultTask);
    }

    @AfterEach
    void clear(){
        log.debug("debug : db clear");
        defaultTaskRepository.deleteAll();
    }

    @Test
    void 숙제_전체_조회(){
        log.debug("debug : 숙제 조회");

         //given

         //when
        List<DefaultTaskListResponseDto> responseDto = defaultTaskService.getAllTaskList();

         //then
        Assertions.assertThat(responseDto.get(0).getName()).isEqualTo("일일 필드 이벤트(3회)");
    }

    @Test
    void 숙제_모드_조회(){
        log.debug("debug : 숙제 모드 조회");

         //given

         //when
        List<DefaultTaskListResponseDto> responseDto = defaultTaskService.getFilteredByModeTaskList("pve");

         //then
        Assertions.assertThat(responseDto.get(0).getName()).isEqualTo("일일 필드 이벤트(3회)");
        Assertions.assertThat(responseDto.size()).isEqualTo(4);
    }

}