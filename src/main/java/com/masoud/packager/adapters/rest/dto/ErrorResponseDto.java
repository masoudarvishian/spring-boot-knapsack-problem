package com.masoud.packager.adapters.rest.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorResponseDto {
    private final int statusCode;
    private final String message;
}
