package com.masoud.packager.adapters.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masoud.packager.domain.entities.Item;
import com.masoud.packager.adapters.rest.dto.ErrorResponseDto;
import com.masoud.packager.adapters.rest.dto.FillPackageInputDto;
import com.masoud.packager.adapters.rest.dto.FillPackageOutputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class PackagerRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String url = "/api/v1/packager";

    @Test
    public void shouldReturnOkResultWithExpectedValues() throws Exception {
        FillPackageInputDto inputDto = new FillPackageInputDto();
        inputDto.maxPackageWeight = 70;
        inputDto.items = List.of(
                new Item(1, 20.1f, 60),
                new Item(2, 30.0f, 70),
                new Item(3, 40.0f, 80),
                new Item(4, 50.0f, 90),
                new Item(5, 60.0f, 100)
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        FillPackageOutputDto outputDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FillPackageOutputDto.class);

        List<Integer> expectedResult = List.of(2, 3);

        Assertions.assertIterableEquals(expectedResult, outputDto.result());
    }

    @Test
    public void shouldReturnBadRequestResultWithExpectedValues() throws Exception {
        FillPackageInputDto inputDto = new FillPackageInputDto();
        inputDto.maxPackageWeight = 0;
        inputDto.items = List.of(
                new Item(1, 20.1f, 60),
                new Item(2, 30.0f, 70)
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ErrorResponseDto outputDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponseDto.class);

        Assertions.assertEquals(400, outputDto.statusCode());
        Assertions.assertEquals("Package weight is less than or equal to zero", outputDto.message());
    }
}