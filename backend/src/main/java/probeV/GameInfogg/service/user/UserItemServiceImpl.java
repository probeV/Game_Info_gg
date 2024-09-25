package probeV.GameInfogg.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import probeV.GameInfogg.controller.user.dto.response.UserItemListResponseDto;
import probeV.GameInfogg.controller.user.dto.request.UserItemListSaveorUpdateRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserItemListDeleteRequestDto;
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
    public void saveItems(List<UserItemListSaveorUpdateRequestDto> requestDto) {
        // 사용자의 아이템 목록을 생성하는 로직을 구현합니다.
        log.info("saveItems 내 아이템 생성");

        // 유저 찾기
        User user = securityUtil.getUser();

        // 해당 유저의 존재하는 아이템 목록 찾기
        List<UserItem> userItems = userItemRepository.findByUserId(user.getId());

        // 존재하는 UserItem ID를 Key로, UserItem을 Value로 하는 Map 생성
        Map<Long, UserItem> userItemMap = userItems.stream()
            .collect(Collectors.toMap(UserItem::getId, Function.identity()));

        // 수정 및 생성 처리
        for (UserItemListSaveorUpdateRequestDto dto : requestDto) {
            Long id = dto.getId();

            // 이미 존재하는 경우
            if (id != null && userItemMap.containsKey(id)) {
                // 수정 로직 호출
                log.info("유저 " + user.getId() + "의 아이템 " + id + " 수정 로직 호출");

                UserItem userItem = userItemMap.get(id);
                userItem.updateUserItem(LocalDateTime.parse(dto.getRestTime()));
            }
            // 존재하지 않는 경우
            else if (id == null) {
                // 생성 로직
                log.info("유저 " + user.getId() + "의 아이템 " + id + " 생성 로직 호출");

                UserItem userItem = dto.toEntity();
                userItem.setUser(user);
                userItemRepository.save(userItem);
            }
            // 존재하지 않는 경우
            else {
                log.error("유저 " + user.getId() + "의 아이템 " + id + " 오류 발생");

                throw new ItemNotFoundException("Item not found" + id);
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

        for(UserItemListDeleteRequestDto dto : requestDto) {
            if(userItemMap.get(dto.getId()) != null) {
                existingUserItemIds.remove(dto.getId());
            }
        }

        for(Long itemId : existingUserItemIds) {
            log.info("유저 " + user.getId() + "의 아이템 " + itemId + " 삭제 로직 호출");
            if(userItemMap.get(itemId) != null) {
                userItemRepository.deleteById(itemId);
            }
        }
        
    }
}
