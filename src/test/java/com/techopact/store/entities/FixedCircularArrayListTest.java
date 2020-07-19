package com.techopact.store.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class FixedCircularArrayListTest {

    @Test
    void when_less_than_specified_no_of_elements_added_should_add_all_the_elements() {
        List<String> fixedCircularArrayList = new FixedCircularArrayList<>(10);
        fixedCircularArrayList.add("Anand");
        fixedCircularArrayList.add("Aaron");
        fixedCircularArrayList.add("Aidan");

        final int expectedSize = 3;
        assertAll(
                () -> assertTrue(fixedCircularArrayList.contains("Anand")),
                () -> assertTrue(fixedCircularArrayList.contains("Aaron")),
                () -> assertTrue(fixedCircularArrayList.contains("Aidan")),
                () -> assertEquals(expectedSize, fixedCircularArrayList.size())
        );
        System.out.println(fixedCircularArrayList);
    }

    @Test
    void when_more_than_specified_no_of_elements_added_should_rearrange_the_elements() {
        List<String> fixedCircularArrayList = new FixedCircularArrayList<>(10);
        fixedCircularArrayList.add("Anand");
        fixedCircularArrayList.add("Aaron");
        fixedCircularArrayList.add("Aidan");
        fixedCircularArrayList.add("Thor");
        fixedCircularArrayList.add("Hulk");
        fixedCircularArrayList.add("Captain America");
        fixedCircularArrayList.add("Spider man");
        fixedCircularArrayList.add("Iron man");
        fixedCircularArrayList.add("Dr Strange");
        fixedCircularArrayList.add("Captain Marvel");
        fixedCircularArrayList.add("War machine");
        fixedCircularArrayList.add("Thanos");

        assertAll(
                () -> assertEquals("Aidan", fixedCircularArrayList.get(0)),
                () -> assertEquals("Thanos", fixedCircularArrayList.get(9)),
                () -> assertEquals(10, fixedCircularArrayList.size())
        );
        System.out.println(fixedCircularArrayList);
    }


}
