package com.epam.esm.gift.controller;

import com.epam.esm.gift.model.Tag;
import com.epam.esm.gift.model.User;
import com.epam.esm.gift.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {
    @Mock
    private UserServiceImpl userServiceImpl;
    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getMostUsedTag() throws Exception {
        Tag tag = new Tag();
        tag.setId(4);
        tag.setName("METRO");
        when(userServiceImpl.getMostUsedTag(3)).thenReturn(tag);
        mockMvc.perform(get("/user/3/tag")).andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("METRO")));
        verify(userServiceImpl, times(1)).getMostUsedTag(3);
    }

    @Test
    void getUser() throws Exception {
        User user = new User();
        user.setId(3);
        user.setName("user");
        user.setAccount(2000);
        when(userServiceImpl.getById(3)).thenReturn(user);
        mockMvc.perform(get("/user/3")).andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("user")));
        verify(userServiceImpl, times(1)).getById(3);
    }

    @Test
    void makePurchase() throws Exception {
        User user = new User();
        user.setId(3);
        user.setName("user");
        user.setAccount(2000);
        user.setPurchases(new ArrayList<>());
        when(userServiceImpl.makePurchase(3, 2)).thenReturn(true);
        mockMvc.perform(post("/user/3/makePurchase").param("certificateId", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(jsonPath("$", is("Purchase was made successfully")));
        verify(userServiceImpl, times(1)).makePurchase(3, 2);
    }
}