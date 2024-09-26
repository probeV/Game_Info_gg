package probeV.GameInfogg.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import probeV.GameInfogg.controller.user.dto.request.UserItemListSaveRequestDto;
import probeV.GameInfogg.controller.user.dto.response.UserItemListResponseDto;
import probeV.GameInfogg.controller.user.dto.request.UserItemListUpdateRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserItemListDeleteRequestDto;
import probeV.GameInfogg.domain.item.Item;
import probeV.GameInfogg.repository.item.ItemRepository;
import probeV.GameInfogg.repository.user.UserItemRepository;
import probeV.GameInfogg.auth.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import probeV.GameInfogg.domain.user.User;
import probeV.GameInfogg.domain.user.UserItem;
import probeV.GameInfogg.exception.item.ItemNotFoundException;



@Slf4j
@RequiredArgsConstructor
@Service
public class UserItemServiceImpl implements UserItemService {

    private final UserItemRepository userItemRepository;    
    private final ItemRepository itemRepository;
    private final SecurityUtil securityUtil;

    @Override
    public List<UserItemListResponseDto> getUserItems() {
        // 사용자의 아이템 목록을 조회하는 로직을 구현합니다.
        log.info("getUserItems 내 아이템 조회");

        // 유저 찾기
        User user = securityUtil.getUser();

        return userItemRepository.findByUserId(user.getId()).stream()
            .map(UserItemListResponseDto::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveItems(List<UserItemListSaveRequestDto> requestDto) {
        // 사용자의 아이템 목록을 생성하는 로직을 구현합니다.
        log.info("saveItems 내 아이템 생성");

        // 유저 찾기
        User user = securityUtil.getUser();

        // 수정 및 생성 처리
        for (UserItemListSaveRequestDto dto : requestDto) {
            Long itemId = dto.getItemId();

            // 생성 로직
            log.info("user id: " + user.getId() + " itemId: " + itemId + " 생성 로직 호출");

            // Item 찾기
            Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + dto.getItemId()));

            // UserItem 생성
            UserItem userItem = dto.toEntity();
            userItem.setUser(user);
            userItem.setItem(item);
            userItemRepository.save(userItem);

        }
    }

    @Override
    @Transactional
    public void updateItems(List<UserItemListUpdateRequestDto> requestDto) {
        // 사용자의 아이템 목록을 수정하는 로직을 구현합니다.
        log.info("updateItems 내 아이템 수정");

        // 유저 찾기
        User user = securityUtil.getUser();

        // 존재하는 UserItem ID를 Key로, UserItem을 Value로 하는 Map 생성
        Map<Long, UserItem> userItemMap = userItemRepository.findByUserId(user.getId()).stream()
            .collect(Collectors.toMap(UserItem::getId, Function.identity()));

        for (UserItemListUpdateRequestDto dto : requestDto) {
            Long id = dto.getUserItemid();

            // 수정 로직
            log.info("user id: " + user.getId() + " userItem id: " + id + " 수정 로직 호출");

            // UserItem 찾기
            UserItem userItem = userItemMap.get(id);
            if (userItem != null) {
                userItem.updateUserItem(LocalDateTime.parse(dto.getRestTime()));
            }
            // UserItem 존재하지 않을 때
            else {
                log.error("user id: " + user.getId() + " userItem id: " + id + " 오류 발생");
                throw new ItemNotFoundException("Item not found with id: " + id);
            }
        }
        
    }


    @Override
    @Transactional
    public void deleteItems(List<UserItemListDeleteRequestDto> requestDto) {
        // 사용자의 아이템 목록을 삭제하는 로직을 구현합니다.
        log.info("deleteItems 내 아이템 삭제");

        // 유저 찾기
        User user = securityUtil.getUser();

        // 존재하는 UserItem 목록 가져오기
        List<UserItem> existingUserItems = userItemRepository.findByUserId(user.getId());

        // 존재하는 UserItem ID 목록 가져오기
        Set<Long> existingUserItemIds = existingUserItems.stream()
            .map(UserItem::getId)
            .collect(Collectors.toSet());

        // 존재하는 UserItem ID를 Key로, UserItem을 Value로 하는 Map 생성
        Map<Long, UserItem> userItemMap = existingUserItems.stream()
            .collect(Collectors.toMap(UserItem::getId, Function.identity()));

        // 삭제할 UserItem 남기기
        for(UserItemListDeleteRequestDto dto : requestDto) {
            if(userItemMap.get(dto.getUserItemId()) != null) {
                existingUserItemIds.remove(dto.getUserItemId());
            }
            // 자신의 아이템이 아닌 경우
            else {
                log.error("user id: " + user.getId() + " userItem id: " + dto.getUserItemId() + " 오류 발생");
                throw new ItemNotFoundException("is not your item" + dto.getUserItemId());
            }
        }

        // UserItem 삭제
        for(Long userItemId : existingUserItemIds) {
            log.info("user id: " + user.getId() + " userItem id: " + userItemId + " 삭제 로직 호출");
            if(userItemMap.get(userItemId) != null) {
                log.info("삭제" + userItemId);
                userItemRepository.deleteById(userItemId);
            }

        }
        
    }
}
