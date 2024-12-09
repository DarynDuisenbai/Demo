package com.example.demo.controller;

import com.example.demo.dto.request.conclusion.CreateConclusionRequest;
import com.example.demo.dto.request.conclusion.EditSavedConclusionRequest;
import com.example.demo.dto.request.conclusion.FilterRequest;
import com.example.demo.dto.request.user.DecisionRequest;
import com.example.demo.dto.responce.*;
import com.example.demo.exception.*;
import com.example.demo.service.spec.ConclusionService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ConclusionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConclusionService conclusionService;

    @Test
    @WithMockUser
    void createConclusionTest() throws Exception {
        CreateConclusionRequest request = new CreateConclusionRequest();
        doNothing().when(conclusionService).createConclusion(request);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Replace with serialized JSON request
                .andExpect(status().isOk())
                .andExpect(content().string("Document successfully created."));
    }

    @Test
    @WithMockUser
    void turnConclusionTest() throws Exception {
        doNothing().when(conclusionService).turnToPermanent("12345");

        mockMvc.perform(post("/turn")
                        .param("registrationNumber", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().string("Document successfully created."));
    }

    @Test
    @WithMockUser
    void filterTest() throws Exception {
        Set<ConclusionDto> filteredResults = Set.of(new ConclusionDto());
        when(conclusionService.filter(any(FilterRequest.class))).thenReturn(filteredResults);

        mockMvc.perform(get("/filter")
                        .param("iin", "123456789012"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void myConclusionsTest() throws Exception {
        List<ConclusionDto> conclusions = List.of(new ConclusionDto());
        when(conclusionService.userConclusions("123456789012")).thenReturn(conclusions);

        mockMvc.perform(get("/usersDocs")
                        .param("IIN", "123456789012"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void myAgreementsTest() throws Exception {
        AgreementDto agreement = new AgreementDto();
        when(conclusionService.userAgreements("123456789012", "Z012")).thenReturn(agreement);

        mockMvc.perform(get("/usersAgreements")
                        .param("IIN", "123456789012")
                        .param("regNum", "Z012"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void myTempConclusionsTest() throws Exception {
        List<TempConclusionDto> tempConclusions = List.of(new TempConclusionDto());
        when(conclusionService.userSavedConclusions("123456789012")).thenReturn(tempConclusions);

        mockMvc.perform(get("/temps")
                        .param("IIN", "123456789012"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getSpecificConclusionTest() throws Exception {
        ConclusionDto conclusion = new ConclusionDto();
        when(conclusionService.getSpecific("12345", "123456789012")).thenReturn(conclusion);

        mockMvc.perform(get("/fullConclusion")
                        .param("regNumber", "12345")
                        .param("iin", "123456789012"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getSpecificTempConclusionTest() throws Exception {
        TempConclusionDto tempConclusion = new TempConclusionDto();
        when(conclusionService.getSpecificTemp("12345", "123456789012")).thenReturn(tempConclusion);

        mockMvc.perform(get("/fullTempConclusion")
                        .param("regNumber", "12345")
                        .param("iin", "123456789012"))
                .andExpect(status().isOk());
    }

    @Test
    void allUDTest() throws Exception {
        List<String> udList = List.of("UD1", "UD2");
        when(conclusionService.allUD()).thenReturn(udList);

        mockMvc.perform(get("/allUD"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void saveConclusionTest() throws Exception {
        CreateConclusionRequest request = new CreateConclusionRequest();
        doNothing().when(conclusionService).saveConclusion(request);

        mockMvc.perform(post("/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Document successfully saved."));
    }

    @Test
    @WithMockUser
    void editConclusionTest() throws Exception {
        EditSavedConclusionRequest request = new EditSavedConclusionRequest();
        doNothing().when(conclusionService).editSavedConclusion(request);

        mockMvc.perform(put("/edit")
                        .param("registrationNumber", "12345")
                        .param("iinOfInvestigator", "987654321012"))
                .andExpect(status().isOk())
                .andExpect(content().string("Document successfully edited."));
    }

    @Test
    @WithMockUser
    void remakeConclusionTest() throws Exception {
        EditSavedConclusionRequest request = new EditSavedConclusionRequest();
        doNothing().when(conclusionService).remakeConclusion(request);

        mockMvc.perform(put("/remake")
                        .param("registrationNumber", "12345")
                        .param("iinOfInvestigator", "987654321012"))
                .andExpect(status().isOk())
                .andExpect(content().string("Document successfully edited."));
    }

    @Test
    @WithMockUser
    void decisionTest() throws Exception {
        AgreementDto mockAgreement = new AgreementDto();

        when(conclusionService.makeDecision(any(DecisionRequest.class))).thenReturn(mockAgreement);

        mockMvc.perform(post("/decision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Decision was made."));
    }

    @Test
    @WithMockUser
    void historyTest() throws Exception {
        History history = new History();
        when(conclusionService.history("987654321012", "123456789012", "goal")).thenReturn(history);

        mockMvc.perform(get("/history")
                        .param("iinUser", "987654321012")
                        .param("iinOfCalled", "123456789012")
                        .param("goal", "goal"))
                .andExpect(status().isOk());
    }
}
