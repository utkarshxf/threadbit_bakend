package com.backend.threadbit.service;


import com.backend.threadbit.repository.ItemRepository;
import com.backend.threadbit.dto.ItemDto;
import com.backend.threadbit.model.Item;
import com.backend.threadbit.model.Status;
import com.backend.threadbit.repository.CategoryRepository;
import com.backend.threadbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CategoryRepository categoryRepository;

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemsByCategory(String categoryId) {
        return itemRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Item> getItemsBySeller(String sellerId) {
        return itemRepository.findBySellerId(sellerId);
    }

    @Override
    public Item getItemById(String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
    }

    @Override
    public Item createItem(ItemDto itemDto) {
        // Validate that seller exists
        userRepository.findById(itemDto.getSellerId())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));
        
        // Validate that category exists
        categoryRepository.findById(itemDto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
        
        // Create and save the item
        Item item = Item.builder()
                .title(itemDto.getTitle())
                .description(itemDto.getDescription())
                .brand(itemDto.getBrand())
                .size(itemDto.getSize())
                .condition(itemDto.getCondition())
                .color(itemDto.getColor())
                .startingPrice(itemDto.getStartingPrice())
                .currentPrice(itemDto.getStartingPrice()) // Initially set to starting price
                .imageUrls(itemDto.getImageUrls())
                .sellerId(itemDto.getSellerId())
                .categoryId(itemDto.getCategoryId())
                .endTime(itemDto.getEndTime())
                .status(Status.ACTIVE) // Default status
                .build();
        
        return itemRepository.save(item);
    }

    @Override
    public Item updateItemStatus(String id, Status status) {
        Item item = getItemById(id);
        item.setStatus(status);
        return itemRepository.save(item);
    }
}
