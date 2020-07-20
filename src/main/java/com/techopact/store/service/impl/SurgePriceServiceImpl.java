package com.techopact.store.service.impl;

import com.techopact.store.entities.FixedCircularArrayList;
import com.techopact.store.entities.Item;
import com.techopact.store.repository.ItemRepository;
import com.techopact.store.service.SurgePriceService;
import com.techopact.store.utils.StoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SurgePriceServiceImpl implements SurgePriceService {
    private final ItemRepository itemRepository;
    private final StoreProperties storeProperties;

    private static final double HUNDRED = 100;


    public List<Item> calculateSurgePrice(List<Item> items) {
        final List<Item> updatedPriceItems = updateSurgePrice(items);
        updateLastViewedTime(items);

        return updatedPriceItems;
    }

    private List<Item> updateSurgePrice(List<Item> items) {
        final double hundredPlusPercentage = HUNDRED + storeProperties.getPercentageIncrease();
        List<Item> increasePriceItems = increasePriceItem(items, hundredPlusPercentage);
        List<Item> decreasedPriceItems = decreasePriceItems(items, hundredPlusPercentage);
        List<Item> updatedPrice = Stream.concat(increasePriceItems.stream(), decreasedPriceItems.stream())
                             .collect(Collectors.toList());
        itemRepository.saveAll(updatedPrice);
        return items;
    }

    private List<Item> increasePriceItem(List<Item> items, double hundredPlusPercentage) {
        return items.stream()
                .filter(this::shouldIncreasePrice)
                .peek(item -> {
                    final double incPrice = item.getPrice() * (hundredPlusPercentage/HUNDRED);
                    item.setPrice(incPrice);
                    item.setPriceIncreased(true);
                })
                .collect(Collectors.toList());
    }

    private List<Item> decreasePriceItems(List<Item> items, double hundredPlusPercentage) {
        return items.stream()
                .filter(this::shouldDecreasePrice)
                .peek(item -> {
                    final double decPrice = item.getPrice() * (HUNDRED / hundredPlusPercentage);
                    item.setPrice(decPrice);
                    item.setPriceIncreased(false);
                })
                .collect(Collectors.toList());
    }

    private boolean shouldDecreasePrice(Item item) {
        boolean shouldDecreasePrice;
        final LocalDateTime firstDateTime = getFirstDateTime(item);
        final LocalDateTime currentDateTime = LocalDateTime.now();
        shouldDecreasePrice = item.getLastViewedTimes().size() == storeProperties.getViewLimit()
                && item.isPriceIncreased()
                && firstDateTime.plusMinutes(storeProperties.getWindowPeriodInMinutes()).isBefore(currentDateTime);
        return shouldDecreasePrice;
    }

    private boolean shouldIncreasePrice(Item item) {
        boolean shouldIncreasePrice;
        final LocalDateTime firstDateTime = getFirstDateTime(item);
        final LocalDateTime currentDateTime = LocalDateTime.now();
        shouldIncreasePrice = item.getLastViewedTimes().size() == storeProperties.getViewLimit()
                && !item.isPriceIncreased()
                && firstDateTime.plusMinutes(storeProperties.getWindowPeriodInMinutes()).isAfter(currentDateTime);
        return shouldIncreasePrice;
    }

    private LocalDateTime getFirstDateTime(Item item) {
        LocalDateTime firstDateTime = null;
        if(item.getLastViewedTimes().size()!=0){
            firstDateTime = LocalDateTime.parse(item.getLastViewedTimes().get(0).toString());
        }
        return firstDateTime;
    }

    private void updateLastViewedTime(List<Item> items){
        List<Item> alteredItems = new ArrayList<>(items);
        for(Item item : alteredItems){
            List<LocalDateTime> fixedCircularArrayList =  new FixedCircularArrayList<>(2);
            fixedCircularArrayList.addAll(item.getLastViewedTimes());

            fixedCircularArrayList.add(LocalDateTime.now());
            item.setLastViewedTimes(fixedCircularArrayList);
        }
        itemRepository.saveAll(alteredItems);
    }
}
