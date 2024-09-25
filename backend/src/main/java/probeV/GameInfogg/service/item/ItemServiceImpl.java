package probeV.GameInfogg.service.item;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import probeV.GameInfogg.controller.item.dto.response.ItemListResponseDto;
import probeV.GameInfogg.repository.item.ItemRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public List<ItemListResponseDto> getAllItemList() {
        // 아이템 목록을 조회하는 로직을 구현합니다.
        log.info("getAllItemList 아이템 조회");

        return itemRepository.findAll().stream()
            .map(ItemListResponseDto::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<ItemListResponseDto> getSearchItemList(String keyword) {
        // 아이템 목록을 검색하는 로직을 구현합니다.
        log.info("getSearchItemList 아이템 검색");
        
        return itemRepository.findByItemNameContaining(keyword).stream()
            .map(ItemListResponseDto::new)
            .collect(Collectors.toList());
    }
    
}
