package com.techopact.store.service;

import com.techopact.store.entities.FixedCircularArrayList;
import com.techopact.store.entities.Item;
import com.techopact.store.repository.ItemRepository;
import com.techopact.store.service.impl.SurgePriceServiceImpl;
import com.techopact.store.utils.StoreProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SurgePriceServiceImplTest {
    private SurgePriceService surgePriceService;

    @Mock
    ItemRepository itemRepository;
    @Mock
    StoreProperties storeProperties;

    @BeforeEach
    void init(@Mock ItemRepository itemRepository, @Mock StoreProperties storeProperties) {
        this.itemRepository = itemRepository;
        this.storeProperties = storeProperties;
        surgePriceService = new SurgePriceServiceImpl(itemRepository, storeProperties);
    }

    @Test
    void when_hits_less_than_limit_and_price_not_increased_then_should_not_increase_price() {
        List<Item> givenItems = new ArrayList<>();
        Item item = new Item();
        item.setPrice(20);
        item.setPriceIncreased(false);
        item.setLastViewedTimes(new FixedCircularArrayList<>(2));
        givenItems.add(item);

        when(storeProperties.getPercentageIncrease()).thenReturn( 10d);

        final List<Item> updatedPriceItems = surgePriceService.calculateSurgePrice(givenItems);

        assertAll(
                () -> assertNotNull(updatedPriceItems.get(0)),
                () -> assertEquals(givenItems.get(0).getPrice(), updatedPriceItems.get(0).getPrice())
        );
    }

    @Test
    void when_hits_equal_to_limit_and_price_not_increased_then_should_increase_price() {
        Item item = new Item();
        item.setPrice(20);
        item.setPriceIncreased(false);
        List<LocalDateTime> lastVisitedList = new FixedCircularArrayList<>(2);
        lastVisitedList.add(LocalDateTime.now().minusMinutes(50));
        lastVisitedList.add(LocalDateTime.now().minusMinutes(40));
        item.setLastViewedTimes(lastVisitedList);
        List<Item> givenItems = new ArrayList<>();
        givenItems.add(item);

        when(storeProperties.getPercentageIncrease()).thenReturn(10d);

        final List<Item> updatedPriceItems = surgePriceService.calculateSurgePrice(givenItems);

        assertAll(
                () -> assertNotNull(updatedPriceItems.get(0)),
                () -> assertEquals(22, updatedPriceItems.get(0).getPrice())
        );
    }
    @Test
    void when_hits_equal_to_limit_and_price_not_increased_and_first_hit_more_than_1hr_then_should_not_increase_price() {
        Item item = new Item();
        item.setPrice(20);
        item.setPriceIncreased(false);
        List<LocalDateTime> lastVisitedList = new FixedCircularArrayList<>(2);
        lastVisitedList.add(LocalDateTime.now().minusMinutes(61));
        lastVisitedList.add(LocalDateTime.now().minusMinutes(40));
        item.setLastViewedTimes(lastVisitedList);
        List<Item> givenItems = new ArrayList<>();
        givenItems.add(item);

        when(storeProperties.getPercentageIncrease()).thenReturn(10d);

        final List<Item> updatedPriceItems = surgePriceService.calculateSurgePrice(givenItems);

        assertAll(
                () -> assertNotNull(updatedPriceItems.get(0)),
                () -> assertEquals(20, updatedPriceItems.get(0).getPrice())
        );
    }

    @Test
    void when_hits_equal_to_limit_and_price_not_increased_then_should_not_decrease_price() {
        List<Item> givenItems = new ArrayList<>();
        Item item = new Item();
        item.setPrice(20);
        item.setPriceIncreased(false);
        item.setLastViewedTimes(new FixedCircularArrayList<>(2));
        givenItems.add(item);

        when(storeProperties.getPercentageIncrease()).thenReturn(10d);

        final List<Item> updatedPriceItems = surgePriceService.calculateSurgePrice(givenItems);

        assertAll(
                () -> assertNotNull(updatedPriceItems.get(0)),
                () -> assertEquals(givenItems.get(0).getPrice(), updatedPriceItems.get(0).getPrice())
        );
    }

    @Test
    void when_hits_equal_to_limit_and_price_increased_and_first_hit_more_than_1hr_then_should_decrease_price() {
        Item item = new Item();
        item.setPrice(22);
        item.setPriceIncreased(true);
        List<LocalDateTime> lastVisitedList = new FixedCircularArrayList<>(2);
        lastVisitedList.add(LocalDateTime.now().minusMinutes(61));
        lastVisitedList.add(LocalDateTime.now().minusMinutes(40));
        item.setLastViewedTimes(lastVisitedList);
        List<Item> givenItems = new ArrayList<>();
        givenItems.add(item);

        when(storeProperties.getPercentageIncrease()).thenReturn(10d);

        final List<Item> updatedPriceItems = surgePriceService.calculateSurgePrice(givenItems);

        assertAll(
                () -> assertNotNull(updatedPriceItems.get(0)),
                () -> assertEquals(20, updatedPriceItems.get(0).getPrice())
        );
    }
}
