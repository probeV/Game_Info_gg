package probeV.GameInfogg.service.admin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import probeV.GameInfogg.domain.task.DefaultTask;
import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.repository.task.DefaultTaskRepository;
import probeV.GameInfogg.repository.user.UserRepository;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskSaveRequestDto;
import probeV.GameInfogg.controller.admin.dto.request.DefaultTaskUpdateDto;
import probeV.GameInfogg.controller.admin.dto.response.UserListResponseDto;
import probeV.GameInfogg.exception.task.TaskNotFoundException;

class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private DefaultTaskRepository defaultTaskRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_성공() {
        // given
        DefaultTaskSaveRequestDto requestDto = new DefaultTaskSaveRequestDto("테스트 태스크", 1, "12:00");
        
        // when
        adminService.createTask("DAILY", "WEEKLY", "CHAOS_DUNGEON", requestDto);
        
        // then
        verify(defaultTaskRepository, times(1)).save(any(DefaultTask.class));
    }

    @Test
    void updateTask_성공() {
        // given
        Integer taskId = 1;
        DefaultTask existingTask = new DefaultTask();
        when(defaultTaskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        
        DefaultTaskUpdateDto updateDto = new DefaultTaskUpdateDto("수정된 태스크", 2, "13:00");
        
        // when
        adminService.updateTask(taskId, "DAILY", "WEEKLY", "CHAOS_DUNGEON", updateDto);
        
        // then
        verify(defaultTaskRepository, times(1)).findById(taskId);
        assertEquals("수정된 태스크", existingTask.getName());
    }

    @Test
    void updateTask_태스크없음_예외발생() {
        // given
        Integer taskId = 1;
        when(defaultTaskRepository.findById(taskId)).thenReturn(Optional.empty());
        
        DefaultTaskUpdateDto updateDto = new DefaultTaskUpdateDto("수정된 태스크", 2, "13:00");
        
        // when & then
        assertThrows(TaskNotFoundException.class, () -> 
            adminService.updateTask(taskId, "DAILY", "WEEKLY", "CHAOS_DUNGEON", updateDto));
    }

    @Test
    void deleteTask_성공() {
        // given
        Integer taskId = 1;
        DefaultTask existingTask = new DefaultTask();
        when(defaultTaskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        
        // when
        adminService.deleteTask(taskId);
        
        // then
        verify(defaultTaskRepository, times(1)).delete(existingTask);
    }

    @Test
    void deleteTask_태스크없음_예외발생() {
        // given
        Integer taskId = 1;
        when(defaultTaskRepository.findById(taskId)).thenReturn(Optional.empty());
        
        // when & then
        assertThrows(TaskNotFoundException.class, () -> adminService.deleteTask(taskId));
    }

    @Test
    void getUserList_성공() {
        // given
        List<User> users = List.of(new User(), new User());
        Page<User> userPage = new PageImpl<>(users);
        Pageable pageable = mock(Pageable.class);
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        
        // when
        List<UserListResponseDto> result = adminService.getUserList(pageable);
        
        // then
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll(pageable);
    }
}