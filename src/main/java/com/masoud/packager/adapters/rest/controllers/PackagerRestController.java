package com.masoud.packager.adapters.rest.controllers;

import com.masoud.packager.adapters.rest.dto.ErrorResponseDto;
import com.masoud.packager.adapters.rest.dto.FillPackageInputDto;
import com.masoud.packager.application.interfaces.PackagerService;
import com.masoud.packager.adapters.rest.dto.FillPackageOutputDto;
import com.masoud.packager.domain.exceptions.InvalidDataLimitException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@RestController
@RequestMapping(path = "api/v1/packager")
class PackagerRestController {

    private static final Logger logger = LoggerFactory.getLogger(PackagerRestController.class);

    private final PackagerService packagerService;

    @Autowired
    PackagerRestController(PackagerService packagerService) {
        this.packagerService = packagerService;
    }

    @Operation(summary = "Fill a package with items based on given constraints",
            description = "This endpoint processes a list of items and the maximum package weight, returning the list of " +
                    "item IDs that can optimally fit within the given weight limit.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully filled package",
                    content = @Content(schema = @Schema(implementation = FillPackageOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping()
    public FillPackageOutputDto fillPackage(@Valid @RequestBody FillPackageInputDto inputDto) {
        logger.info("[Endpoint] post -> /api/v1/packager/fillPackage was called");
        CompletableFuture<List<Integer>> futureResult = CompletableFuture.supplyAsync(() ->
            packagerService.fillPackage(inputDto.getMaxPackageWeight(), inputDto.getItems())
        );
        try {
            List<Integer> result = futureResult.join();
            return new FillPackageOutputDto(result);
        }
        catch (CompletionException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof InvalidDataLimitException) {
                throw (InvalidDataLimitException) cause;
            }
            throw ex;
        }
    }
}