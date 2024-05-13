package com.masoud.packager.adapters.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class FillPackageOutputDto {
    private final List<Integer> result;
}
