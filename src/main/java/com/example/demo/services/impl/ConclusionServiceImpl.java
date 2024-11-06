package com.example.demo.services.impl;

import com.example.demo.dtos.requests.CreateConclusionRequest;
import com.example.demo.dtos.requests.FilterRequest;
import com.example.demo.dtos.requests.ShortConclusionRequest;
import com.example.demo.dtos.requests.UserConclusionRequest;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.mappers.ConclusionMapper;
import com.example.demo.models.Conclusion;
import com.example.demo.models.User;
import com.example.demo.repository.ConclusionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.ConclusionService;
import com.example.demo.utils.Generator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConclusionServiceImpl implements ConclusionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConclusionServiceImpl.class);
    private final ConclusionRepository conclusionRepository;
    private final UserRepository userRepository;
    private final ConclusionMapper conclusionMapper;
    private final Generator generator;

    @Override
    public void createConclusion(CreateConclusionRequest createConclusionRequest) throws UserNotFoundException {
        LOGGER.debug("Creating a new conclusion");
        Conclusion conclusion = conclusionMapper.fromCreateToConclusion(createConclusionRequest);

        conclusion.setRegistrationNumber(generator.generateUniqueNumber());
        conclusion.setCreationDate(LocalDate.now());

       /* conclusion.setRegistrationDate(fetchRegistrationDate(createConclusionRequest.getUD()));
        conclusion.setArticle(fetchArticleByUD(createConclusionRequest.getUD()));
        conclusion.setDecision(fetchDecisionByUD(createConclusionRequest.getUD()));
        conclusion.setPlot(fetchSummaryByUD(createConclusionRequest.getUD()));

        conclusion.setFullNameOfCalled(fetchFullNameByIIN(createConclusionRequest.getIIN()));
        conclusion.setFullNameOfDefender(fetchFullNameByIIN(createConclusionRequest.getIINDefender()));

        conclusion.setInvestigator(getCurrentUserInvestigator());*/

        String userIIN = createConclusionRequest.getIIN();
        User user = userRepository.findByIIN(userIIN).orElseThrow(() -> new UserNotFoundException("User not found."));

        List<Conclusion> userConclusions = user.getConclusions();
        userConclusions.add(conclusion);

        conclusionRepository.save(conclusion);

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
    public List<ConclusionDto> userConclusions(UserConclusionRequest userConclusionRequest) throws UserNotFoundException {
        LOGGER.debug("Retrieving user conclusions...");
        User user = userRepository.findByEmail(userConclusionRequest.getIIN()).orElseThrow(()-> new UserNotFoundException("User not found."));
        return conclusionMapper.toDtoList(user.getConclusions());
    }
}
