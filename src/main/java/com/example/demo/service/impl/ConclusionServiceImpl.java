package com.example.demo.service.impl;

import com.example.demo.constant.JobConstants;
import com.example.demo.constant.StatusConstants;
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
import com.example.demo.util.Validator;
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
    private final Validator validator;

    @Override
    public void sendConclusion(Conclusion conclusion, List<User> managers) {
        for (User user : managers) {
            user.getReceivedConclusionsRegNumbers().add(conclusion.getRegistrationNumber());
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void createConclusion(CreateConclusionRequest createConclusionRequest) throws UserNotFoundException, RegionNotFoundException, CaseNotFound {
        LOGGER.debug("Creating a new conclusion");
        Conclusion conclusion = conclusionMapper.fromCreateToConclusion(createConclusionRequest);

        conclusion.setRegistrationNumber(generator.generateUniqueNumber());
        conclusion.setCreationDate(utcFormatter.convertUTCToUTCPlus5(LocalDateTime.now()));

        Status status = statusRepository.findByName(StatusConstants.TO_BE_AGREED.getLabel());
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
                orElseThrow(() -> new UserNotFoundException("Investigator with IIN: " +iinInvestigator+" not found."));

        List<User> managers = getAllManagers(investigator);
        sendConclusion(conclusion, managers);

        List<String> userConclusions = investigator.getConclusionsRegNumbers();
        userConclusions.add(conclusion.getRegistrationNumber());

        conclusionRepository.save(conclusion);
        userRepository.save(investigator);

    }

    private List<User> getAllManagers(User user) throws UserNotFoundException {
        List<User> managers = new ArrayList<>();
        String managerIIN = user.getManagerIIN();
        LOGGER.warn("MANAGER IIN: " + managerIIN);
        while (!managerIIN.isEmpty()) {
            String finalManagerIIN = managerIIN;
            LOGGER.warn("MANAGER COPPY: " + finalManagerIIN);
            User manager = userRepository.findByIIN(managerIIN)
                    .orElseThrow(() -> new UserNotFoundException("Manager with IIN: "+ finalManagerIIN + " not found."));
            managers.add(manager);
            managerIIN = manager.getManagerIIN();
        }

        return managers;
    }

    private Case assignCase(String UD) throws CaseNotFound {
        return caseRepository.findCaseByUD(UD).orElseThrow(() -> new CaseNotFound("Case with UD: " +UD+" not found."));
    }

    @Override
    @Transactional
    public void saveConclusion(CreateConclusionRequest createConclusionRequest)
            throws RegionNotFoundException, UserNotFoundException, CaseNotFound {
        LOGGER.debug("Saving a temporary conclusion.");
        TemporaryConclusion temporaryConclusion = tempMapper.fromCreateToTemp(createConclusionRequest);

        temporaryConclusion.setRegistrationNumber(generator.generateUniqueNumber());
        temporaryConclusion.setCreationDate(LocalDateTime.now());

        Status status = statusRepository.findByName(StatusConstants.IN_PROGRESS.getLabel());
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
                orElseThrow(() -> new UserNotFoundException("Investigator with IIN: "+ iinInvestigator +" not found."));

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
        User investigator = userRepository.findByIIN(investigatorIIN).orElseThrow(() -> new UserNotFoundException("Investigator with IIN:"+ investigatorIIN+" not found."));
        investigator.getTemporaryConclusionsRegNumbers().removeIf(temporaryConclusionRegNumber ->
                temporaryConclusionRegNumber.equals(editSavedConclusionRequest.getRegistrationNumber())
        );

        TemporaryConclusion temporaryConclusion = temporaryConclusionRepository.
                findTemporaryConclusionByRegistrationNumber(editSavedConclusionRequest.getRegistrationNumber()).
                orElseThrow(() -> new NoTemporaryConclusionFound("Temporary conclusion with registration number: "+editSavedConclusionRequest.getRegistrationNumber() +" not found."));

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
        Status status = statusRepository.findByName(StatusConstants.IN_PROGRESS.getLabel());
        temporaryConclusion.setStatus(status);

        temporaryConclusion.setPlannedActions(request.getPlannedActions());
        temporaryConclusion.setEventTime(request.getEventDateTime());
        temporaryConclusion.setEventPlace(request.getEventPlace());
        temporaryConclusion.setRelation(request.getRelation());
        temporaryConclusion.setInvestigation(request.getInvestigationType());
        temporaryConclusion.setIsBusiness(request.getRelatesToBusiness());
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
    public void turnToPermanent(String registrationNumber) throws UserNotFoundException, NoTemporaryConclusionFound, ConclusionNotReadyException {
        TemporaryConclusion tempConclusion = temporaryConclusionRepository.findTemporaryConclusionByRegistrationNumber(registrationNumber).
                orElseThrow(() -> new NoTemporaryConclusionFound("Temporary conclusion with registration number: "+registrationNumber +" not found."));

        if(!validator.isValidConclusion(tempConclusion)){
            throw new ConclusionNotReadyException("Your conclusion is not ready!");
        }
        Conclusion conclusion = conclusionMapper.fromTempToConclusion(tempConclusion);

        Status status = statusRepository.findByName(StatusConstants.TO_BE_AGREED.getLabel());
        conclusion.setStatus(status);
        conclusionRepository.save(conclusion);

        User investigator = userRepository.findByIIN(tempConclusion.getInvestigatorIIN()).
                orElseThrow(() -> new UserNotFoundException("Investigator with IIN:"+ tempConclusion.getInvestigation()+ " not found."));

        investigator.getTemporaryConclusionsRegNumbers().removeIf(temporaryConclusionRegNumber ->
                temporaryConclusionRegNumber.equals(tempConclusion.getRegistrationNumber())
        );
        investigator.getConclusionsRegNumbers().add(conclusion.getRegistrationNumber());

        List<User> managers = getAllManagers(investigator);
        sendConclusion(conclusion, managers);

        userRepository.save(investigator);
        temporaryConclusionRepository.delete(tempConclusion);
    }

    @Override
    public Set<ConclusionDto> filter(FilterRequest filterRequest) throws UserNotFoundException {
        LOGGER.debug("Filtering...");

        User user = userRepository.findByIIN(filterRequest.getIIN())
                .orElseThrow(UserNotFoundException::new);

        List<Conclusion> filteredConclusions = new ArrayList<>();

        if (user.getJob().getName().equals(JobConstants.EMPLOYEE.getLabel()) ||
                user.getJob().getName().equals(JobConstants.CURATOR.getLabel()) ||
                user.getJob().getName().equals(JobConstants.SPECIALIST.getLabel())) {
            List<Conclusion> allDocsOfUser = conclusionRepository.
                    findByRegistrationNumbers(user.getConclusionsRegNumbers());
            filteredConclusions = conclusionRepository.filterSomeConclusions(allDocsOfUser, filterRequest);

        } else if (user.getJob().getName().equals(JobConstants.ANALYST.getLabel())) {
            List<Conclusion> receivedConclusions = conclusionRepository.
                    findByRegistrationNumbers(user.getReceivedConclusionsRegNumbers());
            filteredConclusions = conclusionRepository.filterSomeConclusions(receivedConclusions, filterRequest);

        } else if (user.getJob().getName().equals(JobConstants.MODERATOR.getLabel())) {
            filteredConclusions = conclusionRepository.filterAllConclusions(filterRequest);
        }

        return conclusionMapper.toDtoSet(filteredConclusions);
    }


    @Override
    public Set<ConclusionDto> userConclusions(String IIN) throws UserNotFoundException {
        LOGGER.debug("Retrieving user conclusions...");
        User user = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User with IIN: " + IIN + " not found."));
        List<Conclusion> conclusions = new ArrayList<>();

        if (user.getJob().getName().equals(JobConstants.EMPLOYEE.getLabel())){
            conclusions = conclusionRepository.findByRegistrationNumbers(user.getConclusionsRegNumbers());
        }
        else if(user.getJob().getName().equals(JobConstants.CURATOR.getLabel()) ||
                user.getJob().getName().equals(JobConstants.SPECIALIST.getLabel())) {
            conclusions = conclusionRepository.findByRegistrationNumbers(user.getConclusionsRegNumbers());
            List<Conclusion> receivedConclusions = conclusionRepository.findByRegistrationNumbers(user.getReceivedConclusionsRegNumbers());
            conclusions.addAll(receivedConclusions);

        } else if (user.getJob().getName().equals(JobConstants.ANALYST.getLabel())) {
            conclusions = conclusionRepository.findByRegistrationNumbers(user.getReceivedConclusionsRegNumbers());

        } else if (user.getJob().getName().equals(JobConstants.MODERATOR.getLabel())) {
            conclusions = getAllConclusions();
        }

        conclusions.sort(Comparator.comparingInt(conclusion -> getStatusOrder(conclusion.getStatus().getName())));
        return conclusionMapper.toDtoSet(conclusions);
    }

    private int getStatusOrder(String status) {
        return switch (status) {
            case "На согласовании" -> 1;
            case "Отправлено на доработку" -> 2;
            case "Согласовано" -> 3;
            case "Оставлено без рассмотрения" -> 4;
            case "Отказано" -> 5;
            default -> Integer.MAX_VALUE;
        };
    }

    @Override
    public List<AgreementDto> userAgreements(String IIN) throws UserNotFoundException {
        User user = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User with IIN: " + IIN + " not found."));

        return agreementMapper.toDtoList(user.getAgreements());
    }

    @Override
    public List<TempConclusionDto> userSavedConclusions(String IIN) throws UserNotFoundException {
        LOGGER.debug("Retrieving user saved conclusions...");
        User user = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User with IIN: " + IIN + " not found."));

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

        User receiver = userRepository.findByIIN(decisionRequest.getIIN()).orElseThrow(() -> new UserNotFoundException("Receiver with IIN: "+decisionRequest.getIIN() +" not found."));
        agreement.setRegNumber(decisionRequest.getRegistrationNumber());
        agreement.setFullName(receiver.getName() + " " + receiver.getSecondName());
        agreement.setJobTitle(receiver.getJob().getName());

        Conclusion conclusion = conclusionRepository.findConclusionByRegistrationNumber(decisionRequest.getRegistrationNumber()).
                orElseThrow(() -> new NoConclusionException("Conclusion with registration number: "+ decisionRequest.getRegistrationNumber() + " not found"));

        User investigator = userRepository.findByIIN(conclusion.getInvestigatorIIN()).orElseThrow(UserNotFoundException::new);
        Status status = statusRepository.findByName(decisionRequest.getStatus());

        agreement.setDate(conclusion.getEventTime());
        agreement.setReason(decisionRequest.getReason());
        agreement.setStatus(status);

        receiver.getAgreements().add(agreement);
        userRepository.save(receiver);
        agreementRepository.save(agreement);

        List<User> managers = getAllManagers(investigator);
        if(isAllAnswered(agreement, managers)) {
            updateConclusionStatusBasedOnAgreements(conclusion);
            notifyInvestigator(agreement, conclusion.getInvestigatorIIN());
        }
        return agreementMapper.toAgreementDto(agreement);
    }

    private void updateConclusionStatusBasedOnAgreements(Conclusion conclusion) {
        Set<Agreement> relatedAgreements = agreementRepository.findAgreementsByRegNumber(conclusion.getRegistrationNumber());
        long uniqueStatusCount = relatedAgreements.stream()
                .map(Agreement::getStatus)
                .distinct()
                .count();

        if (uniqueStatusCount == 1) {
            Status unanimousStatus = relatedAgreements.iterator().next().getStatus();
            conclusion.setStatus(unanimousStatus);
        } else {
            Status refusedStatus = statusRepository.findByName(StatusConstants.REFUSED.getLabel());
            conclusion.setStatus(refusedStatus);
        }
        conclusionRepository.save(conclusion);
    }

    private void notifyInvestigator(Agreement agreement, String investigatorIIN) throws UserNotFoundException {
        User user = userRepository.findByIIN(investigatorIIN).orElseThrow(UserNotFoundException::new);
        user.getAgreements().add(agreement);
        userRepository.save(user);
    }

    private boolean isAllAnswered(Agreement agreement, List<User> managers){
        for(User user : managers){
            List<Agreement> agreements = user.getAgreements();
            if(!agreements.contains(agreement)){
                return false;
            }
        }
        return true;
    }
    @Override
    public ConclusionDto getSpecific(String regNumber) throws NoConclusionException, UserNotFoundException {
        return conclusionMapper.toConclusionDto(conclusionRepository.
                findConclusionByRegistrationNumber(regNumber).orElseThrow(()-> new NoConclusionException("Conclusion with registration number: "+regNumber + " not found")));
    }

    @Override
    public History history(String iinOfCalled, String goal) throws UserNotFoundException {
        User user = userRepository.findByIIN(iinOfCalled).orElseThrow(() -> new UserNotFoundException("User with IIN: " + iinOfCalled + " not found."));

        List<Agreement> agreements = agreementRepository.getAgreementsByIInOfCalled(user.getIIN());
        return historyMapper.toHistory(agreements, goal);
    }
}
