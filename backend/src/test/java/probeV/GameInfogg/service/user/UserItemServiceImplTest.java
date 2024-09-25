package probeV.GameInfogg.service.user;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import probeV.GameInfogg.auth.SecurityUtil;
import probeV.GameInfogg.controller.user.dto.request.UserItemListDeleteRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserItemListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.user.dto.response.UserItemListResponseDto;
import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.domain.user.UserItem;
import probeV.GameInfogg.exception.item.ItemNotFoundException;
import probeV.GameInfogg.repository.user.UserItemRepository;

public class UserItemServiceImplTest {

    @Mock
    private UserItemRepository userItemRepository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private UserItemServiceImpl userItemService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "username", "password");
        when(securityUtil.getUser()).thenReturn(user);
    }

    @Test
    public void testGetUserItems() {
        // Given
        UserItem userItem1 = new UserItem(1L, user, LocalDateTime.now());
        UserItem userItem2 = new UserItem(2L, user, LocalDateTime.now());
        when(userItemRepository.findByUserId(user.getId())).thenReturn(Arrays.asList(userItem1, userItem2));

        // When
        List<UserItemListResponseDto> result = userItemService.getUserItems();

        // Then
        assertEquals(2, result.size());
        assertEquals(userItem1.getId(), result.get(0).getId());
        assertEquals(userItem2.getId(), result.get(1).getId());
    }

    @Test
    public void testSaveItems() {
        // Given
        UserItemListSaveorUpdateRequestDto dto1 = new UserItemListSaveorUpdateRequestDto(1L, "2023-10-10T10:00:00");
        UserItemListSaveorUpdateRequestDto dto2 = new UserItemListSaveorUpdateRequestDto(null, "2023-10-11T10:00:00");
        UserItem userItem1 = new UserItem(1L, user, LocalDateTime.now());
        when(userItemRepository.findByUserId(user.getId())).thenReturn(Arrays.asList(userItem1));

        // When
        userItemService.saveItems(Arrays.asList(dto1, dto2));

        // Then
        verify(userItemRepository, times(1)).save(any(UserItem.class));
    }

    @Test
    public void testDeleteItems() {
        // Given
        UserItemListDeleteRequestDto dto1 = new UserItemListDeleteRequestDto(1L);
        UserItem userItem1 = new UserItem(1L, user, LocalDateTime.now());
        UserItem userItem2 = new UserItem(2L, user, LocalDateTime.now());
        when(userItemRepository.findByUserId(user.getId())).thenReturn(Arrays.asList(userItem1, userItem2));

        // When
        userItemService.deleteItems(Arrays.asList(dto1));

        // Then
        verify(userItemRepository, times(1)).deleteById(2L);
    }

    @Test
    public void testSaveItems_ItemNotFound() {
        // Given
        UserItemListSaveorUpdateRequestDto dto = new UserItemListSaveorUpdateRequestDto(99L, "2023-10-10T10:00:00");
        when(userItemRepository.findByUserId(user.getId())).thenReturn(Arrays.asList());

        // When & Then
        assertThrows(ItemNotFoundException.class, () -> userItemService.saveItems(Arrays.asList(dto)));
    }
}
