package com.example.demo.service.impl;

import com.example.demo.dto.request.conclusion.CreateConclusionRequest;
import com.example.demo.dto.request.conclusion.EditSavedConclusionRequest;
import com.example.demo.dto.request.conclusion.FilterRequest;
import com.example.demo.dto.request.user.DecisionRequest;
import com.example.demo.dto.responce.AgreementDto;
import com.example.demo.dto.responce.ConclusionDto;
import com.example.demo.dto.responce.History;
import com.example.demo.dto.responce.TempConclusionDto;
import com.example.demo.exception.*;
import com.example.demo.mapper.spec.AgreementMapper;
import com.example.demo.mapper.spec.ConclusionMapper;
import com.example.demo.mapper.spec.HistoryMapper;
import com.example.demo.mapper.spec.TempMapper;
import com.example.demo.domain.*;
import com.example.demo.repository.spec.*;
import com.example.demo.service.spec.ConclusionService;
import com.example.demo.util.Generator;
import com.example.demo.util.UTCFormatter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ConclusionServiceImpl implements ConclusionService {
    private final String DEFAULT_STATUS = "На согласовании";
    private final String SAVED = "В работе";
    private final String EMPLOYEE = "Сотрудник СУ";
    private final String ANALYST = "Аналитик СД";
    private final Logger LOGGER = LoggerFactory.getLogger(ConclusionServiceImpl.class);
    private final ConclusionRepository conclusionRepository;
    private final CaseRepository caseRepository;
    private final TemporaryConclusionRepository temporaryConclusionRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final RegionRepository regionRepository;
    private final AgreementRepository agreementRepository;
    private final ConclusionMapper conclusionMapper;
    private final TempMapper tempMapper;
    private final AgreementMapper agreementMapper;
    private final HistoryMapper historyMapper;
    private final Generator generator;
    private final UTCFormatter utcFormatter;

    @Override
    public void sendConclusion(Conclusion conclusion, String IIN) throws UserNotFoundException {
        User user = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User not found."));
        user.getReceivedConclusionsRegNumbers().add(conclusion.getRegistrationNumber());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createConclusion(CreateConclusionRequest createConclusionRequest) throws UserNotFoundException, RegionNotFoundException, CaseNotFound {
        LOGGER.debug("Creating a new conclusion");
        Conclusion conclusion = conclusionMapper.fromCreateToConclusion(createConclusionRequest);

        conclusion.setRegistrationNumber(generator.generateUniqueNumber());
        conclusion.setCreationDate(utcFormatter.convertUTCToUTCPlus5(LocalDateTime.now()));

        Status status = statusRepository.findByName(DEFAULT_STATUS);
        conclusion.setStatus(status);

        Case relatedCase = assignCase(createConclusionRequest.getUD());
        conclusion.setRegistrationDate(utcFormatter.convertUTCToUTCPlus5(relatedCase.getRegistrationDate()));
        conclusion.setUD(relatedCase.getUD());
        conclusion.setArticle(relatedCase.getArticle());
        conclusion.setDecision(relatedCase.getDecision());
        conclusion.setPlot(relatedCase.getSummary());

        String calledName = generator.generateNames();
        String defenderName = generator.generateNames();

        while (defenderName.equals(calledName)) {
            defenderName = generator.generateNames();
        }

        conclusion.setFullNameOfCalled(calledName);
        conclusion.setFullNameOfDefender(defenderName);

        String iinInvestigator = createConclusionRequest.getIINOfInvestigator();
        conclusion.setInvestigatorIIN(iinInvestigator);

        User investigator = userRepository.findByIIN(iinInvestigator).
                orElseThrow(() -> new UserNotFoundException("User not found."));

        User analystOfDep = userRepository.findAnalystByDepartment(
                investigator.getDepartment().getName());

        sendConclusion(conclusion, analystOfDep.getIIN());

        List<String> userConclusions = investigator.getConclusionsRegNumbers();
        userConclusions.add(conclusion.getRegistrationNumber());

        conclusionRepository.save(conclusion);
        userRepository.save(investigator);

    }

    private Case assignCase(String UD) throws CaseNotFound {
        return caseRepository.findCaseByUD(UD).orElseThrow(()-> new CaseNotFound("Case not found."));
    }

    @Override
    @Transactional
    public void saveConclusion(CreateConclusionRequest createConclusionRequest)
            throws RegionNotFoundException, UserNotFoundException, CaseNotFound {
        LOGGER.debug("Saving a temporary conclusion.");
        TemporaryConclusion temporaryConclusion = tempMapper.fromCreateToTemp(createConclusionRequest);

        temporaryConclusion.setRegistrationNumber(generator.generateUniqueNumber());
        temporaryConclusion.setCreationDate(LocalDateTime.now());

        Status status = statusRepository.findByName(SAVED);
        temporaryConclusion.setStatus(status);

        Case relatedCase = assignCase(createConclusionRequest.getUD());
        temporaryConclusion.setRegistrationDate(utcFormatter.convertUTCToUTCPlus5(relatedCase.getRegistrationDate()));
        temporaryConclusion.setUD(relatedCase.getUD());
        temporaryConclusion.setArticle(relatedCase.getArticle());
        temporaryConclusion.setDecision(relatedCase.getDecision());
        temporaryConclusion.setPlot(relatedCase.getSummary());

        String calledName = generator.generateNames();
        String defenderName = generator.generateNames();

        while (defenderName.equals(calledName)) {
            defenderName = generator.generateNames();
        }

        temporaryConclusion.setFullNameOfCalled(calledName);
        temporaryConclusion.setFullNameOfDefender(defenderName);


        String iinInvestigator = createConclusionRequest.getIINOfInvestigator();
        temporaryConclusion.setInvestigatorIIN(iinInvestigator);

        User investigator = userRepository.findByIIN(iinInvestigator).
                orElseThrow(()-> new UserNotFoundException("User not found."));

        List<String> temporaryConclusions = investigator.getTemporaryConclusionsRegNumbers();
        temporaryConclusions.add(temporaryConclusion.getRegistrationNumber());

        temporaryConclusionRepository.save(temporaryConclusion);
        userRepository.save(investigator);
        LOGGER.debug("Temporary conclusion saved.");
    }

    @Override
    @Transactional
    public void editSavedConclusion(EditSavedConclusionRequest editSavedConclusionRequest)
            throws UserNotFoundException, NoTemporaryConclusionFound, RegionNotFoundException, CaseNotFound {
        LOGGER.debug("Editing a temporary conclusion.");
        String investigatorIIN = editSavedConclusionRequest.getCreateConclusionRequest().getIINOfInvestigator();
        LOGGER.warn("IIN IS " + investigatorIIN);
        User investigator = userRepository.findByIIN(investigatorIIN).orElseThrow(() -> new UserNotFoundException("User not found."));
        investigator.getTemporaryConclusionsRegNumbers().removeIf(temporaryConclusionRegNumber ->
                temporaryConclusionRegNumber.equals(editSavedConclusionRequest.getRegistrationNumber())
        );

        TemporaryConclusion temporaryConclusion = temporaryConclusionRepository.
                findTemporaryConclusionByRegistrationNumber(editSavedConclusionRequest.getRegistrationNumber()).
                orElseThrow(() -> new NoTemporaryConclusionFound("No temporary conclusions found."));

        CreateConclusionRequest request = editSavedConclusionRequest.getCreateConclusionRequest();

        Case relatedCase = assignCase(request.getUD());
        temporaryConclusion.setRegistrationDate(utcFormatter.convertUTCToUTCPlus5(relatedCase.getRegistrationDate()));
        temporaryConclusion.setUD(relatedCase.getUD());
        temporaryConclusion.setArticle(relatedCase.getArticle());
        temporaryConclusion.setDecision(relatedCase.getDecision());
        temporaryConclusion.setPlot(relatedCase.getSummary());

        String calledName = generator.generateNames();
        String defenderName = generator.generateNames();

        while (defenderName.equals(calledName)) {
            defenderName = generator.generateNames();
        }

        temporaryConclusion.setFullNameOfCalled(calledName);
        temporaryConclusion.setFullNameOfDefender(defenderName);
        temporaryConclusion.setIINofCalled(request.getIINOfCalled());
        temporaryConclusion.setIINDefender(request.getIINDefender());

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
        temporaryConclusion.setJustification(request.getJustification());
        temporaryConclusion.setResult(request.getResult());
        temporaryConclusion.setInvestigatorIIN(investigatorIIN);

        temporaryConclusionRepository.save(temporaryConclusion);

        investigator.getTemporaryConclusionsRegNumbers().add(temporaryConclusion.getRegistrationNumber());
        userRepository.save(investigator);
        LOGGER.debug("Temporary conclusion edited.");
    }

    @Override
    @Transactional
    public void turnToPermanent(String registrationNumber) throws UserNotFoundException, NoTemporaryConclusionFound {
        TemporaryConclusion tempConclusion = temporaryConclusionRepository.findTemporaryConclusionByRegistrationNumber(registrationNumber).
                orElseThrow(()-> new NoTemporaryConclusionFound("No temporary conclusion found."));

        Conclusion conclusion = conclusionMapper.fromTempToConclusion(tempConclusion);
        conclusionRepository.save(conclusion);

        User investigator = userRepository.findByIIN(tempConclusion.getInvestigatorIIN()).
                orElseThrow(() -> new UserNotFoundException("User not found."));

        investigator.getTemporaryConclusionsRegNumbers().removeIf(temporaryConclusionRegNumber ->
                temporaryConclusionRegNumber.equals(tempConclusion.getRegistrationNumber())
        );
        investigator.getConclusionsRegNumbers().add(conclusion.getRegistrationNumber());

        User analystOfDep = userRepository.findAnalystByDepartment(investigator.getDepartment().getName());
        sendConclusion(conclusion,analystOfDep.getIIN());

        userRepository.save(investigator);
        temporaryConclusionRepository.delete(tempConclusion);
    }

    @Override
    public Set<ConclusionDto> filter(FilterRequest filterRequest) throws UserNotFoundException {
        LOGGER.debug("Filtering...");

        User user = userRepository.findByIIN(filterRequest.getIIN())
                .orElseThrow(UserNotFoundException::new);

        List<Conclusion> filteredConclusions;

        if (user.getJob().getName().equals(EMPLOYEE)) {
            List<Conclusion> allDocsOfUser = conclusionRepository.
                    findByRegistrationNumbers(user.getConclusionsRegNumbers());
            filteredConclusions = conclusionRepository.filterSomeConclusions(allDocsOfUser, filterRequest);

        } else if (user.getJob().getName().equals(ANALYST)) {
            List<Conclusion> receivedConclusions = conclusionRepository.
                    findByRegistrationNumbers(user.getReceivedConclusionsRegNumbers());
            filteredConclusions = conclusionRepository.filterSomeConclusions(receivedConclusions, filterRequest);

        } else {
            filteredConclusions = conclusionRepository.filterAllConclusions(filterRequest);
        }

        return conclusionMapper.toDtoSet(filteredConclusions);
    }


    @Override
    public Set<ConclusionDto> userConclusions(String IIN) throws UserNotFoundException {
        LOGGER.debug("Retrieving user conclusions...");
        User user = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User not found."));
        List<Conclusion> conclusions;

        if (user.getJob().getName().equals(EMPLOYEE)) {
            conclusions = conclusionRepository.findByRegistrationNumbers(user.getConclusionsRegNumbers());

        } else if (user.getJob().getName().equals(ANALYST)) {
            conclusions = conclusionRepository.findByRegistrationNumbers(user.getReceivedConclusionsRegNumbers());

        } else {
            conclusions = getAllConclusions();
        }

        conclusions.sort(Comparator.comparingInt(conclusion -> getStatusOrder(conclusion.getStatus().getName())));
        return conclusionMapper.toDtoSet(conclusions);
    }

    private int getStatusOrder(String status) {
        switch (status) {
            case "На согласовании": return 1;
            case "Отправлено на доработку": return 2;
            case "Согласовано": return 3;
            case "Оставлено без рассмотрения": return 4;
            case "Отказано": return 5;
            default: return Integer.MAX_VALUE;
        }
    }

    @Override
    public List<AgreementDto> userAgreements(String IIN) throws UserNotFoundException {
        User user = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User not found."));

        return agreementMapper.toDtoList(user.getAgreements());
    }

    @Override
    public List<TempConclusionDto> userSavedConclusions(String IIN) throws UserNotFoundException {
        LOGGER.debug("Retrieving user saved conclusions...");
        User user = userRepository.findByIIN(IIN).orElseThrow(()-> new UserNotFoundException("User not found."));

        return tempMapper.toDtoList(temporaryConclusionRepository.
                findByRegistrationNumbers(user.getTemporaryConclusionsRegNumbers()));
    }

    @Override
    public List<String> allUD() {
        return caseRepository.findAll().stream().map(Case::getUD).toList();
    }

    @Override
    public List<Conclusion> getAllConclusions() {
        return conclusionRepository.findAll();
    }

    @Override
    public AgreementDto makeDecision(DecisionRequest decisionRequest) throws UserNotFoundException, NoConclusionException {
        Agreement agreement = new Agreement();

        User receiver = userRepository.findByIIN(decisionRequest.getIIN()).orElseThrow(() -> new UserNotFoundException("User not found."));
        agreement.setRegNumber(decisionRequest.getRegistrationNumber());
        agreement.setFullName(receiver.getName() + " " + receiver.getSecondName());
        agreement.setJobTitle(receiver.getJob().getName());

        Conclusion conclusion = conclusionRepository.findConclusionByRegistrationNumber(decisionRequest.getRegistrationNumber()).
                orElseThrow(()-> new NoConclusionException("Conclusion not found."));

        User investigator = userRepository.findByIIN(conclusion.getInvestigatorIIN()).
                orElseThrow(() -> new UserNotFoundException("User not found."));

        String regNum = investigator.getConclusionsRegNumbers().stream()
                .filter(c -> equals(conclusion.getRegistrationNumber()))
                .findFirst().orElseThrow(NoConclusionException::new);

        Conclusion doc = conclusionRepository.findConclusionByRegistrationNumber(regNum).
                orElseThrow(NoConclusionException::new);
        doc.setStatus(statusRepository.findByName(decisionRequest.getStatus()));


        Status status = statusRepository.findByName(decisionRequest.getStatus());
        conclusion.setStatus(status);
        agreement.setDate(conclusion.getEventTime());
        agreement.setReason(decisionRequest.getReason());
        agreement.setStatus(status);

        sendAgreement(agreement, conclusion.getInvestigatorIIN());

        receiver.getAgreements().add(agreement);
        userRepository.save(receiver);
        userRepository.save(investigator);
        agreementRepository.save(agreement);
        conclusionRepository.save(conclusion);
        return agreementMapper.toAgreementDto(agreement);
    }

    @Override
    public void sendAgreement(Agreement agreement, String IIN) throws UserNotFoundException {
        User investigator = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User not found."));
        investigator.getAgreements().add(agreement);
        userRepository.save(investigator);
    }

    @Override
    public ConclusionDto getSpecific(String regNumber) throws NoConclusionException, UserNotFoundException {
        return conclusionMapper.toConclusionDto(conclusionRepository.
                findConclusionByRegistrationNumber(regNumber).orElseThrow(NoConclusionException::new));
    }

    @Override
    public History history(String iinOfCalled, String goal) throws UserNotFoundException {
        User user = userRepository.findByIIN(iinOfCalled).orElseThrow(UserNotFoundException::new);

        List<Agreement> agreements = agreementRepository.getAgreementsByIInOfCalled(user.getIIN());
        return historyMapper.toHistory(agreements, goal);
    }
}
