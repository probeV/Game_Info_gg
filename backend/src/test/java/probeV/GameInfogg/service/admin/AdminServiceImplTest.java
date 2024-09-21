package probeV.GameInfogg.service.admin;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import probeV.GameInfogg.domain.task.DefaultTask;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.domain.user.constant.RoleType;
import probeV.GameInfogg.repository.task.DefaultTaskRepository;
import probeV.GameInfogg.repository.user.UserRepository;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListDeleteDto;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.admin.dto.response.UserPageResponseDto;
import probeV.GameInfogg.exception.task.TaskNotFoundException;
import probeV.GameInfogg.domain.task.constant.ModeType;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@Slf4j
@SpringBootTest
@Transactional
class AdminServiceImplTest {

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private DefaultTaskRepository defaultTaskRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        defaultTaskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void saveTasks_생성_성공() {
        // Given
        DefaultTaskListSaveorUpdateRequestDto dto1 = new DefaultTaskListSaveorUpdateRequestDto(null, "Task 1", ModeType.PVP.toString(), FrequencyType.DAILY.toString(), EventType.NORMAL.toString());
        DefaultTaskListSaveorUpdateRequestDto dto2 = new DefaultTaskListSaveorUpdateRequestDto(null, "Task 2", ModeType.PVE.toString(), FrequencyType.DAILY.toString(), EventType.NORMAL.toString());
        List<DefaultTaskListSaveorUpdateRequestDto> requestDto = Arrays.asList(dto1, dto2);

        // When
        adminService.saveTasks(requestDto);

        // Then
        List<DefaultTask> tasks = defaultTaskRepository.findAll();
        assertThat(2).isEqualTo(tasks.size());
        assertThat(tasks.get(0).getName()).isEqualTo("Task 1");
        assertThat(tasks.get(1).getName()).isEqualTo("Task 2");
    }

    @Test
    @Transactional
    public void saveTasks_수정_성공() {
        // Given
        DefaultTask task1 = DefaultTask.builder()
                .name("Task 0")
                .modeType(ModeType.PVP)
                .frequencyType(FrequencyType.DAILY)
                .eventType(EventType.NORMAL)
                .build();
        defaultTaskRepository.save(task1);

        DefaultTaskListSaveorUpdateRequestDto dto1 = new DefaultTaskListSaveorUpdateRequestDto(1, "Task 1", ModeType.PVE.toString(), FrequencyType.WEEKLY.toString(), EventType.TIME.toString());
        DefaultTaskListSaveorUpdateRequestDto dto2 = new DefaultTaskListSaveorUpdateRequestDto(null, "Task 2", ModeType.PVE.toString(), FrequencyType.DAILY.toString(), EventType.NORMAL.toString());
        List<DefaultTaskListSaveorUpdateRequestDto> requestDto = Arrays.asList(dto1, dto2);

        // When
        adminService.saveTasks(requestDto);

        // Then
        List<DefaultTask> tasks = defaultTaskRepository.findAll();
        assertThat(2).isEqualTo(tasks.size());
        assertThat(tasks.get(0).getName()).isEqualTo("Task 1");
        assertThat(tasks.get(1).getName()).isEqualTo("Task 2");
    }
    
    @Test
    @Transactional
    public void saveTasks_존재하지_않는_id_예외() {
        // Given
        DefaultTaskListSaveorUpdateRequestDto dto1 = new DefaultTaskListSaveorUpdateRequestDto(1, "Task 1", ModeType.PVE.toString(), FrequencyType.WEEKLY.toString(), EventType.TIME.toString());
        DefaultTaskListSaveorUpdateRequestDto dto2 = new DefaultTaskListSaveorUpdateRequestDto(2, "Task 2", ModeType.PVE.toString(), FrequencyType.DAILY.toString(), EventType.NORMAL.toString());
        List<DefaultTaskListSaveorUpdateRequestDto> requestDto = Arrays.asList(dto1, dto2);

        // When & Then
        assertThrows(TaskNotFoundException.class, () -> adminService.saveTasks(requestDto));
    }

    @Test
    @Transactional
    public void deleteTasks_삭제_성공() {
        // Given
        DefaultTask task1 = DefaultTask.builder()
                .name("Task 0")
                .modeType(ModeType.PVP)
                .frequencyType(FrequencyType.DAILY)
                .eventType(EventType.NORMAL)
                .build();
        defaultTaskRepository.save(task1);

        List<DefaultTaskListDeleteDto> requestDto = Arrays.asList();

        // When
        adminService.deleteTasks(requestDto);

        // Then

        List<DefaultTask> tasks = defaultTaskRepository.findAll();
        assertThat(0).isEqualTo(tasks.size());
    }


    @Test
    @Transactional
    void getUserList_성공() {
        // given

        User user1 = User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        User user2 = User.builder()
            .name("test2")
            .email("test2")
            .roleType(RoleType.USER)
            .provider("test2")
            .attributeCode("test2")
            .build();
        userRepository.save(user1);
        userRepository.save(user2);

        // when
        UserPageResponseDto result = adminService.getUserList(0);
        
        // then
        assertThat(result.getUserList()).hasSize(2);
        assertThat(result.getUserList().get(0).getName()).isEqualTo("test1");
        assertThat(result.getUserList().get(1).getName()).isEqualTo("test2");
    }
}