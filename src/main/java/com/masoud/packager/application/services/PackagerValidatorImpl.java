package com.masoud.packager.application.services;

import com.masoud.packager.domain.entities.Item;
import com.masoud.packager.domain.exceptions.DuplicateIdException;
import com.masoud.packager.domain.exceptions.InvalidDataLimitException;
import com.masoud.packager.application.interfaces.PackagerLimits;
import com.masoud.packager.application.interfaces.PackagerValidator;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
class PackagerValidatorImpl implements PackagerValidator {

    private final PackagerLimits packageLimits;

    public PackagerValidatorImpl(PackagerLimits packageLimits) {
        this.packageLimits = packageLimits;
    }

    @Override
    public void validateItems(int maxPackagerWeight, List<Item> items) throws InvalidDataLimitException, DuplicateIdException {
        if (items == null || items.isEmpty())
            throw new InvalidDataLimitException("Items list is empty");

        validateItemProperties(items);
        validateItemCountsAgainstLimits(items);
        validatePackageWeightLimit(maxPackagerWeight);
        ensureUniqueItemIds(items);
    }

    private void validateItemProperties(List<Item> items) throws InvalidDataLimitException {
        for (Item item : items) {
            if (item.id() <= 0 || item.weight() <= 0 || item.price() <= 0)
                throw new InvalidDataLimitException("Item Id, weight or price is less than or equal to zero");

            if (item.weight() > packageLimits.getMaxItemWeightLimit() || item.price() > packageLimits.getMaxItemPriceLimit())
                throw new InvalidDataLimitException("Item weight or price exceeds maximum limit");
        }
    }

    private void validateItemCountsAgainstLimits(List<Item> items) throws InvalidDataLimitException {
        if (items.size() > packageLimits.getMaxItemsLimit())
            throw new InvalidDataLimitException("Number of items exceeds maximum limit");
    }

    private void validatePackageWeightLimit(int maxPackagerWeight) throws InvalidDataLimitException {
        if (maxPackagerWeight <= 0)
            throw new InvalidDataLimitException("Package weight is less than or equal to zero");
        if (maxPackagerWeight > packageLimits.getMaxPackageWeight())
            throw new InvalidDataLimitException("Package weight exceeds maximum limit");
    }

    private void ensureUniqueItemIds(List<Item> items) throws DuplicateIdException {
        Set<Integer> ids = new HashSet<>();
        for (Item item : items) {
            if (!ids.add(item.id()))
                throw new DuplicateIdException("Duplicate item ID found: " + item.id());
        }
    }
}
