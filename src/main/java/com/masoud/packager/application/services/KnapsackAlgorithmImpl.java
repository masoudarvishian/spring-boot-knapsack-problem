package com.masoud.packager.application.services;

import com.masoud.packager.application.interfaces.KnapsackAlgorithm;
import com.masoud.packager.domain.entities.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class KnapsackAlgorithmImpl implements KnapsackAlgorithm {

    @Override
    public List<Integer> execute(float maxWeight, List<Item> items) {
        List<List<Integer>> bestCombinations = findBestPriceCombinationsUnderWeight(maxWeight, items);
        return selectCombinationWithMinimumWeight(bestCombinations, items);
    }

    private List<List<Integer>> findBestPriceCombinationsUnderWeight(float maxWeight, List<Item> items) {
        int n = items.size();
        List<List<Integer>> bestCombinations = new ArrayList<>();
        int highestPrice = 0;

        for (int bitmask = 0; bitmask < (1 << n); bitmask++) {
            float accumulatedWeight = 0;
            int accumulatedPrice = 0;
            List<Integer> currentCombination = new ArrayList<>();

            for (int j = 0; j < n; j++) {
                if ((bitmask & (1 << j)) != 0) {
                    Item currentItem = items.get(j);
                    accumulatedWeight += currentItem.getWeight();
                    if (accumulatedWeight > maxWeight) break;
                    accumulatedPrice += currentItem.getPrice();
                    currentCombination.add(currentItem.getId());
                }
            }

            if (accumulatedWeight <= maxWeight) {
                if (accumulatedPrice > highestPrice) {
                    highestPrice = accumulatedPrice;
                    bestCombinations.clear();
                    bestCombinations.add(currentCombination);
                } else if (accumulatedPrice == highestPrice) {
                    bestCombinations.add(currentCombination);
                }
            }
        }
        return bestCombinations;
    }

    private List<Integer> selectCombinationWithMinimumWeight(List<List<Integer>> bestCombinations, List<Item> items) {
        float minimumWeight = Float.MAX_VALUE;
        List<Integer> lightestCombination = null;

        for (List<Integer> combination : bestCombinations) {
            float combinationWeight = calculateWeightFor(combination, items);
            if (combinationWeight < minimumWeight) {
                minimumWeight = combinationWeight;
                lightestCombination = combination;
            }
        }
        return lightestCombination;
    }

    private float calculateWeightFor(List<Integer> combination, List<Item> items) {
        float totalWeight = 0;
        for (Integer itemId : combination) {
            totalWeight += items.stream()
                    .filter(item -> item.getId() == itemId)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid item ID"))
                    .getWeight();
        }
        return totalWeight;
    }
}
