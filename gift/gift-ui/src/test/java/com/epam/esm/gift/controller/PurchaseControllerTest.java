package com.epam.esm.gift.controller;

import com.epam.esm.gift.model.Purchase;
import com.epam.esm.gift.service.PurchaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PurchaseControllerTest {
    @Mock
    private PurchaseServiceImpl purchaseServiceImpl;
    @InjectMocks
    private PurchaseController purchaseController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(purchaseController).build();
    }

    @Test
    void getAll() throws Exception {
        Purchase purchase1 = new Purchase();
        purchase1.setId(1);
        Purchase purchase2 = new Purchase();
        purchase2.setId(2);
        when(purchaseServiceImpl.getAll(10)).thenReturn(List.of(purchase1, purchase2));
        mockMvc.perform(get("/purchase").param("page","10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id",is(1)))
                .andExpect(jsonPath("$[1].id",is(2)));
        verify(purchaseServiceImpl, times(1)).getAll(10);
    }
}