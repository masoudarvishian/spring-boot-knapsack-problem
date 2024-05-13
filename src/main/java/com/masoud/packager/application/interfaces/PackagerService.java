package com.masoud.packager.application.interfaces;

import com.masoud.packager.domain.entities.Item;
import com.masoud.packager.domain.exceptions.DuplicateIdException;
import com.masoud.packager.domain.exceptions.EmptyPackageException;
import com.masoud.packager.domain.exceptions.InvalidDataLimitException;

import java.util.List;

public interface PackagerService {
    /**
     * Fills the package with the optimal set of items based on weight and other constraints.
     *
     * @param maxPackagerWeight the maximum weight the package can hold
     * @param items the list of items to be packed
     * @return the list of optimal item IDs that fit within the package
     * @throws EmptyPackageException if no items can be placed in the package
     * @throws InvalidDataLimitException if the data provided for the items or package do not meet validation criteria
     * @throws DuplicateIdException if duplicate item IDs are found during validation
     */
    List<Integer> fillPackage(int maxPackagerWeight, List<Item> items);
}
