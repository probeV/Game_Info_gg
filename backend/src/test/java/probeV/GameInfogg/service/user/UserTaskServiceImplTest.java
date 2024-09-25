package probeV.GameInfogg.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import probeV.GameInfogg.controller.user.dto.request.UserTaskListDeleteRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserTaskListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.user.dto.response.UserTaskListResponseDto;
import probeV.GameInfogg.domain.user.UserTask;
import probeV.GameInfogg.domain.task.constant.EventType;
import probeV.GameInfogg.domain.task.constant.FrequencyType;
import probeV.GameInfogg.domain.task.constant.ModeType;
import probeV.GameInfogg.exception.task.TaskNotFoundException;
import probeV.GameInfogg.repository.user.UserTaskRepository;
import probeV.GameInfogg.repository.user.UserRepository;
import probeV.GameInfogg.domain.user.constant.RoleType;
import java.util.Arrays;

@Slf4j
@SpringBootTest
@Transactional
public class UserTaskServiceImplTest {

    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private UserTaskRepository userTaskRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @AfterEach
    public void tearDown() {
        userTaskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Mock UserDetails
        UserDetails userDetails = User.withUsername("test1")
                                        .password("password")
                                        .roles("USER")
                                        .build();
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    @Transactional
    public void getAllUserTaskList_조회_성공() {
        // given
        probeV.GameInfogg.domain.user.User user = probeV.GameInfogg.domain.user.User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        UserTask userTask = UserTask.builder()
            .name("test1")
            .modeType(ModeType.PVE)
            .frequencyType(FrequencyType.DAILY)
            .eventType(EventType.NORMAL)
            .build();
        userTask.setUser(user);
        userTaskRepository.save(userTask);

        UserTask userTask2 = UserTask.builder()
            .name("test2")
            .modeType(ModeType.PVE)
            .frequencyType(FrequencyType.DAILY)
            .eventType(EventType.NORMAL)
            .build();
        userTask2.setUser(user);
        userTaskRepository.save(userTask2);

        // when
        List<UserTaskListResponseDto> result = userTaskService.getAllUserTaskList();

        // then
        assertThat(result.get(0).getName()).isEqualTo("test1");
        assertThat(result.get(1).getName()).isEqualTo("test2");
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void getFilteredByModeUserTaskList_조회_성공() {
        // given
        probeV.GameInfogg.domain.user.User user = probeV.GameInfogg.domain.user.User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        UserTask userTask = UserTask.builder()
            .name("test1")
            .modeType(ModeType.PVP)
            .frequencyType(FrequencyType.DAILY)
            .eventType(EventType.NORMAL)
            .build();
        userTask.setUser(user);
        userTaskRepository.save(userTask);

        UserTask userTask2 = UserTask.builder()
            .name("test2")
            .modeType(ModeType.PVE)
            .frequencyType(FrequencyType.DAILY)
            .eventType(EventType.NORMAL)
            .build();
        userTask2.setUser(user);
        userTaskRepository.save(userTask2);

        // when
        List<UserTaskListResponseDto> result = userTaskService.getFilteredByModeUserTaskList("PVE");

        // then
        assertThat(result.get(0).getName()).isEqualTo("test2");
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void getFilteredByEventUserTaskList_조회_성공() {
        // given

        probeV.GameInfogg.domain.user.User user = probeV.GameInfogg.domain.user.User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);  

        UserTask userTask = UserTask.builder()
            .name("test1")
            .modeType(ModeType.PVE)
            .frequencyType(FrequencyType.DAILY)
            .eventType(EventType.SPECIAL)
            .build();
        userTask.setUser(user);
        userTaskRepository.save(userTask);

        UserTask userTask2 = UserTask.builder()
            .name("test2")
            .modeType(ModeType.PVE)
            .frequencyType(FrequencyType.DAILY)
            .eventType(EventType.NORMAL)
            .build();
        userTask2.setUser(user);
        userTaskRepository.save(userTask2);

        // when
        List<UserTaskListResponseDto> result = userTaskService.getFilteredByEventUserTaskList("SPECIAL");

        // then
        assertThat(result.get(0).getName()).isEqualTo("test1");
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void getFilteredByModeEventUserTaskList_조회_성공() {
        // given
        probeV.GameInfogg.domain.user.User user = probeV.GameInfogg.domain.user.User.builder()

            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        UserTask userTask = UserTask.builder()
            .name("test1")
            .modeType(ModeType.PVE)
            .frequencyType(FrequencyType.DAILY)
            .eventType(EventType.SPECIAL)
            .build();
        userTask.setUser(user);
        userTaskRepository.save(userTask);

        UserTask userTask2 = UserTask.builder()
            .name("test2")
            .modeType(ModeType.PVE)
            .frequencyType(FrequencyType.DAILY)
            .eventType(EventType.NORMAL)
            .build();
        userTask2.setUser(user);
        userTaskRepository.save(userTask2);
        
        // when
        List<UserTaskListResponseDto> result = userTaskService.getFilteredByModeEventUserTaskList("PVE", "SPECIAL");

        // then
        assertThat(result.get(0).getName()).isEqualTo("test1");
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void saveTasks_생성_성공() {
        // given
        probeV.GameInfogg.domain.user.User user = probeV.GameInfogg.domain.user.User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        UserTaskListSaveorUpdateRequestDto dto1 = new UserTaskListSaveorUpdateRequestDto(null, "Task 1", ModeType.PVP.toString(), FrequencyType.DAILY.toString(), EventType.NORMAL.toString(), null, null, null);
        UserTaskListSaveorUpdateRequestDto dto2 = new UserTaskListSaveorUpdateRequestDto(null, "Task 2", ModeType.PVE.toString(), FrequencyType.DAILY.toString(), EventType.NORMAL.toString(), null, null, null);
        List<UserTaskListSaveorUpdateRequestDto> requestDto = Arrays.asList(dto1, dto2);

        // when
        userTaskService.saveTasks(requestDto);

        // then
        List<UserTask> tasks = userTaskRepository.findAll();
        assertThat(tasks.get(0).getName()).isEqualTo("Task 1");
        assertThat(tasks.get(1).getName()).isEqualTo("Task 2");
        assertThat(tasks.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void saveTasks_수정_성공() {
        // given
        probeV.GameInfogg.domain.user.User user = probeV.GameInfogg.domain.user.User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);


        UserTask task1 = UserTask.builder()
            .name("Task 0")
            .modeType(ModeType.PVP)
            .frequencyType(FrequencyType.DAILY)
            .eventType(EventType.NORMAL)
            .build();
        task1.setUser(user);
        userTaskRepository.save(task1);
        
        UserTaskListSaveorUpdateRequestDto dto1 = new UserTaskListSaveorUpdateRequestDto(1L, "Task 1", ModeType.PVE.toString(), FrequencyType.WEEKLY.toString(), EventType.TIME.toString(), null, null, null);
        UserTaskListSaveorUpdateRequestDto dto2 = new UserTaskListSaveorUpdateRequestDto(null, "Task 2", ModeType.PVE.toString(), FrequencyType.DAILY.toString(), EventType.NORMAL.toString(), null, null, null);
        List<UserTaskListSaveorUpdateRequestDto> requestDto = Arrays.asList(dto1, dto2);

        // when
        userTaskService.saveTasks(requestDto);

        // then
        List<UserTask> tasks = userTaskRepository.findAll();
        assertThat(2).isEqualTo(tasks.size());
        assertThat(tasks.get(0).getName()).isEqualTo("Task 1");
        assertThat(tasks.get(1).getName()).isEqualTo("Task 2");
    }

    @Test
    @Transactional
    public void saveTasks_존재하지_않는_id_예외() {
        // given
        probeV.GameInfogg.domain.user.User user = probeV.GameInfogg.domain.user.User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        UserTaskListSaveorUpdateRequestDto dto1 = new UserTaskListSaveorUpdateRequestDto(1L, "Task 1", ModeType.PVE.toString(), FrequencyType.WEEKLY.toString(), EventType.TIME.toString(), null, null, null);
        UserTaskListSaveorUpdateRequestDto dto2 = new UserTaskListSaveorUpdateRequestDto(null, "Task 2", ModeType.PVE.toString(), FrequencyType.DAILY.toString(), EventType.NORMAL.toString(), null, null, null);

        List<UserTaskListSaveorUpdateRequestDto> requestDto = Arrays.asList(dto1, dto2);

        // when & then
        assertThrows(TaskNotFoundException.class, () -> userTaskService.saveTasks(requestDto));
    }

    @Test
    @Transactional
    public void deleteTasks_삭제_성공() {
        // given
        probeV.GameInfogg.domain.user.User user = probeV.GameInfogg.domain.user.User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        UserTask task1 = UserTask.builder()
            .name("Task 0")
            .modeType(ModeType.PVP)

            .frequencyType(FrequencyType.DAILY)
            .eventType(EventType.NORMAL)
            .build();
        task1.setUser(user);
        userTaskRepository.save(task1);
        
        List<UserTaskListDeleteRequestDto> requestDto = Arrays.asList();

        // when
        userTaskService.deleteTasks(requestDto);

        // then
        List<UserTask> tasks = userTaskRepository.findAll();
        assertThat(0).isEqualTo(tasks.size());
    }
}