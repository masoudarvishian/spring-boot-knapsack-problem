package com.masoud.packager.application.services;

import com.masoud.packager.domain.entities.Item;
import com.masoud.packager.domain.exceptions.EmptyPackageException;
import com.masoud.packager.application.interfaces.KnapsackAlgorithm;
import com.masoud.packager.application.interfaces.PackagerService;
import com.masoud.packager.application.interfaces.PackagerValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class PackagerServiceImpl implements PackagerService {

    private final PackagerValidator validator;
    private final KnapsackAlgorithm algorithm;

    public PackagerServiceImpl(PackagerValidator validator, KnapsackAlgorithm algorithm) {
        this.validator = validator;
        this.algorithm = algorithm;
    }

    @Override
    public List<Integer> fillPackage(int maxPackagerWeight, List<Item> items) {
        validator.validateItems(maxPackagerWeight, items);
        List<Integer> optimalItems = this.algorithm.execute(maxPackagerWeight, items);
        if (optimalItems.isEmpty())
            throw new EmptyPackageException("no items can be placed in the package under the given constraints");
        return optimalItems;
    }

}
