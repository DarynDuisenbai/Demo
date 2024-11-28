package com.example.demo.service.impl;

import com.example.demo.domain.Conclusion;
import com.example.demo.dto.responce.ConclusionDto;
import com.example.demo.exception.NoConclusionException;
import com.example.demo.repository.spec.ConclusionRepository;
import com.example.demo.service.spec.ConclusionService;
import com.example.demo.service.spec.ExportService;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {
    private final ConclusionRepository conclusionRepository;
    private final ConclusionService conclusionService;

    @Override
    public ByteArrayInputStream exportToExcel(String IIN) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Conclusions");

            Row headerRow = sheet.createRow(0);
            String[] columns = {
                    "Registration Number", "Creation Date", "UD", "Registration Date",
                    "Article", "Decision", "Plot", "IIN of Called", "Full Name of Called",
                    "Job Title of Called", "BIN/IIN of Called", "Job Place", "Region",
                    "Planned Actions", "Event Time", "Event Place", "Investigator",
                    "Status", "Relation", "Investigation", "Is Business",
                    "BIN/IIN Pension", "IIN of Defender", "Full Name of Defender",
                    "Justification", "Result"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            Set<ConclusionDto> conclusions = conclusionService.userConclusions(IIN);
            int rowIndex = 1;
            for (ConclusionDto conclusion : conclusions) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(conclusion.getRegistrationNumber());
                row.createCell(1).setCellValue(conclusion.getCreationDate());
                row.createCell(2).setCellValue(conclusion.getUdNumber());
                row.createCell(3).setCellValue(conclusion.getRegistrationDate());
                row.createCell(4).setCellValue(conclusion.getArticle());
                row.createCell(5).setCellValue(conclusion.getDecision());
                row.createCell(6).setCellValue(conclusion.getSummary());
                row.createCell(7).setCellValue(conclusion.getCalledPersonIIN());
                row.createCell(8).setCellValue(conclusion.getCalledPersonFullName());
                row.createCell(9).setCellValue(conclusion.getCalledPersonPosition());
                row.createCell(10).setCellValue(conclusion.getCalledPersonBIN());
                row.createCell(11).setCellValue(conclusion.getWorkPlace());
                row.createCell(12).setCellValue(conclusion.getRegion().getName());
                row.createCell(13).setCellValue(conclusion.getPlannedInvestigativeActions());
                row.createCell(14).setCellValue(conclusion.getEventDateTime());
                row.createCell(15).setCellValue(conclusion.getEventPlace());
                row.createCell(16).setCellValue(conclusion.getInvestigatorIIN());
                row.createCell(17).setCellValue(conclusion.getStatus());
                row.createCell(18).setCellValue(conclusion.getRelationToEvent());
                row.createCell(19).setCellValue(conclusion.getInvestigationTypes());
                row.createCell(20).setCellValue(conclusion.getRelatesToBusiness());
                row.createCell(21).setCellValue(conclusion.getCalledPersonBIN());
                row.createCell(22).setCellValue(conclusion.getDefenseAttorneyIIN());
                row.createCell(23).setCellValue(conclusion.getDefenseAttorneyFullName());
                row.createCell(24).setCellValue(conclusion.getJustification());
                row.createCell(25).setCellValue(conclusion.getResult());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to export data to Excel: " + e.getMessage());
        }
    }


    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    @Override
    public ByteArrayInputStream exportToPdf(String regNumber) throws NoConclusionException, IOException {
        // Retrieve the conclusion data from the database
        Conclusion conclusion = conclusionRepository.findConclusionByRegistrationNumber(regNumber)
                .orElseThrow(NoConclusionException::new);

        // Create a new PDF document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Add content to the document
        addConclusionDataToPdf(document, page, conclusion);

        // Save the document to a byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.save(out);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addConclusionDataToPdf(PDDocument document, PDPage page, Conclusion conclusion) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/Faberge-Regular.otf");
            if (fontStream == null) {
                throw new FileNotFoundException("Font file not found in resources");
            }

            PDType0Font font = PDType0Font.load(document, fontStream);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750);
            contentStream.setFont(font, 12);
            // Add text content to the document
            contentStream.showText("Заявление");
            contentStream.newLineAtOffset(0, -20);

            contentStream.showText("Регистрационный номер: " + conclusion.getRegistrationNumber());
            contentStream.newLineAtOffset(0, -20);

            contentStream.showText("Статья: " + conclusion.getArticle());
            contentStream.newLineAtOffset(0, -20);

            contentStream.endText();
        }
    }
}

