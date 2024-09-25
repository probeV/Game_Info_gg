package probeV.GameInfogg.controller.item;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import probeV.GameInfogg.controller.item.dto.response.ItemListResponseDto;
import probeV.GameInfogg.service.item.ItemService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ItemController {

    private final ItemService itemService;

    // 아이템 목록 조회 및 검색
    @GetMapping("/items")
    public List<ItemListResponseDto> getItemList(
        @RequestParam(value = "keyword", required = false) 
        @Size(max = 255, message = "키워드는 255자 이하여야 합니다.") String keyword) {
        
        if (keyword == null || keyword.isEmpty()) {
            return itemService.getAllItemList();
        } else {
            return itemService.getSearchItemList(keyword);
        }
    }
}
