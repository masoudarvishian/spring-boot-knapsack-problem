package com.masoud.packager.application.interfaces;

import com.masoud.packager.domain.entities.Item;
import org.springframework.stereotype.Service;

import java.util.List;

public interface KnapsackAlgorithm {
    List<Integer> execute(float maxWeight, List<Item> items);
}
