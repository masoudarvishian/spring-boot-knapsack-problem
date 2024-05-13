package com.masoud.packager.application.services;

import com.masoud.packager.application.interfaces.KnapsackAlgorithm;
import com.masoud.packager.application.interfaces.PackagerLimits;
import com.masoud.packager.application.interfaces.PackagerService;
import com.masoud.packager.application.interfaces.PackagerValidator;
import com.masoud.packager.application.services.KnapsackAlgorithmImpl;
import com.masoud.packager.application.services.PackagerServiceImpl;
import com.masoud.packager.application.services.PackagerValidatorImpl;
import com.masoud.packager.domain.exceptions.EmptyPackageException;
import com.masoud.packager.domain.entities.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class PackagerServiceTests {

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

    @ParameterizedTest
    @MethodSource("testDataProvider")
    void shouldReturnExpectedResultWhenInvoked(TestData testData) {
        // given
        int maxPackagerWeight = testData.maxPackagerWeight();
        List<Item> itemList = testData.itemList();
        List<Integer> expectedResult = testData.expectedResult();

        // when
        List<Integer> result = packagerService.fillPackage(maxPackagerWeight, itemList);

        // then
        Assertions.assertIterableEquals(expectedResult, result);
    }

    @Test
    void shouldThrowExceptionWhenNoItemCouldBeAddToPackage() {
        int maxPackagerWeight = 8;
        List<Item> items = List.of(
            new Item(1, 15.3f, 34)
        );
        Assertions.assertThrows(EmptyPackageException.class, () -> {
            packagerService.fillPackage(maxPackagerWeight, items);
        });
    }

    private static List<TestData> testDataProvider() {
        List<TestData> testDataList = new ArrayList<>();

        testDataList.add(getTestDataGroup1());
        testDataList.add(getTestDataGroup2());
        testDataList.add(getTestDataGroup3());
        testDataList.add(getTestDataGroup4());
        testDataList.add(getTestDataGroup5());
        testDataList.add(getTestDataGroup6());

        return testDataList;
    }

    private static TestData getTestDataGroup1() {
        int maxPackagerWeight = 75;
        List<Item> items = List.of(
                new Item(1, 85.31f, 29),
                new Item(2, 14.55f, 74),
                new Item(3, 3.98f, 16),
                new Item(4, 26.24f, 55),
                new Item(5, 63.69f, 52),
                new Item(6, 76.25f, 75),
                new Item(7, 60.02f, 74),
                new Item(8, 93.18f, 35),
                new Item(9, 89.95f, 78)
        );
        List<Integer> expectedResult = List.of(2, 7);
        return new TestData(maxPackagerWeight, items, expectedResult);
    }

    private static TestData getTestDataGroup2() {
        int maxPackagerWeight = 80;
        List<Item> items = List.of(
                new Item(1, 53.38f, 45),
                new Item(2, 88.62f, 98),
                new Item(3, 78.48f, 3),
                new Item(4, 72.30f, 76),
                new Item(5, 30.18f, 9),
                new Item(6, 46.34f, 48)
        );
        List<Integer> expectedResult = List.of(4);
        return new TestData(maxPackagerWeight, items, expectedResult);
    }

    private static TestData getTestDataGroup3() {
        int maxPackagerWeight = 56;
        List<Item> items = List.of(
                new Item(1, 90.72f, 13),
                new Item(2, 33.80f, 40),
                new Item(3, 43.15f, 10),
                new Item(4, 37.97f, 16),
                new Item(5, 46.81f, 36),
                new Item(6, 48.77f, 79),
                new Item(7, 81.80f, 45),
                new Item(8, 19.36f, 79),
                new Item(9, 6.76f, 64)
        );
        List<Integer> expectedResult = List.of(8, 9);
        return new TestData(maxPackagerWeight, items, expectedResult);
    }

    private static TestData getTestDataGroup4() {
        int maxPackagerWeight = 70;
        List<Item> items = List.of(
                new Item(1, 20.1f, 60),
                new Item(2, 30.0f, 70),
                new Item(3, 40.0f, 80),
                new Item(4, 50.0f, 90),
                new Item(5, 60.0f, 100)
        );
        List<Integer> expectedResult = List.of(2, 3);
        return new TestData(maxPackagerWeight, items, expectedResult);
    }

    private static TestData getTestDataGroup5() {
        int maxPackagerWeight = 30;
        List<Item> items = List.of(
                new Item(1, 6.0f, 20),
                new Item(2, 7.0f, 30),
                new Item(3, 8.0f, 40),
                new Item(4, 9.0f, 50),
                new Item(5, 10.0f, 60)
        );
        List<Integer> expectedResult = List.of(3, 4, 5);
        return new TestData(maxPackagerWeight, items, expectedResult);
    }

    private static TestData getTestDataGroup6() {
        int maxPackagerWeight = 21;
        List<Item> items = List.of(
                new Item(1, 10.0f, 20),
                new Item(2, 10.0f, 20),
                new Item(3, 9.0f, 20),
                new Item(4, 8.0f, 20)
        );
        List<Integer> expectedResult = List.of(3, 4);
        return new TestData(maxPackagerWeight, items, expectedResult);
    }

    private record TestData(int maxPackagerWeight, List<Item> itemList, List<Integer> expectedResult) {
    }
}