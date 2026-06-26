package com.safehome.seismic_portal.controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.Color;
import java.io.IOException;

@Controller
public class ReportDownloadController {

    @GetMapping("/api/report/download")
    public void downloadReportCard(
            @RequestParam(defaultValue = "Unknown") String pincode,
            @RequestParam(defaultValue = "Unknown") String location,
            @RequestParam(defaultValue = "Unknown") String zone,
            @RequestParam(defaultValue = "Unknown") String soil,
            @RequestParam(defaultValue = "Unknown") String era,
            @RequestParam(defaultValue = "Unknown") String pga,
            @RequestParam(defaultValue = "Unknown") String intensity,
            @RequestParam(defaultValue = "Unknown") String forecast,
            HttpServletResponse response) throws IOException {

        // Configure standard response stream headers for document delivery
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Seismic_Risk_Report_" + pincode + ".pdf");

        // Initialize PDF Document
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Design Palette Color Configuration
        Color primaryBlue = new Color(30, 58, 138);   // #1e3a8a
        Color textDark = new Color(30, 41, 59);      // #1e293b
        Color lightGrey = new Color(241, 245, 249);  // #f1f5f9
        Color borderColor = new Color(226, 232, 240); // #e2e8f0

        // Typography Setup
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, primaryBlue);
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(100, 116, 139));
        Font sectionHeadingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, primaryBlue);
        Font cellLabelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, textDark);
        Font cellValFont = FontFactory.getFont(FontFactory.HELVETICA, 10, textDark);

        // Header Section Title Elements
        Paragraph title = new Paragraph("SEISMIC SAFETY PORTAL", titleFont);
        title.setSpacingAfter(2);
        document.add(title);

        Paragraph sub = new Paragraph("Official Geotechnical & Structural Hazard Vulnerability Assessment Report", subTitleFont);
        sub.setSpacingAfter(20);
        document.add(sub);

        // Grid Table 1: Metadata Summary Layout
        PdfPTable metaTable = new PdfPTable(4);
        metaTable.setWidthPercentage(100);
        metaTable.setSpacingAfter(20);

        addTableCell(metaTable, "Location Vector:", cellLabelFont, lightGrey, borderColor);
        addTableCell(metaTable, location, cellValFont, Color.WHITE, borderColor);
        addTableCell(metaTable, "Pincode Metric:", cellLabelFont, lightGrey, borderColor);
        addTableCell(metaTable, pincode, cellValFont, Color.WHITE, borderColor);

        addTableCell(metaTable, "Seismic Zoning:", cellLabelFont, lightGrey, borderColor);
        addTableCell(metaTable, zone, cellValFont, Color.WHITE, borderColor);
        addTableCell(metaTable, "Soil Classification:", cellLabelFont, lightGrey, borderColor);
        addTableCell(metaTable, soil, cellValFont, Color.WHITE, borderColor);

        document.add(metaTable);

        // Section Section: Detailed Structural Audit Checklist
        Paragraph secHeading = new Paragraph("CRITICAL STRUCTURAL RISK SUMMARY", sectionHeadingFont);
        secHeading.setSpacingAfter(10);
        document.add(secHeading);

        PdfPTable riskTable = new PdfPTable(2);
        riskTable.setWidthPercentage(100);
        riskTable.setWidths(new float[]{30f, 70f});
        riskTable.setSpacingAfter(25);

        // Row 1: Structural Era
        addTableCell(riskTable, "Construction Era Risk", cellLabelFont, lightGrey, borderColor);
        addTableCell(riskTable, "Design engineered under the " + era + " parameters. " +
                "Properties constructed prior to major Indian Standard building code cycles require verification checks against modern ductile loop tie configurations.", cellValFont, Color.WHITE, borderColor);

        // Row 2: Soil Parameter Analysis
        addTableCell(riskTable, "Geotechnical Soil Factor", cellLabelFont, lightGrey, borderColor);
        addTableCell(riskTable, "Configured using " + soil + " profiles. Loose stratum layers amplify structural shaking acceleration patterns drastically compared to solid base rock frameworks.", cellValFont, Color.WHITE, borderColor);

        // Row 3: PGA Value
        addTableCell(riskTable, "Peak Ground Acceleration", cellLabelFont, lightGrey, borderColor);
        addTableCell(riskTable, "Estimated PGA: " + pga + ". This represents the maximum expected ground shaking force at this location per IS 1893:2016 zone factor tables.", cellValFont, Color.WHITE, borderColor);

// Row 4: Intensity Level
        addTableCell(riskTable, "Shaking Intensity (MMI)", cellLabelFont, lightGrey, borderColor);
        addTableCell(riskTable, "Expected intensity: " + intensity + " on the Modified Mercalli Intensity scale based on zone classification.", cellValFont, Color.WHITE, borderColor);

// Row 5: Structural Forecast
        addTableCell(riskTable, "Structural Vulnerability", cellLabelFont, lightGrey, borderColor);
        addTableCell(riskTable, forecast, cellValFont, Color.WHITE, borderColor);

        document.add(riskTable);

        // Section 3: Engineering Guidelines Callout Area
        Paragraph guidelinesHeading = new Paragraph("MANDATORY INDIAN STANDARD COMPLIANCE CODES", sectionHeadingFont);
        guidelinesHeading.setSpacingAfter(10);
        document.add(guidelinesHeading);

        Paragraph guidanceText = new Paragraph();
        guidanceText.setFont(FontFactory.getFont(FontFactory.HELVETICA, 10, textDark));
        guidanceText.setLeading(15f);
        guidanceText.add("• IS 1893:2016 Compliance: Verify building response shear designs meet seismic zone limits.\n");
        guidanceText.add("• IS 13920 Ductile Detailing: Ensure mandatory confinement hoops bind critical beam-column connection matrices.\n");
        guidanceText.add("• Safe Engineering Advisory: Conduct active structural site checks if evaluating structural extensions on legacy masonry footprints.");

        // Wrap guidance in an clean box frame
        PdfPTable boxContainer = new PdfPTable(1);
        boxContainer.setWidthPercentage(100);
        PdfPCell boxCell = new PdfPCell(guidanceText);
        boxCell.setPadding(15);
        boxCell.setBackgroundColor(new Color(240, 253, 244)); // Light desaturated green accent tint
        boxCell.setBorderColor(new Color(22, 163, 74));
        boxCell.setBorderWidthLeft(4f); // Thick structural accent band
        boxContainer.addCell(boxCell);

        document.add(boxContainer);

        // Document Legal Footer Notice
        String timestamp = new java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a"){{
            setTimeZone(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
        }}.format(new java.util.Date());
        Paragraph footerNotice = new Paragraph("\n\n\n\n\n\n\n\nReport Generated: " + timestamp +
                " | IIT Roorkee Earthquake Engineering Department\n" +
                "* Disclaimer: This automated report card provides mathematical estimations based upon public Bureau of Indian Standards mapping datasets. Direct structural engineering blueprints override general automated computations.",
                FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC, Color.GRAY));
        footerNotice.setAlignment(Element.ALIGN_CENTER);
        document.add(footerNotice);        footerNotice.setAlignment(Element.ALIGN_CENTER);

        document.close();
    }

    private void addTableCell(PdfPTable table, String text, Font font, Color bg, Color border) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setPadding(10);
        cell.setBackgroundColor(bg);
        cell.setBorderColor(border);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
}