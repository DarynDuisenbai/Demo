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
import com.example.demo.mapper.spec.TempMapper;
import com.example.demo.domain.*;
import com.example.demo.repository.spec.*;
import com.example.demo.service.spec.ConclusionService;
import com.example.demo.util.Generator;
import com.example.demo.util.UTCFormatter;
import com.example.demo.util.Validator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
    private final Generator generator;
    private final UTCFormatter utcFormatter;
    private final Validator validator;

    @Override
    public void sendConclusion(Conclusion conclusion, User manager) {
        manager.getReceivedConclusionsRegNumbers().add(conclusion.getRegistrationNumber());
        userRepository.save(manager);
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

        String iinInvestigator = createConclusionRequest.getIINOfInvestigator();
        conclusion.setInvestigatorIIN(iinInvestigator);

        User investigator = userRepository.findByIIN(iinInvestigator).
                orElseThrow(() -> new UserNotFoundException("Investigator with IIN: " + iinInvestigator + " not found."));

        if(!investigator.getManagerIIN().isEmpty()) {
            User manager = userRepository.findByIIN(investigator.getManagerIIN()).
                    orElseThrow(() -> new UserNotFoundException("Manager with given IIN:" + investigator.getManagerIIN() + "not found"));
            sendConclusion(conclusion, manager);
        }

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
                    .orElseThrow(() -> new UserNotFoundException("Manager with IIN: " + finalManagerIIN + " not found."));
            managers.add(manager);
            managerIIN = manager.getManagerIIN();
        }

        return managers;
    }

    private Case assignCase(String UD) throws CaseNotFound {
        return caseRepository.findCaseByUD(UD).orElseThrow(() -> new CaseNotFound("Case with UD: " + UD + " not found."));
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

        String iinInvestigator = createConclusionRequest.getIINOfInvestigator();
        temporaryConclusion.setInvestigatorIIN(iinInvestigator);

        User investigator = userRepository.findByIIN(iinInvestigator).
                orElseThrow(() -> new UserNotFoundException("Investigator with IIN: " + iinInvestigator + " not found."));

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
        User investigator = userRepository.findByIIN(investigatorIIN).orElseThrow(() -> new UserNotFoundException("Investigator with IIN:" + investigatorIIN + " not found."));
        investigator.getTemporaryConclusionsRegNumbers().removeIf(temporaryConclusionRegNumber ->
                temporaryConclusionRegNumber.equals(editSavedConclusionRequest.getRegistrationNumber())
        );

        TemporaryConclusion temporaryConclusion = temporaryConclusionRepository.
                findTemporaryConclusionByRegistrationNumber(editSavedConclusionRequest.getRegistrationNumber()).
                orElseThrow(() -> new NoTemporaryConclusionFound(
                        "Temporary conclusion with registration number: " + editSavedConclusionRequest.getRegistrationNumber() + " not found.")
                );

        CreateConclusionRequest request = editSavedConclusionRequest.getCreateConclusionRequest();
        if (request.getUD() != null) {
            Case relatedCase = assignCase(request.getUD());
            temporaryConclusion.setRegistrationDate(utcFormatter.convertUTCToUTCPlus5(relatedCase.getRegistrationDate()));
            temporaryConclusion.setUD(relatedCase.getUD());
            temporaryConclusion.setArticle(relatedCase.getArticle());
            temporaryConclusion.setDecision(relatedCase.getDecision());
            temporaryConclusion.setPlot(relatedCase.getSummary());
        }
        if (request.getIINOfCalled() != null) {
            temporaryConclusion.setIINofCalled(request.getIINOfCalled());
            temporaryConclusion.setFullNameOfCalled(generator.generateNames());
        }

        if (request.getIINDefender() != null) {
            temporaryConclusion.setIINDefender(request.getIINDefender());
            temporaryConclusion.setFullNameOfDefender(generator.generateNames());
        }

        if (request.getBIN_IIN() != null) {
            temporaryConclusion.setBINorIINOfCalled(request.getBIN_IIN());
            temporaryConclusion.setJobPlace(generator.generateJobPlaces());
        }

        if (request.getJobTitle() != null) {
            temporaryConclusion.setJobTitleOfCalled(request.getJobTitle());
        }

        if (request.getRegion() != null) {
            Region region = regionRepository.findRegionByName(request.getRegion())
                    .orElseThrow(() -> new RegionNotFoundException("Region not found."));
            temporaryConclusion.setRegion(region);
        }

        if (request.getPlannedActions() != null) {
            temporaryConclusion.setPlannedActions(request.getPlannedActions());
        }

        if (request.getEventDateTime() != null) {
            temporaryConclusion.setEventTime(utcFormatter.convertUTCToUTCPlus5(request.getEventDateTime()));
        }

        if (request.getEventPlace() != null) {
            temporaryConclusion.setEventPlace(request.getEventPlace());
        }

        if (request.getRelation() != null) {
            temporaryConclusion.setRelation(request.getRelation());
        }

        if (request.getInvestigationType() != null) {
            temporaryConclusion.setInvestigation(request.getInvestigationType());
        }

        if (request.getRelatesToBusiness() != null) {
            temporaryConclusion.setIsBusiness(request.getRelatesToBusiness());
            temporaryConclusion.setBINOrIINofBusiness(generator.generateBIN());
            temporaryConclusion.setWorkPlaceBusiness(generator.generateWorkPlaceBusiness());
        }

        if (request.getJustification() != null) {
            temporaryConclusion.setJustification(request.getJustification());
        }

        if (request.getResult() != null) {
            temporaryConclusion.setResult(request.getResult());
        }

        Status status = statusRepository.findByName(StatusConstants.IN_PROGRESS.getLabel());
        temporaryConclusion.setStatus(status);

        temporaryConclusionRepository.save(temporaryConclusion);

        investigator.getTemporaryConclusionsRegNumbers().add(temporaryConclusion.getRegistrationNumber());
        userRepository.save(investigator);
        LOGGER.debug("Temporary conclusion edited.");
    }

    @Override
    public void deleteConclusion(String registrationNumber)
            throws UserNotFoundException, NoConclusionException {
        LOGGER.debug("Удаление заключения...");

        Conclusion conclusion = conclusionRepository
                .findConclusionByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new NoConclusionException(
                        "Заключение с регистрационным номером: " + registrationNumber + " не найдено."));

        conclusionRepository.delete(conclusion);

        User user = userRepository.findByConclusionsRegNumbersContaining(registrationNumber)
                .orElseThrow(() -> new UserNotFoundException(
                        "Пользователь с регистрационным номером: " + registrationNumber + " не найден."));

        boolean removed = user.getConclusionsRegNumbers().removeIf(regNum -> regNum.equals(registrationNumber));
        if (removed) {
            userRepository.save(user);
            LOGGER.debug("Регистрационный номер удален из списка пользователя.");
        } else {
            LOGGER.warn("Регистрационный номер не найден в списке пользователя.");
        }
        LOGGER.debug("Заключение успешно удалено.");
    }

    @Override
    @Transactional
    public void turnToPermanent(String registrationNumber)
            throws UserNotFoundException, NoTemporaryConclusionFound, ConclusionNotReadyException {
        TemporaryConclusion tempConclusion = temporaryConclusionRepository.findTemporaryConclusionByRegistrationNumber(registrationNumber).
                orElseThrow(() -> new NoTemporaryConclusionFound("Temporary conclusion with registration number: " + registrationNumber + " not found."));

        if (!validator.isValidConclusion(tempConclusion)) {
            throw new ConclusionNotReadyException("Your conclusion is not ready!");
        }
        Conclusion conclusion = conclusionMapper.fromTempToConclusion(tempConclusion);

        Status status = statusRepository.findByName(StatusConstants.TO_BE_AGREED.getLabel());
        conclusion.setStatus(status);
        conclusionRepository.save(conclusion);

        User investigator = userRepository.findByIIN(tempConclusion.getInvestigatorIIN()).
                orElseThrow(() -> new UserNotFoundException("Investigator with IIN:" + tempConclusion.getInvestigation() + " not found."));

        investigator.getTemporaryConclusionsRegNumbers().removeIf(temporaryConclusionRegNumber ->
                temporaryConclusionRegNumber.equals(tempConclusion.getRegistrationNumber())
        );
        investigator.getConclusionsRegNumbers().add(conclusion.getRegistrationNumber());

        if(!investigator.getManagerIIN().isEmpty()) {
            User manager = userRepository.findByIIN(investigator.getManagerIIN()).
                    orElseThrow(() -> new UserNotFoundException("Manager with given IIN:" + investigator.getManagerIIN() + "not found"));
            sendConclusion(conclusion, manager);
        }

        userRepository.save(investigator);
        temporaryConclusionRepository.delete(tempConclusion);
    }

    @Override
    @Transactional
    public void remakeConclusion(EditSavedConclusionRequest editSavedConclusionRequest)
            throws NoConclusionException, NoPermissionForUpdateException, CaseNotFound, RegionNotFoundException {
        Conclusion conclusion = conclusionRepository.findConclusionByRegistrationNumber(editSavedConclusionRequest.getRegistrationNumber()).
                orElseThrow(()-> new NoConclusionException(
                        "Conclusion with registration number: " + editSavedConclusionRequest.getRegistrationNumber() + " not found.")
                );

        if(!conclusion.getStatus().getName().equals(StatusConstants.FOR_REWORK.getLabel())){
            throw new NoPermissionForUpdateException("You have not permission for update this conclusion.");
        }
        CreateConclusionRequest request = editSavedConclusionRequest.getCreateConclusionRequest();
        if (request.getUD() != null) {
            Case relatedCase = assignCase(request.getUD());
            conclusion.setRegistrationDate(utcFormatter.convertUTCToUTCPlus5(relatedCase.getRegistrationDate()));
            conclusion.setUD(relatedCase.getUD());
            conclusion.setArticle(relatedCase.getArticle());
            conclusion.setDecision(relatedCase.getDecision());
            conclusion.setPlot(relatedCase.getSummary());
        }
        if (request.getIINOfCalled() != null) {
            conclusion.setIINofCalled(request.getIINOfCalled());
            conclusion.setFullNameOfCalled(generator.generateNames());
        }

        if (request.getIINDefender() != null) {
            conclusion.setIINDefender(request.getIINDefender());
            conclusion.setFullNameOfDefender(generator.generateNames());
        }

        if (request.getBIN_IIN() != null) {
            conclusion.setBINorIINOfCalled(request.getBIN_IIN());
            conclusion.setJobPlace(generator.generateJobPlaces());
        }

        if (request.getJobTitle() != null) {
            conclusion.setJobTitleOfCalled(request.getJobTitle());
        }

        if (request.getRegion() != null) {
            Region region = regionRepository.findRegionByName(request.getRegion())
                    .orElseThrow(() -> new RegionNotFoundException("Region not found."));
            conclusion.setRegion(region);
        }

        if (request.getPlannedActions() != null) {
            conclusion.setPlannedActions(request.getPlannedActions());
        }

        if (request.getEventDateTime() != null) {
            conclusion.setEventTime(utcFormatter.convertUTCToUTCPlus5(request.getEventDateTime()));
        }

        if (request.getEventPlace() != null) {
            conclusion.setEventPlace(request.getEventPlace());
        }

        if (request.getRelation() != null) {
            conclusion.setRelation(request.getRelation());
        }

        if (request.getInvestigationType() != null) {
            conclusion.setInvestigation(request.getInvestigationType());
        }

        if (request.getRelatesToBusiness() != null) {
            conclusion.setBusiness(request.getRelatesToBusiness());
            conclusion.setWorkPlaceBusiness(generator.generateWorkPlaceBusiness());
            conclusion.setBINOrIINofBusiness(generator.generateBIN());
        }

        if (request.getJustification() != null) {
            conclusion.setJustification(request.getJustification());
        }

        if (request.getResult() != null) {
            conclusion.setResult(request.getResult());
        }

        Status status = statusRepository.findByName(StatusConstants.TO_BE_AGREED.getLabel());
        conclusion.setStatus(status);

        conclusionRepository.save(conclusion);
    }

    @Override
    public Set<ConclusionDto> filter(FilterRequest filterRequest) throws UserNotFoundException {
        LOGGER.debug("Filtering...");

        User user = userRepository.findByIIN(filterRequest.getIIN())
                .orElseThrow(UserNotFoundException::new);

        List<Conclusion> filteredConclusions = new ArrayList<>();

        if (user.getJob().getName().equals(JobConstants.EMPLOYEE.getLabel())) {
            List<Conclusion> allDocsOfUser = new ArrayList<>(conclusionRepository.
                    findByRegistrationNumbers(user.getConclusionsRegNumbers()));
            filteredConclusions = conclusionRepository.filterSomeConclusions(allDocsOfUser, filterRequest);
        }
        else if (user.getJob().getName().equals(JobConstants.CURATOR.getLabel()) ||
                user.getJob().getName().equals(JobConstants.SPECIALIST.getLabel())) {
            List<Conclusion> allDocsOfUser = new ArrayList<>(conclusionRepository.
                    findByRegistrationNumbers(user.getConclusionsRegNumbers()));
            List<Conclusion> receivedConclusions = new ArrayList<>(conclusionRepository.
                    findByRegistrationNumbers(user.getReceivedConclusionsRegNumbers()));

            allDocsOfUser.addAll(receivedConclusions);
            filteredConclusions = conclusionRepository.filterSomeConclusions(allDocsOfUser, filterRequest);

        }
        else if (user.getJob().getName().equals(JobConstants.ANALYST.getLabel())) {
            List<Conclusion> receivedConclusions = new ArrayList<>(conclusionRepository.
                    findByRegistrationNumbers(user.getReceivedConclusionsRegNumbers()));
            filteredConclusions = conclusionRepository.filterSomeConclusions(receivedConclusions, filterRequest);

        } else if (user.getJob().getName().equals(JobConstants.MODERATOR.getLabel())) {
            filteredConclusions = conclusionRepository.filterAllConclusions(filterRequest);
        }

        return conclusionMapper.toDtoSet(filteredConclusions);
    }


    @Override
    public List<ConclusionDto> userConclusions(String IIN) throws UserNotFoundException {
        LOGGER.debug("Retrieving user conclusions...");
        User user = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User with IIN: " + IIN + " not found."));
        List<Conclusion> conclusions = new ArrayList<>();

        if (user.getJob().getName().equals(JobConstants.EMPLOYEE.getLabel())) {
            conclusions = new ArrayList<>(conclusionRepository.findByRegistrationNumbers(user.getConclusionsRegNumbers()));
        } else if (user.getJob().getName().equals(JobConstants.CURATOR.getLabel()) ||
                user.getJob().getName().equals(JobConstants.SPECIALIST.getLabel())) {
            conclusions = new ArrayList<>(conclusionRepository.findByRegistrationNumbers(user.getConclusionsRegNumbers()));
            List<Conclusion> receivedConclusions = new ArrayList<>(conclusionRepository.findByRegistrationNumbers(user.getReceivedConclusionsRegNumbers()));
            conclusions.addAll(receivedConclusions);

        } else if (user.getJob().getName().equals(JobConstants.ANALYST.getLabel())) {
            conclusions = new ArrayList<>(conclusionRepository.findByRegistrationNumbers(user.getReceivedConclusionsRegNumbers()));

        } else if (user.getJob().getName().equals(JobConstants.MODERATOR.getLabel())) {
            conclusions = getAllConclusions();
        }

        conclusions.sort(Comparator.comparingInt(conclusion -> getStatusOrder(conclusion.getStatus().getName())));
        return conclusionMapper.toDtoList(conclusions);
    }

    private int getStatusOrder(String status) {
        return switch (status) {
            case "На согласовании" -> 1;
            case "Отправлено на доработку" -> 2;
            case "Отказано" -> 3;
            case "Оставлено без рассмотрения" -> 4;
            case "Согласовано" -> 5;
            default -> Integer.MAX_VALUE;
        };
    }

    @Override
    public AgreementDto userAgreements(String IIN, String regNum) throws UserNotFoundException {
        User user = userRepository.findByIIN(IIN).orElseThrow(() -> new UserNotFoundException("User with IIN: " + IIN + " not found."));

        List<Agreement> agreements = user.getAgreements().stream().
                filter(x -> x.getRegNumber().equals(regNum)).
                collect(Collectors.toList());
        Agreement last = agreements.get(agreements.size()-1);

        return agreementMapper.toAgreementDto(last);
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

        User receiver = userRepository.findByIIN(decisionRequest.getIIN()).orElseThrow(() -> new UserNotFoundException("Receiver with IIN: " + decisionRequest.getIIN() + " not found."));
        agreement.setRegNumber(decisionRequest.getRegistrationNumber());
        agreement.setFullName(receiver.getName() + " " + receiver.getSecondName());
        agreement.setJobTitle(receiver.getJob().getName());

        Conclusion conclusion = conclusionRepository.findConclusionByRegistrationNumber(decisionRequest.getRegistrationNumber()).
                orElseThrow(() -> new NoConclusionException("Conclusion with registration number: " + decisionRequest.getRegistrationNumber() + " not found"));

        User investigator = userRepository.findByIIN(conclusion.getInvestigatorIIN()).orElseThrow(UserNotFoundException::new);
        Status status = statusRepository.findByName(decisionRequest.getStatus());

        agreement.setDate(conclusion.getEventTime());
        agreement.setReason(decisionRequest.getReason());
        agreement.setStatus(status);

        receiver.getAgreements().add(agreement);
        userRepository.save(receiver);
        agreementRepository.save(agreement);

        if(status.getName().equals(StatusConstants.REFUSED.getLabel()) ||
                status.getName().equals(StatusConstants.FOR_REWORK.getLabel())){

            conclusion.setStatus(status);
            notifyInvestigator(agreement, investigator.getIIN());
            conclusionRepository.save(conclusion);

            return agreementMapper.toAgreementDto(agreement);
        }
        if(!receiver.getManagerIIN().isEmpty() &&
                !status.getName().equals(StatusConstants.REFUSED.getLabel()) &&
                !status.getName().equals(StatusConstants.FOR_REWORK.getLabel())){
            User manager = userRepository.findByIIN(receiver.getManagerIIN()).
                    orElseThrow(()-> new UserNotFoundException("Manager with given IIN: "+ receiver.getManagerIIN() + "not found"));
            sendConclusion(conclusion, manager);
        }
        List<User> managers = getAllManagers(investigator);
        if (isAllAnswered(agreement, managers)) {
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
        conclusion.setAcceptDateTime(LocalDateTime.now());
        conclusionRepository.save(conclusion);
    }

    private void notifyInvestigator(Agreement agreement, String investigatorIIN) throws UserNotFoundException {
        User user = userRepository.findByIIN(investigatorIIN).orElseThrow(UserNotFoundException::new);
        user.getAgreements().add(agreement);
        userRepository.save(user);
    }

    private boolean isAllAnswered(Agreement agreement, List<User> managers) {
        String registrationNumber = agreement.getRegNumber();

        return managers.stream()
                .allMatch(manager -> manager.getAgreements().stream()
                        .filter(userAgreement -> userAgreement.getRegNumber().equals(registrationNumber))
                        .max(Comparator.comparing(Agreement::getDate))
                        .isPresent()
                );
    }

    @Override
    public ConclusionDto getSpecific(String regNumber, String iin) throws NoConclusionException, UserNotFoundException, AccessDeniedException {
        User user = userRepository.findByIIN(iin).orElseThrow(() -> new UserNotFoundException("User with IIN: " + iin + " not found."));
        if(!user.getConclusionsRegNumbers().contains(regNumber)){
            throw new AccessDeniedException("You do not have permission for this operation");
        }
        return conclusionMapper.toConclusionDto(conclusionRepository.
                findConclusionByRegistrationNumber(regNumber).orElseThrow(() -> new NoConclusionException("Conclusion with registration number: " + regNumber + " not found")));
    }

    @Override
    public TempConclusionDto getSpecificTemp(String regNumber, String iin) throws NoConclusionException, UserNotFoundException, AccessDeniedException {
        User user = userRepository.findByIIN(iin).orElseThrow(() -> new UserNotFoundException("User with IIN: " + iin + " not found."));
        if(!user.getTemporaryConclusionsRegNumbers().contains(regNumber)){
            throw new AccessDeniedException("You do not have permission for this operation");
        }
        return tempMapper.toTempConclusionDto(temporaryConclusionRepository.
                findTemporaryConclusionByRegistrationNumber(regNumber).orElseThrow(() -> new NoConclusionException("Conclusion with registration number: " + regNumber + " not found")));
    }

    @Override
    public History history(String iinUser, String iinOfCalled, String goal) throws UserNotFoundException, NoConclusionException {
        User user = userRepository.findByIIN(iinUser).orElseThrow(() -> new UserNotFoundException("User with given iin: " + iinUser + " not found."));

        List<Conclusion> conclusionsByIINOfCalled = conclusionRepository.findConclusionsByIINofCalled(iinOfCalled);
        History history = new History();
        if(conclusionsByIINOfCalled.isEmpty() || conclusionsByIINOfCalled == null){
            throw new NoConclusionException("There is no conclusion related to person with " + iinOfCalled);
        }
        Conclusion lastConclusion = conclusionsByIINOfCalled.get(conclusionsByIINOfCalled.size()-1);
        history.setGoal(goal);
        history.setFullName(lastConclusion.getFullNameOfCalled());
        history.setStatus(lastConclusion.getStatus().getName());
        history.setPrevCall(lastConclusion.getRegistrationNumber());
        history.setCame(lastConclusion.getCreationDate());
        history.setLeave(lastConclusion.getAcceptDateTime() == null  ? null : lastConclusion.getAcceptDateTime());
        return history;
    }
}
