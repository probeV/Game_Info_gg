package probeV.GameInfogg.service.item;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import probeV.GameInfogg.controller.item.dto.response.ItemListResponseDto;
import probeV.GameInfogg.domain.item.Item;
import probeV.GameInfogg.repository.item.ItemRepository;

public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllItemList() {
        // Given
        Item item1 = new Item(1L, "Item1", "Description1");
        Item item2 = new Item(2L, "Item2", "Description2");
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        // When
        List<ItemListResponseDto> result = itemService.getAllItemList();

        // Then
        assertEquals(2, result.size());
        assertEquals("Item1", result.get(0).getItemName());
        assertEquals("Item2", result.get(1).getItemName());
    }

    @Test
    public void testGetSearchItemList() {
        // Given
        String keyword = "Item";
        Item item1 = new Item(1L, "Item1", "Description1");
        Item item2 = new Item(2L, "Item2", "Description2");
        when(itemRepository.findByItemNameContaining(keyword)).thenReturn(Arrays.asList(item1, item2));

        // When
        List<ItemListResponseDto> result = itemService.getSearchItemList(keyword);

        // Then
        assertEquals(2, result.size());
        assertEquals("Item1", result.get(0).getItemName());
        assertEquals("Item2", result.get(1).getItemName());
    }
}