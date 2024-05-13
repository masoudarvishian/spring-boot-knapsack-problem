package com.masoud.packager.application.services;

import com.masoud.packager.application.interfaces.PackagerLimits;
import org.springframework.stereotype.Service;

@Service
class PackagerLimitsImpl implements PackagerLimits {
    private static final int MAX_ITEMS = 15;
    private static final int MAX_ITEM_WEIGHT = 100;
    private static final int MAX_ITEM_PRICE = 100;
    private static final int MAX_PACKAGE_WEIGHT = 100;

    @Override
    public int getMaxItemsLimit() {
        return MAX_ITEMS;
    }

    @Override
    public float getMaxItemWeightLimit() {
        return MAX_ITEM_WEIGHT;
    }

    @Override
    public int getMaxItemPriceLimit() {
        return MAX_ITEM_PRICE;
    }

    @Override
    public int getMaxPackageWeight() {
        return MAX_PACKAGE_WEIGHT;
    }
}