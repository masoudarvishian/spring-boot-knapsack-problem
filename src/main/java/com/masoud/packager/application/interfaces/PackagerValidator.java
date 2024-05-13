package com.masoud.packager.application.interfaces;

import com.masoud.packager.domain.entities.Item;

import java.util.List;

public interface PackagerValidator {
    void validateItems(int maxPackagerWeight, List<Item> items) throws IllegalArgumentException;
}
