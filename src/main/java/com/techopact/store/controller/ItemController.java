package com.techopact.store.controller;

import com.techopact.store.entities.Item;
import com.techopact.store.enums.ItemStatus;
import com.techopact.store.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("store")
@RequiredArgsConstructor
public class ItemController {
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    @GetMapping("/v1/items")
    public ResponseEntity<List<Item>> fetchAllItems() {
        logger.debug("fetch All Items end point invoked");
        return new ResponseEntity<>(itemService.fetchAllItems(), HttpStatus.OK);
    }

    @GetMapping("/v1/item/{id}")
    public ResponseEntity<Item> fetchItems(@PathVariable Long id) {
        logger.debug("Fetch item with id {}", id);
        Optional<Item> optionalItem = itemService.fetchItemById(id);
        return optionalItem.map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new Item(), HttpStatus.NOT_FOUND));
    }

    @PutMapping("/v1/item/{id}/order")
    public ResponseEntity<String> buyItem(@PathVariable Long id){
        logger.debug("Order placed for item id {}", id);
        switch (itemService.placeOrder(id)){
            case ORDER_PLACED:
                return new ResponseEntity<>(ItemStatus.ORDER_PLACED.toString(), HttpStatus.OK);
            case OUT_OF_STOCK:
                return new ResponseEntity<>(ItemStatus.OUT_OF_STOCK.toString(), HttpStatus.OK);
            default:
                return new ResponseEntity<>(ItemStatus.INVALID.toString(), HttpStatus.NOT_FOUND);
        }
    }
}
