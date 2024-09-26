package probeV.GameInfogg.service.user;

import java.util.List;

import probeV.GameInfogg.controller.user.dto.request.UserItemListSaveRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserItemListUpdateRequestDto;
import probeV.GameInfogg.controller.user.dto.request.UserItemListDeleteRequestDto;
import probeV.GameInfogg.controller.user.dto.response.UserItemListResponseDto;

public interface UserItemService {
    // 내 아이템 조회
    List<UserItemListResponseDto> getUserItems();

    // 내 아이템 항목 새성
    void saveItems(List<UserItemListSaveRequestDto> requestDto);
    // 내 아이템 항목 수정
    void updateItems(List<UserItemListUpdateRequestDto> requestDto);

    // 내 아이템 항목 삭제
    // UserItemListDeleteRequestDto에는 현재 남아있는 Item의 ID만 담아서 보냄 즉, dto에 없는 애들만 삭제
    void deleteItems(List<UserItemListDeleteRequestDto> requestDto);
}
