package com.example.demo.controller;

import com.example.demo.dto.request.conclusion.CreateConclusionRequest;
import com.example.demo.dto.request.conclusion.EditSavedConclusionRequest;
import com.example.demo.dto.request.conclusion.FilterRequest;
import com.example.demo.dto.request.user.DecisionRequest;
import com.example.demo.dto.responce.AgreementDto;
import com.example.demo.dto.responce.ConclusionDto;
import com.example.demo.dto.responce.History;
import com.example.demo.dto.responce.TempConclusionDto;
import com.example.demo.exception.handler.AuthExceptionHandler;
import com.example.demo.exception.handler.ConclusionExceptionHandler;
import com.example.demo.service.spec.ConclusionService;
import com.example.demo.service.spec.UserService;
import com.example.demo.util.JwtRequestFilter;
import com.example.demo.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ConclusionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ConclusionService conclusionService;

    @InjectMocks
    private ConclusionController conclusionController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(conclusionController)
                .setControllerAdvice(new ConclusionExceptionHandler(), new AuthExceptionHandler())
                .build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }


    @Test
    public void createConclusion_Successful() throws Exception {
        CreateConclusionRequest request = new CreateConclusionRequest();
        request.setRelatesToBusiness("Somehow");
        request.setRegion("Astana");
        request.setIINOfInvestigator("050411550617");
        request.setEventDateTime(LocalDateTime.now());
        request.setUD("456789875461235");
        request.setRelation("Some");
        request.setPlannedActions("Do some investigations");
        request.setInvestigationType("Criminality");
        request.setBIN_IIN("123465");
        request.setEventPlace("Judge");
        request.setIINOfCalled("123456789876");
        request.setIINDefender("987654321213");
        request.setJustification("YES");
        request.setResult("");

        doNothing().when(conclusionService).createConclusion(request);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Document successfully created."));
    }

    @Test
    public void turnConclusion_Successful() throws Exception {
        String registrationNumber = "Z099";

        doNothing().when(conclusionService).turnToPermanent(registrationNumber);

        mockMvc.perform(post("/turn")
                        .param("registrationNumber", registrationNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("Document successfully created."));
    }

    @Test
    public void filterConclusions_Successful() throws Exception {//
        String iin = "050411550617";
        ConclusionDto conclusionDto1 = new ConclusionDto();
        conclusionDto1.setInvestigatorIIN(iin);
        conclusionDto1.setRegistrationNumber("Z099");
        conclusionDto1.setStatus("Отказано");

        ConclusionDto conclusionDto2 = new ConclusionDto();
        conclusionDto2.setInvestigatorIIN(iin);
        conclusionDto2.setRegistrationNumber("Z098");
        conclusionDto2.setStatus("Согласовано");

        Set<ConclusionDto> mockResults = Set.of(conclusionDto1);

        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setIIN(iin);
        filterRequest.setStatus("Отказано");
        when(conclusionService.filter(filterRequest)).thenReturn(mockResults);

        MvcResult result = mockMvc.perform(get("/filter")
                        .param("iin", iin)
                        .param("status", "Отказано"))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response is: " + responseContent);
        assertFalse(responseContent.isEmpty());
    }

    @Test
    public void myConclusions_Successful() throws Exception {
        String IIN = "050411550617";
        List<ConclusionDto> mockConclusions = List.of(new ConclusionDto());

        when(conclusionService.userConclusions(IIN)).thenReturn(mockConclusions);

        MvcResult result = mockMvc.perform(get("/usersDocs")
                        .param("IIN", IIN))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertFalse(responseContent.isEmpty());
    }

    @Test
    public void myAgreements_Successful() throws Exception {
        String IIN = "050411550617";
        List<AgreementDto> mockAgreements = List.of(new AgreementDto());

        when(conclusionService.userAgreements(IIN)).thenReturn(mockAgreements);

        MvcResult result = mockMvc.perform(get("/usersAgreements")
                        .param("IIN", IIN))
                .andExpect(status().isOk()).andReturn();
        String responseContent = result.getResponse().getContentAsString();
        assertFalse(responseContent.isEmpty());
    }

    @Test
    public void myTempConclusions_Successful() throws Exception {
        String IIN = "050411550617";
        List<TempConclusionDto> mockConclusions = List.of(new TempConclusionDto());

        when(conclusionService.userSavedConclusions(IIN)).thenReturn(mockConclusions);

        MvcResult result = mockMvc.perform(get("/temps")
                        .param("IIN", IIN))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertFalse(responseContent.isEmpty());
    }

    @Test
    public void getSpecificConclusion_Successful() throws Exception {//
        String regNumber = "Z099";
        String iin = "050411550617";
        ConclusionDto mockConclusion = new ConclusionDto();
        mockConclusion.setRegistrationNumber(regNumber);

        when(conclusionService.getSpecific(regNumber, iin)).thenReturn(mockConclusion);

        MvcResult result = mockMvc.perform(get("/fullConclusion")
                        .param("regNumber", regNumber)
                        .param("iin", iin))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);
    }

    @Test
    public void allUD_Successful() throws Exception {
        List<String> mockUDList = List.of("UD1", "UD2");

        when(conclusionService.allUD()).thenReturn(mockUDList);

        MvcResult result = mockMvc.perform(get("/allUD"))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertFalse(responseContent.isEmpty());
    }

    @Test
    public void save_Successful() throws Exception {
        CreateConclusionRequest request = new CreateConclusionRequest();
        request.setRegion("Astana");
        request.setIINOfInvestigator("050411550617");
        request.setEventDateTime(LocalDateTime.now());
        request.setUD("456789875461235");
        request.setRelation("Some");
        request.setPlannedActions("Do some investigations");
        request.setInvestigationType("Criminality");
        request.setBIN_IIN("123465");
        request.setEventPlace("Judge");
        request.setIINOfCalled("123456789876");
        request.setIINDefender("987654321213");
        request.setJustification("YES");
        request.setResult("");

        doNothing().when(conclusionService).saveConclusion(request);

        mockMvc.perform(post("/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Document successfully saved."));
    }

    @Test
    public void edit_Successful() throws Exception {
        String iin = "050411550617";
        String registrationNumber = "Z099";
        CreateConclusionRequest request = new CreateConclusionRequest();
        request.setRegion("Astana");

        EditSavedConclusionRequest editRequest = new EditSavedConclusionRequest();
        editRequest.setRegistrationNumber(registrationNumber);
        editRequest.setCreateConclusionRequest(request);

        doNothing().when(conclusionService).editSavedConclusion(editRequest);

        mockMvc.perform(put("/edit")
                        .param("registrationNumber", registrationNumber)
                        .param("iinOfInvestigator", iin)
                        .param("region", request.getRegion())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Document successfully edited."));
    }

    @Test
    public void remake_Successful() throws Exception {
        String iin = "050411550617";
        String registrationNumber = "Z099";
        CreateConclusionRequest request = new CreateConclusionRequest();
        request.setRegion("Astana");

        EditSavedConclusionRequest editRequest = new EditSavedConclusionRequest();
        editRequest.setRegistrationNumber(registrationNumber);
        editRequest.setCreateConclusionRequest(request);

        doNothing().when(conclusionService).remakeConclusion(editRequest);

        mockMvc.perform(put("/remake")
                        .param("registrationNumber", registrationNumber)
                        .param("iinOfInvestigator", iin)
                        .param("region", request.getRegion())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Document successfully edited."));
    }

    @Test
    public void decision_Successful() throws Exception {
        DecisionRequest request = new DecisionRequest();
        request.setStatus("Согласовано");

        when(conclusionService.makeDecision(request)).thenReturn(new AgreementDto());

        MvcResult result = mockMvc.perform(post("/decision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Decision was made.")).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertFalse(responseContent.isEmpty());

    }

    @Test
    public void history_Successful() throws Exception {//
        String iin = "050411550617";
        String goal = "For review";
        History mockHistory = new History();
        mockHistory.setAgreements(new ArrayList<>());
        mockHistory.setGoal(goal);
        when(conclusionService.history(iin, goal)).thenReturn(mockHistory);

        MvcResult result = mockMvc.perform(get("/history")
                        .param("iinOfCalled", iin)
                        .param("goal", goal))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertFalse(responseContent.isEmpty());

    }


}
