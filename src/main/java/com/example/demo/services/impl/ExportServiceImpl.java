package com.example.demo.services.impl;

import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.Conclusion;
import com.example.demo.repository.ConclusionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.ConclusionService;
import com.example.demo.services.ExportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;


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
                row.createCell(16).setCellValue(conclusion.getInvestigator().getName() + " " + conclusion.getInvestigator().getSecondName());
                row.createCell(17).setCellValue(conclusion.getStatus());
                row.createCell(18).setCellValue(conclusion.getRelationToEvent());
                row.createCell(19).setCellValue(conclusion.getInvestigationTypes());
                row.createCell(20).setCellValue(conclusion.isRelatesToBusiness());
                row.createCell(21).setCellValue(conclusion.isRelatesToBusiness());
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
    public ByteArrayInputStream exportToPdf(String IIN) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4.rotate());
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, out);
            document.open();

            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD);
            document.add(new com.itextpdf.text.Paragraph("Conclusions Report", titleFont));
            document.add(com.itextpdf.text.Chunk.NEWLINE);

            com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(26);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = new float[26];
            Arrays.fill(columnWidths, 4f);
            table.setWidths(columnWidths);

            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD);
            for (String column : new String[]{
                    "Registration Number", "Creation Date", "UD", "Registration Date",
                    "Article", "Decision", "Plot", "IIN of Called", "Full Name of Called",
                    "Job Title of Called", "BIN/IIN of Called", "Job Place", "Region",
                    "Planned Actions", "Event Time", "Event Place", "Investigator",
                    "Status", "Relation", "Investigation", "Is Business",
                    "BIN/IIN Pension", "IIN of Defender", "Full Name of Defender",
                    "Justification", "Result"
            }) {
                com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(column, headerFont));
                headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
                headerCell.setBackgroundColor(new com.itextpdf.text.BaseColor(200, 200, 200));
                table.addCell(headerCell);
            }

            com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9);
            List<ConclusionDto> conclusions = conclusionService.userConclusions(IIN);
            boolean alternate = false;

            for (ConclusionDto conclusion : conclusions) {
                alternate = !alternate;
                com.itextpdf.text.BaseColor rowColor = alternate ? new com.itextpdf.text.BaseColor(230, 230, 230) : com.itextpdf.text.BaseColor.WHITE;

                for (String cellData : new String[]{
                        conclusion.getRegistrationNumber(),
                        conclusion.getCreationDate().toString(),
                        conclusion.getUdNumber(),
                        conclusion.getRegistrationDate().toString(),
                        conclusion.getArticle(),
                        conclusion.getDecision(),
                        conclusion.getSummary(),
                        conclusion.getCalledPersonIIN(),
                        conclusion.getCalledPersonFullName(),
                        conclusion.getCalledPersonPosition(),
                        conclusion.getCalledPersonBIN(),
                        conclusion.getWorkPlace(),
                        conclusion.getRegion().getName(),
                        conclusion.getPlannedInvestigativeActions(),
                        conclusion.getEventDateTime().toString(),
                        conclusion.getEventPlace(),
                        conclusion.getInvestigator().getName() + " " + conclusion.getInvestigator().getSecondName(),
                        conclusion.getStatus(),
                        conclusion.getRelationToEvent(),
                        conclusion.getInvestigationTypes(),
                        String.valueOf(conclusion.isRelatesToBusiness()),
                        String.valueOf(conclusion.isRelatesToBusiness()),
                        conclusion.getDefenseAttorneyIIN(),
                        conclusion.getDefenseAttorneyFullName(),
                        conclusion.getJustification(),
                        conclusion.getResult()
                }) {
                    com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(cellData, dataFont));
                    cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
                    cell.setBackgroundColor(rowColor);
                    table.addCell(cell);
                }
            }

            document.add(table);
            document.close();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to export data to PDF: " + e.getMessage(), e);
        }
    }


}
