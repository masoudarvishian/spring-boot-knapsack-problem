package com.masoud.packager.domain.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Item {
    private final int id;
    private final float weight;
    private final int price;
}