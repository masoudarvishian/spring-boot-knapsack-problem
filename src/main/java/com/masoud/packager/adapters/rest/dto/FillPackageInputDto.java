package com.masoud.packager.adapters.rest.dto;

import com.masoud.packager.domain.entities.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class FillPackageInputDto {
    @NotNull
    private int maxPackageWeight;
    @NotEmpty
    private List<Item> items;
}
