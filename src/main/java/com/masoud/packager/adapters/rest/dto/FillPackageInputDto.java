package com.masoud.packager.adapters.rest.dto;

import com.masoud.packager.domain.entities.Item;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class FillPackageInputDto {
    @NotNull
    public int maxPackageWeight;
    @NotEmpty
    public List<Item> items;
}
