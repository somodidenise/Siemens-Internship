package com.siemens.internship;

import com.siemens.internship.model.Item;
import com.siemens.internship.repository.ItemRepository;
import com.siemens.internship.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void testFindAll() {
        List<Item> items = List.of(
                new Item(1L, "Item1", "Description1", "Status1", "item1@test.com"),
                new Item(2L, "Item2", "Description2", "Status2", "item2@test.com"),
                new Item(3L, "Item3", "Description3", "Status3", "item3@test.com")
        );
        Mockito.when(itemRepository.findAll()).thenReturn(items);
        List<Item> result = itemService.findAll();

        assertEquals(3, result.size());
        assertEquals("Item1", result.get(0).getName());
        assertEquals("Item2", result.get(1).getName());
        assertEquals("Item3", result.get(2).getName());
    }


    @Test
    void testFindById() {
        Item item = new Item(1L, "Item", "Description", "Status", "item@test.com");
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Optional<Item> result = itemService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Item", result.get().getName());
    }

    @Test
    void testSave() {
        Item item = new Item(1L, "Item", "Description", "Status", "item@test.com");
        Mockito.when(itemRepository.save(any())).thenReturn(item);
        Item saved = itemService.save(item);
        assertEquals("Item", saved.getName());
    }

    @Test
    void testDeleteById() {
        Long itemId = 1L;
        itemService.deleteById(itemId);
        Mockito.verify(itemRepository).deleteById(itemId);
    }

}
