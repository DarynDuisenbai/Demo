package com.example.demo.services.impl;

import com.example.demo.dtos.requests.*;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.dtos.responces.TempConclusionDto;
import com.example.demo.exceptions.NoTemporaryConclusionFound;
import com.example.demo.exceptions.RegionNotFoundException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.mappers.ConclusionMapper;
import com.example.demo.mappers.TempMapper;
import com.example.demo.models.*;
import com.example.demo.repository.*;
import com.example.demo.services.ConclusionService;
import com.example.demo.utils.Generator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConclusionServiceImpl implements ConclusionService {
    private final String DEFAULT_STATUS = "Send for revision";
    private final String SAVED = "In work";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConclusionServiceImpl.class);
    private final ConclusionRepository conclusionRepository;
    private final TemporaryConclusionRepository temporaryConclusionRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final StatusRepository statusRepository;
    private final RegionRepository regionRepository;
    private final ConclusionMapper conclusionMapper;
    private final TempMapper tempMapper;
    private final Generator generator;

    @Override
    public void createConclusion(CreateConclusionRequest createConclusionRequest) throws UserNotFoundException, RegionNotFoundException {
        LOGGER.debug("Creating a new conclusion");
        Conclusion conclusion = conclusionMapper.fromCreateToConclusion(createConclusionRequest);

        conclusion.setRegistrationNumber(generator.generateUniqueNumber());
        conclusion.setCreationDate(LocalDateTime.now());

        Status status = statusRepository.findByName(DEFAULT_STATUS);
        conclusion.setStatus(status);
       /* conclusion.setRegistrationDate(fetchRegistrationDate(createConclusionRequest.getUD()));
        conclusion.setArticle(fetchArticleByUD(createConclusionRequest.getUD()));
        conclusion.setDecision(fetchDecisionByUD(createConclusionRequest.getUD()));
        conclusion.setPlot(fetchSummaryByUD(createConclusionRequest.getUD()));

        conclusion.setFullNameOfCalled(fetchFullNameByIIN(createConclusionRequest.getIIN()));
        conclusion.setFullNameOfDefender(fetchFullNameByIIN(createConclusionRequest.getIINDefender()));

        conclusion.setInvestigator(getCurrentUserInvestigator());*/

        String userIIN = createConclusionRequest.getIIN();
        LOGGER.warn("IIN IS " + userIIN);
        User user = userRepository.findByIIN(userIIN).orElseThrow(() -> new UserNotFoundException("User not found."));

        List<Conclusion> userConclusions = user.getConclusions();
        userConclusions.add(conclusion);

        conclusionRepository.save(conclusion);
        userRepository.save(user);

    }
   /* private LocalDate fetchRegistrationDate(String ud) {
        // Implement fetching registration date based on UD
    }

    private String fetchArticleByUD(String ud) {
        // Implement fetching article based on UD
    }

    private String fetchDecisionByUD(String ud) {
        // Implement fetching decision based on UD
    }

    private String fetchSummaryByUD(String ud) {
        // Implement fetching summary based on UD
    }

    private String fetchFullNameByIIN(String iin) {
        // Implement fetching full name based on IIN
    }

    private String getCurrentUserInvestigator() {
        // Implement fetching investigator from user profile
    }*/

    @Override
    public void saveConclusion(CreateConclusionRequest createConclusionRequest)
            throws RegionNotFoundException, UserNotFoundException {
        LOGGER.debug("Saving a temporary conclusion.");
        TemporaryConclusion temporaryConclusion = tempMapper.fromCreateToTemp(createConclusionRequest);
        temporaryConclusion.setRegistrationNumber(generator.generateUniqueNumber());
        temporaryConclusion.setCreationDate(LocalDateTime.now());

        Status status = statusRepository.findByName(SAVED);
        temporaryConclusion.setStatus(status);

        String userIIN = createConclusionRequest.getIIN();
        LOGGER.warn("IIN IS " + userIIN);
        User user = userRepository.findByIIN(userIIN).orElseThrow(() -> new UserNotFoundException("User not found."));

        List<TemporaryConclusion> userConclusions = user.getTemporaryConclusions();
        userConclusions.add(temporaryConclusion);

        temporaryConclusionRepository.save(temporaryConclusion);
        userRepository.save(user);
        LOGGER.debug("Temporary conclusion saved.");
    }

    @Override
    public void editSavedConclusion(EditSavedConclusionRequest editSavedConclusionRequest) throws UserNotFoundException, NoTemporaryConclusionFound, RegionNotFoundException {
        LOGGER.debug("Editing a temporary conclusion.");
        String userIIN = editSavedConclusionRequest.getCreateConclusionRequest().getIIN();
        LOGGER.warn("IIN IS " + userIIN);
        User user = userRepository.findByIIN(userIIN).orElseThrow(() -> new UserNotFoundException("User not found."));
        user.getTemporaryConclusions().removeIf(temporaryConclusion ->
                temporaryConclusion.getRegistrationNumber().equals(editSavedConclusionRequest.getRegistrationNumber())
        );

        TemporaryConclusion temporaryConclusion = temporaryConclusionRepository.
                findTemporaryConclusionByRegistrationNumber(editSavedConclusionRequest.getRegistrationNumber()).
                orElseThrow(() -> new NoTemporaryConclusionFound("No temporary conclusions found."));

        CreateConclusionRequest request = editSavedConclusionRequest.getCreateConclusionRequest();
        temporaryConclusion.setUD(request.getUD());
        temporaryConclusion.setIINofCalled(request.getIIN());
        temporaryConclusion.setBINorIINOfCalled(request.getBIN_IIN());
        temporaryConclusion.setJobTitleOfCalled(request.getJobTitle());
        Region region = regionRepository.findRegionByName(request.getRegion()).
                orElseThrow(() -> new RegionNotFoundException("Region not found."));

        temporaryConclusion.setRegion(region);
        Status status = statusRepository.findByName(SAVED);
        temporaryConclusion.setStatus(status);

        temporaryConclusion.setPlannedActions(request.getPlannedActions());
        temporaryConclusion.setEventTime(request.getEventDateTime());
        temporaryConclusion.setEventPlace(request.getEventPlace());
        temporaryConclusion.setRelation(request.getRelation());
        temporaryConclusion.setInvestigation(request.getInvestigationType());
        temporaryConclusion.setBusiness(request.getRelatesToBusiness() != null ? request.getRelatesToBusiness() : false);
        temporaryConclusion.setIINDefender(request.getIINDefender());
        temporaryConclusion.setJustification(request.getJustification());
        temporaryConclusion.setResult(request.getResult());

        temporaryConclusionRepository.save(temporaryConclusion);

        user.getTemporaryConclusions().add(temporaryConclusion);
        userRepository.save(user);
        LOGGER.debug("Temporary conclusion edited.");
    }

    @Override
    public void turnToPermanent(String registrationNumber) throws UserNotFoundException, NoTemporaryConclusionFound {
        TemporaryConclusion tempConclusion = temporaryConclusionRepository.findTemporaryConclusionByRegistrationNumber(registrationNumber).
                orElseThrow(()-> new NoTemporaryConclusionFound("No temporary conclusion found."));

        Conclusion conclusion = conclusionMapper.fromTempToConclusion(tempConclusion);
        conclusionRepository.save(conclusion);

        User user = userRepository.findByIIN(tempConclusion.getIINofCalled()).orElseThrow(() -> new UserNotFoundException("User not found."));
        user.getTemporaryConclusions().removeIf(temporaryConclusion ->
                temporaryConclusion.getRegistrationNumber().equals(tempConclusion.getRegistrationNumber())
        );
        user.getConclusions().add(conclusion);
        userRepository.save(user);
        temporaryConclusionRepository.delete(tempConclusion);
    }

    @Override
    public List<ConclusionDto> filter(FilterRequest filterRequest){
        LOGGER.debug("Filtering...");
        List<Conclusion> filteredConclusions = conclusionRepository.filterConclusions(filterRequest);
        return conclusionMapper.toDtoList(filteredConclusions);
    }

    /*@Override
    public List<AgreementDto> agreement() throws UserNotFoundException {

        User user = userRepository.findByEmail(agreementDto.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found."));
        return conclusionMapper.toShortDto(user.getConclusions());
    }*/

    @Override
    public List<ConclusionDto> userConclusions(String IIN) throws UserNotFoundException {
        LOGGER.debug("Retrieving user conclusions...");
        User user = userRepository.findByIIN(IIN).orElseThrow(()-> new UserNotFoundException("User not found."));
        Department userDep = user.getDepartment();
        String job = user.getJob().getName();
        if(job.equals("EMPLOYEE")){
            return conclusionMapper.toDtoList(user.getConclusions());
        }else if(job.equals("ANALYST")){
            List<User> users = userRepository.findByDepartment(userDep.getName());
            List<Conclusion> allConclusions = users.stream()
                    .flatMap(deptUser -> deptUser.getConclusions().stream())
                    .collect(Collectors.toList());
            return conclusionMapper.toDtoList(allConclusions);
        }else {
            return getAllConclusions();
        }
    }

    @Override
    public List<TempConclusionDto> userSavedConclusions(String IIN) throws UserNotFoundException {
        LOGGER.debug("Retrieving user saved conclusions...");
        User user = userRepository.findByIIN(IIN).orElseThrow(()-> new UserNotFoundException("User not found."));
        return tempMapper.toDtoList(user.getTemporaryConclusions());
    }

    @Override
    public List<String> allUD() {
        return conclusionRepository.findAll().stream().map(Conclusion::getUD).toList();
    }

    @Override
    public List<ConclusionDto> getAllConclusions() {
        return conclusionMapper.toDtoList(conclusionRepository.findAll());
    }
}
