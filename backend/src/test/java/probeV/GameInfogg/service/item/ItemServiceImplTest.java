package probeV.GameInfogg.service.item;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import probeV.GameInfogg.controller.item.dto.response.ItemListResponseDto;
import probeV.GameInfogg.domain.item.Item;
import probeV.GameInfogg.repository.item.ItemRepository;


@Slf4j
@SpringBootTest
@Transactional
public class    ItemServiceImplTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @AfterEach
    public void tearDown() {
        itemRepository.deleteAll();
    }

    @Test
    public void getAllItemList_조회_성공() {
        // Given
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
        // When
        List<ItemListResponseDto> result = itemService.getAllItemList();

        // Then
        assertEquals(2, result.size());
        assertEquals("Item1", result.get(0).getName());
        assertEquals("Item2", result.get(1).getName());
    }

    @Test
    public void getSearchItemList_조회_성공() {
        // Given
        String keyword = "Item";
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
        // When
        List<ItemListResponseDto> result = itemService.getSearchItemList(keyword);

        // Then
        assertEquals(2, result.size());
        assertEquals("Item1", result.get(0).getName());
        assertEquals("Item2", result.get(1).getName());
    }

    @Test
    public void getSearchItemList_조회_실패_keyword_관련없음() {
        // Given
        String keyword = "asdf";
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
        // When
        List<ItemListResponseDto> result = itemService.getSearchItemList(keyword);

        // Then
        assertEquals(0, result.size());
    }
}