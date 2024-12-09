package com.example.demo.service.impl;

import com.example.demo.domain.Conclusion;
import com.example.demo.dto.responce.ConclusionDto;
import com.example.demo.exception.NoConclusionException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.spec.ConclusionRepository;
import com.example.demo.service.spec.ConclusionService;
import com.example.demo.service.spec.ExportService;
import com.example.demo.util.UTCFormatter;
import com.itextpdf.text.*;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {
    private final ConclusionRepository conclusionRepository;
    private final ConclusionService conclusionService;
    private final UTCFormatter utcFormatter;

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

            List<ConclusionDto> conclusions = conclusionService.userConclusions(IIN);
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

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    @Override
    public ByteArrayInputStream exportToPdf(String IIN)
            throws NoConclusionException, IOException, UserNotFoundException, DocumentException {
        List<ConclusionDto> conclusions = conclusionService.userConclusions(IIN);
        if (conclusions == null || conclusions.isEmpty()) {
            throw new NoConclusionException("No conclusions found for the given IIN: " + IIN);
        }

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();
        InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/DejaVuSans.ttf");
        if (fontStream == null) {
            throw new IOException("Font file not found in resources: fonts/DejaVuSans.ttf");
        }
        BaseFont baseFont = BaseFont.createFont("DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true,
                fontStream.readAllBytes(), null);

        Font titleFont = new Font(baseFont, 18, Font.BOLD);
        Font headerFont = new Font(baseFont, 12, Font.BOLD);
        Font cellFont = new Font(baseFont, 10, Font.NORMAL);

        Paragraph title = new Paragraph("Репорт Заключении", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        String[] headers = {"Registration Number", "Creation Date", "Article", "Decision", "Region"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }

        for (ConclusionDto conclusion : conclusions) {
            table.addCell(new Phrase(conclusion.getRegistrationNumber(), cellFont));
            table.addCell(new Phrase(String.valueOf(conclusion.getCreationDate()), cellFont));
            table.addCell(new Phrase(conclusion.getArticle(), cellFont));
            table.addCell(new Phrase(conclusion.getDecision(), cellFont));
            table.addCell(new Phrase(conclusion.getRegion().getName(), cellFont));
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream generateConclusionPdf(String registrationNumber) throws NoConclusionException, IOException, DocumentException {

        Conclusion conclusion = conclusionRepository
                .findConclusionByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new NoConclusionException("Conclusion with registration number: " + registrationNumber + " not found"));
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/DejaVuSans.ttf");
        if (fontStream == null) {
            throw new IOException("Font file not found in resources: fonts/DejaVuSans.ttf");
        }
        BaseFont baseFont = BaseFont.createFont("DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true,
                fontStream.readAllBytes(), null);


        Font titleFont = new Font(baseFont, 18, Font.BOLD);
        Font paragraphFont = new Font(baseFont, 14);

        Paragraph investigation = new Paragraph(conclusion.getInvestigation(), paragraphFont);
        investigation.setAlignment(Element.ALIGN_CENTER);
        document.add(investigation);

        Paragraph title = new Paragraph(conclusion.getArticle(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        Paragraph date = new Paragraph(utcFormatter.formatDateInRussian(conclusion.getRegistrationDate()), paragraphFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        document.add(date);

        Paragraph eventPlace = new Paragraph(conclusion.getEventPlace(), paragraphFont);
        eventPlace.setAlignment(Element.ALIGN_CENTER);
        document.add(eventPlace);

        Paragraph region = new Paragraph(conclusion.getRegion().getName(), paragraphFont);
        region.setAlignment(Element.ALIGN_CENTER);
        document.add(region);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph context = new Paragraph(conclusion.getPlot() + " " +
                conclusion.getDecision() + " " + conclusion.getResult(), paragraphFont);
        context.setAlignment(Element.ALIGN_CENTER);
        document.add(context);

        Paragraph defender = new Paragraph("Рассмотрел(-а): " + conclusion.getFullNameOfDefender(), paragraphFont);
        float x1 = document.right() - 120;
        float y1 = document.bottom() + 185;

        ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_RIGHT, defender,
                x1, y1, 0);

        Paragraph sign = new Paragraph("Подпись _______", paragraphFont);
        float x2 = document.right() - 120;
        float y2 = document.bottom() + 150;

        ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_RIGHT, sign,
                x2, y2, 0);

        InputStream imageStream = getClass().getClassLoader().getResourceAsStream("штамп.png");
        if (fontStream == null) {
            throw new IOException("Font file not found in resources: штамп.png");
        }
        byte[] imageBytes = imageStream.readAllBytes();

        Image image = Image.getInstance(imageBytes);
        image.scaleToFit(200, 100);
        float x3 = 100;
        float y3 = y2;
        image.setAbsolutePosition(x2, y2);
        document.add(image);

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}

