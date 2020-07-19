package com.techopact.store.service.impl;

import com.techopact.store.entities.Item;
import com.techopact.store.enums.ItemStatus;
import com.techopact.store.repository.ItemRepository;
import com.techopact.store.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

//    @Value("${app.percentageIncrease:10}")
//    private final Integer percentageIncreaseStr;


//    @Autowired
//    public ItemServiceImpl(ItemRepository itemRepository){
//
//    }

    @Override
    public List<Item> fetchAllItems() {
        final List<Item> items = itemRepository.findAll();
        final List<Item> updatedViewedTimeItems = surgePrice(items);

        itemRepository.saveAll(updatedViewedTimeItems);
        return updatedViewedTimeItems;
    }

    private List<Item> surgePrice(List<Item> items) {
        System.out.println("Before :" + items);
        int percentageIncreaseStr=10;
        final BigDecimal increasePercentage = new BigDecimal(percentageIncreaseStr);
        BigDecimal HUNDRED = new BigDecimal(100);
        BigDecimal hundredPlusPercentage = HUNDRED.add(increasePercentage);
        final List<Item> updatedPriceItems = items.stream()
                .filter(item -> item.getLastViewedTimes().size() == 1)//TODO Export to variable
                .peek(item -> {
                    final BigDecimal incPrice = item.getPrice().multiply(hundredPlusPercentage.divide(HUNDRED));
                    item.setPrice(incPrice);
                })
                .collect(Collectors.toList());
        System.out.println("After :" + updatedPriceItems);

        itemRepository.saveAll(updatedPriceItems);
        return items.stream().peek(item -> item.getLastViewedTimes().add(LocalDateTime.now())).collect(Collectors.toList());
    }

    @Override
    public Optional<Item> fetchItemById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public ItemStatus placeOrder(Long id) {
        final Optional<Item> optionalItem = itemRepository.findById(id);
        if (!optionalItem.isPresent()) {
            return ItemStatus.INVALID;
        }
        final Item item = optionalItem.get();
        int quantity = item.getQuantity();
        if (quantity == 0) {
            return ItemStatus.OUT_OF_STOCK;
        }
        item.setQuantity(--quantity);
        this.itemRepository.save(item);
        //Call an API to place the order
        return ItemStatus.ORDER_PLACED;
    }
}
