package com.backend.threadbit.service;


import com.backend.threadbit.dto.UserDto;
import com.backend.threadbit.repository.ItemRepository;
import com.backend.threadbit.dto.ItemDto;
import com.backend.threadbit.dto.PagedResponseDto;
import com.backend.threadbit.dto.PurchaseDto;
import com.backend.threadbit.model.Item;
import com.backend.threadbit.model.ItemType;
import com.backend.threadbit.model.Purchase;
import com.backend.threadbit.model.Status;
import com.backend.threadbit.model.User;
import com.backend.threadbit.repository.CategoryRepository;
import com.backend.threadbit.repository.PurchaseRepository;
import com.backend.threadbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private final PurchaseRepository purchaseRepository;
    @Autowired
    private final UserService userService;

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
    public List<Item> getItemByUsername(String sellerUsername) {
        Optional<User> seller = userRepository.findByUsername(sellerUsername);
        if(seller.isPresent()) return itemRepository.findBySellerId(seller.get().getId());
        return Collections.emptyList();
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
                .currentPrice(itemDto.getStartingPrice())
                .imageUrls(itemDto.getImageUrls())
                .sellerId(itemDto.getSellerId())
                .categoryId(itemDto.getCategoryId())
                .originalPrice(itemDto.getOriginalPrice())
                .buyNowPrice(itemDto.getBuyNowPrice())
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

    @Override
    public PagedResponseDto<Item> getAllItems(int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.findAll(pageable);
        return createPagedResponse(itemPage);
    }

    @Override
    public PagedResponseDto<Item> getItemsByCategory(String categoryId, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.findByCategoryId(categoryId, pageable);
        return createPagedResponse(itemPage);
    }

    @Override
    public PagedResponseDto<Item> getItemsBySeller(String sellerId, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.findBySellerId(sellerId, pageable);
        return createPagedResponse(itemPage);
    }

    @Override
    public PagedResponseDto<Item> getItemsByStatus(Status status, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.findByStatus(status, pageable);
        return createPagedResponse(itemPage);
    }

    @Override
    public PagedResponseDto<Item> searchItems(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.searchByTitleOrDescription(keyword, pageable);
        return createPagedResponse(itemPage);
    }

    @Override
    public PagedResponseDto<Item> getItems(String keyword, String categoryId, Status status, String sellerId, 
                                          String sellerUsername, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage;

        // Handle search by seller username
        if (sellerUsername != null && !sellerUsername.isEmpty()) {
            List<User> sellers = userRepository.findByUsernameContaining(sellerUsername);
            if (sellers.isEmpty()) {
                // Return empty page if no sellers found
                return new PagedResponseDto<>(new ArrayList<>(), page, size, 0, 0, true);
            }

            List<String> sellerIds = sellers.stream()
                    .map(User::getId)
                    .collect(Collectors.toList());

            itemPage = itemRepository.findBySellerIds(sellerIds, pageable);
            return createPagedResponse(itemPage);
        }

        // Handle combined search and filter
        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && !categoryId.isEmpty()) {
                itemPage = itemRepository.searchByTitleOrDescriptionAndCategory(keyword, categoryId, pageable);
            } else if (status != null) {
                itemPage = itemRepository.searchByTitleOrDescriptionAndStatus(keyword, status, pageable);
            } else {
                itemPage = itemRepository.searchByTitleOrDescription(keyword, pageable);
            }
        } else {
            // Handle filter only
            if (categoryId != null && !categoryId.isEmpty()) {
                itemPage = itemRepository.findByCategoryId(categoryId, pageable);
            } else if (status != null) {
                itemPage = itemRepository.findByStatus(status, pageable);
            } else if (sellerId != null && !sellerId.isEmpty()) {
                itemPage = itemRepository.findBySellerId(sellerId, pageable);
            } else {
                itemPage = itemRepository.findAll(pageable);
            }
        }

        return createPagedResponse(itemPage);
    }


    private Sort createSort(String sortBy, String sortDir) {
        Sort sort = Sort.by(sortBy);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        return sort;
    }

    private PagedResponseDto<Item> createPagedResponse(Page<Item> page) {
        return PagedResponseDto.<Item>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private <T> PagedResponseDto<T> createGenericPagedResponse(Page<T> page) {
        return PagedResponseDto.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    public Item createInstantBuyItem(ItemDto itemDto) {
        // Validate that seller exists
        userRepository.findById(itemDto.getSellerId())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        // Validate that category exists
        categoryRepository.findById(itemDto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found"));

        // Validate instant buy specific fields
        if (itemDto.getBuyNowPrice() == null || itemDto.getBuyNowPrice() <= 0) {
            throw new IllegalArgumentException("Buy now price is required and must be positive");
        }

        if (itemDto.getStockQuantity() == null || itemDto.getStockQuantity() <= 0) {
            throw new IllegalArgumentException("Stock quantity is required and must be positive");
        }

        // Create and save the item
        Item item = Item.builder()
                .title(itemDto.getTitle())
                .description(itemDto.getDescription())
                .brand(itemDto.getBrand())
                .size(itemDto.getSize())
                .condition(itemDto.getCondition())
                .color(itemDto.getColor())
                .startingPrice(itemDto.getStartingPrice() != null ? itemDto.getStartingPrice() : 0)
                .currentPrice(itemDto.getStartingPrice() != null ? itemDto.getStartingPrice() : 0)
                .imageUrls(itemDto.getImageUrls())
                .sellerId(itemDto.getSellerId())
                .categoryId(itemDto.getCategoryId())
                .originalPrice(itemDto.getOriginalPrice())
                .buyNowPrice(itemDto.getBuyNowPrice())
                .itemType(ItemType.INSTANT_BUY)
                .stockQuantity(itemDto.getStockQuantity())
                .soldQuantity(0)
                .status(Status.ACTIVE)
                .build();

        return itemRepository.save(item);
    }

    @Override
    public Purchase purchaseItem(PurchaseDto purchaseDto) {
        // Validate that item exists and is available for purchase
        Item item = itemRepository.findById(purchaseDto.getItemId())
                .orElseThrow(() -> new NoSuchElementException("Item not found"));

        // Check if item is an instant buy item
        if (item.getItemType() != ItemType.INSTANT_BUY) {
            throw new IllegalArgumentException("Item is not available for instant purchase");
        }

        // Check if item is active
        if (item.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("Item is not active");
        }

        // Check if there's enough stock
        if (item.getStockQuantity() < purchaseDto.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        // Validate that buyer exists
        User buyer = userRepository.findById(purchaseDto.getBuyerId())
                .orElseThrow(() -> new NoSuchElementException("Buyer not found"));

        User seller = userRepository.findById(item.getSellerId())
                .orElseThrow(() -> new NoSuchElementException("Seller not found"));

        if(seller.getId().equals(buyer.getId())) {
            throw new IllegalArgumentException("Cannot purchase own item");
        }

        // Calculate total price
        Integer pricePerUnit = item.getBuyNowPrice();
        Integer totalPrice = pricePerUnit * purchaseDto.getQuantity();

        if(totalPrice < Double.parseDouble(buyer.getWalletBalance())) {
            throw new IllegalArgumentException("Insufficient funds in wallet");
        }
        // Update buyer's wallet balance
        double newBuyerBalance = Double.parseDouble(buyer.getWalletBalance()) - totalPrice;
        UserDto buyerDto = UserDto.builder()
                .walletBalance(String.valueOf(newBuyerBalance))
                .build();
        userService.updateUser(buyer.getId(), buyerDto);

        // Update seller's wallet balance
        double newSellerBalance = Double.parseDouble(seller.getWalletBalance()) + totalPrice;
        UserDto sellerDto = UserDto.builder()
                .walletBalance(String.valueOf(newSellerBalance))
                .build();
        userService.updateUser(seller.getId(), sellerDto);

        // Create purchase record
        Purchase purchase = Purchase.builder()
                .itemId(item.getId())
                .buyerId(purchaseDto.getBuyerId())
                .quantity(purchaseDto.getQuantity())
                .pricePerUnit(pricePerUnit)
                .totalPrice(totalPrice)
                .status(Status.COMPLETED)
                .build();

        // Update item stock
        item.setStockQuantity(item.getStockQuantity() - purchaseDto.getQuantity());
        item.setSoldQuantity(item.getSoldQuantity() + purchaseDto.getQuantity());

        // If stock is depleted, mark item as ended
        if (item.getStockQuantity() <= 0) {
            item.setStatus(Status.ENDED);
        }

        // Save updated item
        itemRepository.save(item);

        // Save and return purchase
        return purchaseRepository.save(purchase);
    }

    @Override
    public List<Purchase> getPurchasesByBuyer(String buyerId) {
        return purchaseRepository.findByBuyerId(buyerId);
    }

    @Override
    public PagedResponseDto<Purchase> getPurchasesByBuyer(String buyerId, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Purchase> purchasePage = purchaseRepository.findByBuyerId(buyerId, pageable);
        return createGenericPagedResponse(purchasePage);
    }

    @Override
    public PagedResponseDto<Item> getAvailableInstantBuyItems(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> itemPage = itemRepository.searchAvailableInstantBuyItems(keyword, pageable);
        return createPagedResponse(itemPage);
    }
}
