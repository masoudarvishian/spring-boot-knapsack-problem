package com.masoud.packager.application.services;

import com.masoud.packager.application.interfaces.KnapsackAlgorithm;
import com.masoud.packager.application.interfaces.PackagerLimits;
import com.masoud.packager.application.interfaces.PackagerService;
import com.masoud.packager.application.interfaces.PackagerValidator;
import com.masoud.packager.application.services.KnapsackAlgorithmImpl;
import com.masoud.packager.application.services.PackagerServiceImpl;
import com.masoud.packager.application.services.PackagerValidatorImpl;
import com.masoud.packager.domain.exceptions.DuplicateIdException;
import com.masoud.packager.domain.exceptions.InvalidDataLimitException;
import com.masoud.packager.domain.entities.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
class PackagerValidatorTests {

    @Autowired
    PackagerService packagerService;

    @Autowired
    PackagerValidator packagerValidator;

    @Autowired
    KnapsackAlgorithm knapsackAlgorithm;

    @Mock
    private PackagerLimits packagerLimits;

    @BeforeEach
    public void setUpBeforeAll() {
        packagerValidator = new PackagerValidatorImpl(packagerLimits);
        knapsackAlgorithm = new KnapsackAlgorithmImpl();
        packagerService = new PackagerServiceImpl(packagerValidator, knapsackAlgorithm);

        when(packagerLimits.getMaxItemsLimit()).thenReturn(15);
        when(packagerLimits.getMaxItemWeightLimit()).thenReturn(100f);
        when(packagerLimits.getMaxItemPriceLimit()).thenReturn(100);
        when(packagerLimits.getMaxPackageWeight()).thenReturn(100);
    }

    @Test
    void shouldThrowExceptionWhenExceedMaximumItemsCountLimit() {
        // given
        int maxPackagerWeight = 50;
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 10.0f, 60));
        items.add(new Item(2, 20.0f, 100));
        items.add(new Item(3, 30.0f, 120));
        items.add(new Item(4, 35.0f, 90));
        when(packagerLimits.getMaxItemsLimit()).thenReturn(3);

        // then
        thenThrowsInvalidDataLimitException(maxPackagerWeight, items);
    }

    @Test
    void shouldThrowExceptionWhenExceedMaximumWeightOrPriceLimit() {
        // given
        int maxPackagerWeight = 50;
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 10.0f, 60));
        items.add(new Item(2, 200.0f, 100));
        items.add(new Item(3, 30.0f, 120));

        // then
        thenThrowsInvalidDataLimitException(maxPackagerWeight, items);
    }

    @Test
    void shouldThrowExceptionWhenWeightOrPriceIsLessThanOrEqualToZero() {
        // given
        int maxPackagerWeight = 50;
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, -1, 60));
        items.add(new Item(2, 20.0f, 90));
        items.add(new Item(3, 30.0f, -1));

        // then
        thenThrowsInvalidDataLimitException(maxPackagerWeight, items);
    }

    @Test
    void shouldThrowExceptionWhenWeightIsLessThanOrEqualToZero() {
        // given
        int maxPackagerWeight = 50;
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, -1, 60));
        items.add(new Item(2, 20.0f, 90));
        items.add(new Item(3, 30.0f, 10));

        // then
        thenThrowsInvalidDataLimitException(maxPackagerWeight, items);
    }

    @Test
    void shouldThrowExceptionWhenPriceIsLessThanOrEqualToZero() {
        // given
        int maxPackagerWeight = 50;
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 10, 60));
        items.add(new Item(2, 20.0f, 90));
        items.add(new Item(3, 30.0f, -1));

        // then
        thenThrowsInvalidDataLimitException(maxPackagerWeight, items);
    }

    @Test
    void shouldThrowExceptionWhenIdIsLessThanOrEqualToZero() {
        // given
        int maxPackagerWeight = 50;
        List<Item> items = new ArrayList<>();
        items.add(new Item(-1, 10.0f, 60));
        items.add(new Item(2, 20.0f, 90));
        items.add(new Item(3, 30.0f, 10));

        // then
        thenThrowsInvalidDataLimitException(maxPackagerWeight, items);
    }

    @Test
    void shouldThrowExceptionWhenExceedMaximumPackageWeightLimit() {
        // given
        int maxPackagerWeight = 150;
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 10.0f, 60));
        items.add(new Item(2, 20.0f, 90));
        items.add(new Item(3, 30.0f, 10));

        // then
        thenThrowsInvalidDataLimitException(maxPackagerWeight, items);
    }

    @Test
    void shouldThrowExceptionWhenMaxPackagerWeightIsLessThanOrEqualToZero() {
        // given
        int maxPackagerWeight = -1;
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 10.0f, 60));
        items.add(new Item(2, 20.0f, 90));
        items.add(new Item(3, 30.0f, 10));

        // then
        thenThrowsInvalidDataLimitException(maxPackagerWeight, items);
    }

    @Test
    void shouldThrowExceptionWhenItemsIdAreDuplicate() {
        // given
        int maxPackagerWeight = 50;
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 10.0f, 60));
        items.add(new Item(1, 20.0f, 90));
        items.add(new Item(3, 30.0f, 10));

        // then
        Assertions.assertThrows(DuplicateIdException.class, () -> {
            packagerService.fillPackage(maxPackagerWeight, items);
        });
    }

    @Test
    void shouldThrowExceptionWhenItemsListIsEmptyOrNull() {
        // given
        int maxPackagerWeight = 8;
        List<Item> items = null;
        // then
        thenThrowsInvalidDataLimitException(maxPackagerWeight, items);
    }

    private void thenThrowsInvalidDataLimitException(int maxPackagerWeight, List<Item> items) {
        Assertions.assertThrows(InvalidDataLimitException.class, () -> {
            packagerService.fillPackage(maxPackagerWeight, items);
        });
    }
}
