package probeV.GameInfogg.service.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import probeV.GameInfogg.controller.user.dto.request.UserItemListDeleteRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserItemListSaveRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserItemListUpdateRequestDto;
import probeV.GameInfogg.controller.user.dto.response.UserItemListResponseDto;
import probeV.GameInfogg.domain.item.Item;
import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.domain.user.UserItem;
import probeV.GameInfogg.domain.user.constant.RoleType;
import probeV.GameInfogg.exception.item.ItemNotFoundException;
import probeV.GameInfogg.repository.item.ItemRepository;
import probeV.GameInfogg.repository.user.UserItemRepository;
import probeV.GameInfogg.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;



@Slf4j
@SpringBootTest
@Transactional
public class UserItemServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserItemRepository userItemRepository;

    @Autowired
    private UserItemServiceImpl userItemService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Mock UserDetails
        UserDetails userDetails =  org.springframework.security.core.userdetails.User.withUsername("test1")
                                        .password("password")
                                        .roles("USER")
                                        .build();
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    public void getUserItems_조회_성공() {
        // Given
        User user = User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        Item item1 = Item.builder()
            .name("Item1")
            .effect("Effect1")
            .description("Description1")
            .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
            .name("Item2")
            .effect("Effect2")
            .description("Description2")
            .build();
        itemRepository.save(item2);

        UserItem userItem1 = UserItem.builder()
            .restTime(LocalDateTime.now())
            .build();
        userItem1.setUser(user);
        userItem1.setItem(item1);
        userItemRepository.save(userItem1);

        UserItem userItem2 = UserItem.builder()
            .restTime(LocalDateTime.now())
            .build();
        userItem2.setUser(user);
        userItem2.setItem(item2);
        userItemRepository.save(userItem2);

        // When
        List<UserItemListResponseDto> result = userItemService.getUserItems();

        // Then
        assertEquals(2, result.size());
        assertEquals(userItem1.getId(), result.get(0).getUserItemId());
        assertEquals(userItem2.getId(), result.get(1).getUserItemId());
    }

    @Test
    public void SaveItems_생성_성공() {
        // Given
        User user = User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        Item item1 = Item.builder()
            .name("Item1")
            .effect("Effect1")
            .description("Description1")
            .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
            .name("Item2")
            .effect("Effect2")
            .description("Description2")
            .build();
        itemRepository.save(item2);

        UserItem userItem1 = UserItem.builder()
            .restTime(LocalDateTime.now())
            .build();
        userItem1.setUser(user);
        userItem1.setItem(item1);
        userItemRepository.save(userItem1);

        UserItemListSaveRequestDto dto2 = new UserItemListSaveRequestDto(item2.getId(), "2023-10-10T10:00");
        List<UserItemListSaveRequestDto> requestDto = Arrays.asList(dto2);

        // When
        userItemService.saveItems(requestDto);

        // Then
        assertEquals(2, userItemRepository.findByUserId(user.getId()).size());
        assertEquals(userItem1.getId(), userItemRepository.findByUserId(user.getId()).get(0).getId());
        assertEquals(dto2.getItemId(), userItemRepository.findByUserId(user.getId()).get(1).getItem().getId());
        assertEquals(LocalDateTime.parse(dto2.getResetTime()), userItemRepository.findByUserId(user.getId()).get(1).getResetTime());
    }

    @Test
    public void SaveItems_오류_아이템없음() {
        // Given
        User user = User.builder()
                .name("test1")
                .email("test1")
                .roleType(RoleType.USER)
                .provider("test1")
                .attributeCode("test1")
                .build();
        userRepository.save(user);

        Item item1 = Item.builder()
                .name("Item1")
                .effect("Effect1")
                .description("Description1")
                .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
                .name("Item2")
                .effect("Effect2")
                .description("Description2")
                .build();
        itemRepository.save(item2);

        UserItem userItem1 = UserItem.builder()
                .restTime(LocalDateTime.now())
                .build();
        userItem1.setUser(user);
        userItem1.setItem(item1);
        userItemRepository.save(userItem1);

        UserItem userItem2 = UserItem.builder()
                .restTime(LocalDateTime.now())
                .build();
        userItem2.setUser(user);
        userItem2.setItem(item2);
        userItemRepository.save(userItem2);

        UserItemListSaveRequestDto dto = new UserItemListSaveRequestDto(99L, "2023-10-10T10:00:00");

        // When & Then
        assertThrows(ItemNotFoundException.class, () -> userItemService.saveItems(List.of(dto)));
    }

    @Test
    public void updateItems_수정_성공() {
        // Given
        User user = User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        Item item1 = Item.builder()
            .name("Item1")
            .effect("Effect1")
            .description("Description1")
            .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
            .name("Item2")
            .effect("Effect2")
            .description("Description2")
            .build();
        itemRepository.save(item2);

        UserItem userItem1 = UserItem.builder()
            .restTime(LocalDateTime.now())
            .build();
        userItem1.setUser(user);
        userItem1.setItem(item1);
        userItemRepository.save(userItem1);

        UserItem userItem2 = UserItem.builder()
            .restTime(LocalDateTime.now())
            .build();
        userItem2.setUser(user);
        userItem2.setItem(item2);
        userItemRepository.save(userItem2);

        UserItemListUpdateRequestDto dto1 = new UserItemListUpdateRequestDto(item1.getId(), "2023-10-10T10:00");
        UserItemListUpdateRequestDto dto2 = new UserItemListUpdateRequestDto(item2.getId(), "2023-10-10T10:00");
        List<UserItemListUpdateRequestDto> requestDto = Arrays.asList(dto1, dto2);
    
        // When
        userItemService.updateItems(requestDto);

        // Then
        assertEquals(dto1.getUserItemId(), userItemRepository.findByUserId(user.getId()).get(0).getId());
        assertEquals(dto2.getUserItemId(), userItemRepository.findByUserId(user.getId()).get(1).getId());
        assertEquals(LocalDateTime.parse(dto1.getResetTime()), userItemRepository.findByUserId(user.getId()).get(0).getResetTime());
        assertEquals(LocalDateTime.parse(dto2.getResetTime()), userItemRepository.findByUserId(user.getId()).get(1).getResetTime());
    }

    @Test
    public void updateItems_오류_아이템없음() {
        // Given
        User user = User.builder()
                .name("test1")
                .email("test1")
                .roleType(RoleType.USER)
                .provider("test1")
                .attributeCode("test1")
                .build();
        userRepository.save(user);

        Item item1 = Item.builder()
                .name("Item1")
                .effect("Effect1")
                .description("Description1")
                .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
                .name("Item2")
                .effect("Effect2")
                .description("Description2")
                .build();
        itemRepository.save(item2);

        UserItem userItem1 = UserItem.builder()
                .restTime(LocalDateTime.now())
                .build();
        userItem1.setUser(user);
        userItem1.setItem(item1);
        userItemRepository.save(userItem1);

        UserItem userItem2 = UserItem.builder()
                .restTime(LocalDateTime.now())
                .build();
        userItem2.setUser(user);
        userItem2.setItem(item2);
        userItemRepository.save(userItem2);

        UserItemListUpdateRequestDto dto = new UserItemListUpdateRequestDto(99L, "2023-10-10T10:00:00");

        // When & Then
        assertThrows(ItemNotFoundException.class, () -> userItemService.updateItems(List.of(dto)));
    }

    @Test
    public void DeleteItems_전체_삭제_성공() {
        // Given
        User user = User.builder()
                .name("test1")
                .email("test1")
                .roleType(RoleType.USER)
                .provider("test1")
                .attributeCode("test1")
                .build();
        userRepository.save(user);

        Item item1 = Item.builder()
                .name("Item1")
                .effect("Effect1")
                .description("Description1")
                .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
                .name("Item2")
                .effect("Effect2")
                .description("Description2")
                .build();
        itemRepository.save(item2);

        UserItem userItem1 = UserItem.builder()
                .restTime(LocalDateTime.now())
                .build();
        userItem1.setUser(user);
        userItem1.setItem(item1);
        userItemRepository.save(userItem1);

        UserItem userItem2 = UserItem.builder()
                .restTime(LocalDateTime.now())
                .build();
        userItem2.setUser(user);
        userItem2.setItem(item2);
        userItemRepository.save(userItem2);

        // When
        userItemService.deleteItems(List.of());

        // Then
        assertEquals(0, userItemRepository.findByUserId(user.getId()).size());
    }

    @Test
    public void DeleteItems_부분_삭제_성공() {
        // Given
        User user = User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        Item item1 = Item.builder()
            .name("Item1")
            .effect("Effect1")
            .description("Description1")
            .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
            .name("Item2")
            .effect("Effect2")
            .description("Description2")
            .build();
        itemRepository.save(item2);

        UserItem userItem1 = UserItem.builder()
            .restTime(LocalDateTime.now())
            .build();
        userItem1.setUser(user);
        userItem1.setItem(item1);
        userItemRepository.save(userItem1);

        UserItem userItem2 = UserItem.builder()
            .restTime(LocalDateTime.now())
            .build();
        userItem2.setUser(user);
        userItem2.setItem(item2);
        userItemRepository.save(userItem2);

        UserItemListDeleteRequestDto dto1 = new UserItemListDeleteRequestDto(userItem1.getId());

        // When
        userItemService.deleteItems(List.of(dto1));

        // Then
        List<UserItem> userItems = userItemRepository.findByUserId(user.getId());
        assertEquals(1 , userItems.size());
        assertEquals(userItem1.getItem().getId(), userItems.get(0).getItem().getId());

    }

    @Test
    public void DeleteItems_오류_아이템없음() {
        // Given
        User user = User.builder()
            .name("test1")
            .email("test1")
            .roleType(RoleType.USER)
            .provider("test1")
            .attributeCode("test1")
            .build();
        userRepository.save(user);

        Item item1 = Item.builder() 
            .name("Item1")
            .effect("Effect1")
            .description("Description1")
            .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
            .name("Item2")
            .effect("Effect2")
            .description("Description2")
            .build();
        itemRepository.save(item2);

        UserItem userItem1 = UserItem.builder()
            .restTime(LocalDateTime.now())
            .build();
        userItem1.setUser(user);
        userItem1.setItem(item1);
        userItemRepository.save(userItem1);

        UserItemListDeleteRequestDto dto = new UserItemListDeleteRequestDto(99L);

        // When

        // Then
        assertThrows(ItemNotFoundException.class, () -> userItemService.deleteItems(List.of(dto)));
    }
}
