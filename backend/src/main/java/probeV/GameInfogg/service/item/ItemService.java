package probeV.GameInfogg.service.item;

import java.util.List;

import probeV.GameInfogg.controller.item.dto.response.ItemListResponseDto;

public interface ItemService {
    // 아이템 목록 조회
    List<ItemListResponseDto> getAllItemList();
    // 아이템 목록 조회 (검색)
    List<ItemListResponseDto> getSearchItemList(String keyword);
}
