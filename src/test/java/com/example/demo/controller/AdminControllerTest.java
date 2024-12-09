package com.example.demo.controller;

import com.example.demo.dto.responce.AgreementDto;
import com.example.demo.dto.responce.ConclusionDto;
import com.example.demo.exception.*;
import com.example.demo.mapper.spec.AgreementMapper;
import com.example.demo.mapper.spec.ConclusionMapper;
import com.example.demo.domain.Agreement;
import com.example.demo.domain.Conclusion;
import com.example.demo.service.spec.AgreementService;
import com.example.demo.service.spec.ConclusionService;
import com.example.demo.service.spec.UserService;
import com.example.demo.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ConclusionMapper conclusionMapper;

    @MockBean
    private AgreementMapper agreementMapper;

    @MockBean
    private ConclusionService conclusionService;

    @MockBean
    private AgreementService agreementService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @WithMockUser
    void promote_success() throws Exception {
        Mockito.doNothing().when(userService).promote(anyString());

        mockMvc.perform(patch("/admin/promote")
                        .param("IIN", "1234567890"))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully promoted."));
    }

    @Test
    @WithMockUser
    void getAllConclusions_success() throws Exception {
        List<Conclusion> conclusions = Arrays.asList(new Conclusion(), new Conclusion());
        List<ConclusionDto> conclusionDtos = Arrays.asList(new ConclusionDto(), new ConclusionDto());

        Mockito.when(conclusionService.getAllConclusions()).thenReturn(conclusions);
        Mockito.when(conclusionMapper.toDtoList(conclusions)).thenReturn(conclusionDtos);

        mockMvc.perform(get("/admin/allConclusions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser
    void getAllAgreements_success() throws Exception {
        List<Agreement> agreements = Arrays.asList(new Agreement(), new Agreement());
        List<AgreementDto> agreementDtos = Arrays.asList(new AgreementDto(), new AgreementDto());

        Mockito.when(agreementService.getAll()).thenReturn(agreements);
        Mockito.when(agreementMapper.toDtoList(agreements)).thenReturn(agreementDtos);

        mockMvc.perform(get("/admin/allAgreements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser
    void deleteConclusion_success() throws Exception {
        Mockito.doNothing().when(conclusionService).deleteConclusion(anyString());

        mockMvc.perform(delete("/admin/deleteConclusion")
                        .param("registrationNumber", "reg123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Conclusion successfully deleted."));
    }

    @Test
    @WithMockUser
    void deleteConclusion_notReady() throws Exception {
        Mockito.doThrow(new ConclusionNotReadyException("Conclusion not ready"))
                .when(conclusionService).deleteConclusion(anyString());

        mockMvc.perform(delete("/admin/deleteConclusion")
                        .param("registrationNumber", "reg123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Conclusion not ready"));
    }

    @Test
    @WithMockUser
    void promote_userNotFound() throws Exception {
        Mockito.doThrow(new UserNotFoundException("User not found"))
                .when(userService).promote(anyString());

        mockMvc.perform(patch("/admin/promote")
                        .param("IIN", "1234567890"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}
