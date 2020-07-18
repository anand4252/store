package com.techopact.store.service.impl;

import com.techopact.store.entities.Item;
import com.techopact.store.enums.ItemStatus;
import com.techopact.store.repository.ItemRepository;
import com.techopact.store.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<Item> fetchAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> fetchItemById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public ItemStatus placeOrder(Long id) {
        final Optional<Item> optionalItem = itemRepository.findById(id);
        if(!optionalItem.isPresent()){
            return ItemStatus.INVALID;
        }
        final Item item = optionalItem.get();
        int quantity = item.getQuantity();
        if(quantity==0){
            return ItemStatus.OUT_OF_STOCK;
        }
        item.setQuantity(--quantity);
        //Call an API to place the order
        return ItemStatus.ORDER_PLACED;
    }
}
