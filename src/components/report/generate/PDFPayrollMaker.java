/**
 * 
 * 
 * 
 * This file contains the code to generate a employee payroll slip with given data.
 * 
 * 
 */

package components.report.generate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 * The class PDFPayrollMaker are create an payroll of employee.
 */
public class PDFPayrollMaker {

    // instance variables/identifiers/references.
    private String employeeName, basicSalary, presents, month, absents, payableAmount, deposit, advance, overtimeAmount,
            overtimeRate, pending,
            overtimeHours, half, path;
    private boolean isUpdate;

    /**
     * A Constructor to get the data for generating a Payroll PDF.
     * 
     * @param employeeName   a name of employee.
     * @param basicSalary    a basic salary of employee.
     * @param present_days   total present days of given month.
     * @param half_days      total half days of given month.
     * @param absent_days    total absent days of given month.
     * @param overtimeRate   rate of per hour for overtime hours.
     * @param overtimeHours  the overtime hours value such as 3,3.3,etc.
     * @param overtimeAmount the amount of each hour.
     * @param advanceAmount  the advance amount of employee.
     * @param depositAmount  the deposited amount of employee.
     * @param pendingAmount  the pending amount after depositing some advances.
     * @param payableAmount  the amount which actually will pay to employee after
     *                       deducting/adding the above amounts.
     * @param payrollMonth   the month in which payroll will generate.
     * @param path           the path of directory where PDF file will store/save.
     * @param isUpdate       a boolean value if the payroll in update mode.
     */
    public PDFPayrollMaker(String employeeName, String basicSalary, String present_days,
            String half_days, String absent_days,
            String overtimeRate,
            String overtimeHours,
            String overtimeAmount, String advanceAmount, String depositAmount, String pendingAmount,
            String payableAmount,
            String payrollMonth, String path, boolean isUpdate) {

        this.employeeName = employeeName;
        this.basicSalary = basicSalary;
        this.presents = present_days;
        this.absents = absent_days;
        this.half = half_days;
        this.overtimeRate = overtimeRate;
        this.overtimeHours = overtimeHours;
        this.overtimeAmount = overtimeAmount;
        this.deposit = depositAmount;
        this.advance = advanceAmount;
        this.pending = pendingAmount;
        this.payableAmount = payableAmount;
        this.month = payrollMonth;
        this.path = path;
        this.isUpdate = isUpdate;
    }

    /**
     * This method will actually generate an pdf file of payroll according to given
     * data.
     * 
     * @return String - path of directory where pdf are saved.
     */
    public String generatePDF() {
        String result = null;
        try {
            // create a document.
            PDDocument document = new PDDocument();
            // create a A4 page.
            PDPage page = new PDPage(PDRectangle.A4);
            // add page in to document.
            document.addPage(page);

            // Load a font for pdf from system.
            PDType0Font font = PDType0Font.load(document,
                    new File("C:\\Users\\user5\\AppData\\Local\\Microsoft\\Windows\\Fonts\\FiraCode-Medium.ttf"));

            // set margin for document.
            float margin = 30;
            float centerX = page.getMediaBox().getWidth() / 2 - margin;
            float yPosition = page.getMediaBox().getHeight() - margin;

            // Report Section Dimensions
            float reportHeight = (page.getMediaBox().getHeight() - 2 * margin) / 2 - 10;
            float reportWidth = page.getMediaBox().getWidth() - 2 * margin;
            float xStart = margin;
            float yStartTop = yPosition;
            float yStartBottom = yPosition - reportHeight - 20; // Space between two copies

            // basic data of headings.
            String owner = "KK Enterprises";
            String address = "Shrirampur MIDC, Belapur Road, Near Prabhat Dairy, Shrirampur-431709";
            String heading = "Payroll Report";

            // Create Content Stream
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Draw two copies with identical formatting
                drawPayrollCopy(contentStream, font, xStart, yStartTop, reportWidth, reportHeight, centerX,
                        owner, address, heading);

                drawPayrollCopy(contentStream, font, xStart, yStartBottom, reportWidth, reportHeight, centerX,
                        owner, address, heading);
            }

            // get the format for unique file name.
            String format = new SimpleDateFormat("_dd-MM-yyyy_h-m-s").format(new Date());
            String generatedPath = "";
            // check it is update.
            if (isUpdate) {
                generatedPath = path.trim() + "\\payroll_report_update_" + employeeName.trim() + format + ".pdf";
            } else {
                generatedPath = path.trim() + "\\payroll_report_" + employeeName.trim() + format + ".pdf";
            }
            // save document at given path.
            document.save(generatedPath);
            result = generatedPath;
            System.out.println("Payroll PDF generated successfully: " + generatedPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // return the path of pdf where it saved.
        return result;
    }

    // Function to draw one payroll copy
    private void drawPayrollCopy(PDPageContentStream contentStream, PDType0Font font, float x, float y,
            float width, float height, float centerX, String owner, String address, String heading)
            throws IOException {

        // Draw Border
        contentStream.setLineWidth(1);
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + width, y);
        contentStream.lineTo(x + width, y - height);
        contentStream.lineTo(x, y - height);
        contentStream.lineTo(x, y);
        contentStream.stroke();

        float textMargin = x + 15;
        float yPosition = y - 30;

        // Owner Name (Centered)
        contentStream.setFont(font, 20);
        drawCenteredText(contentStream, owner, centerX, yPosition);
        yPosition -= 25;

        // Address (Centered)
        contentStream.setFont(font, 12);
        drawCenteredText(contentStream, address, centerX, yPosition);
        yPosition -= 30;

        // Report Heading (Centered)
        contentStream.setFont(font, 13);
        drawCenteredText(contentStream, heading, centerX, yPosition);

        // Month (Right-Aligned)
        contentStream.beginText();
        contentStream.setFont(font, 13);
        contentStream.newLineAtOffset(x + width - 180, yPosition);
        contentStream.showText("Month: " + month);
        contentStream.endText();

        yPosition -= 40;

        // Draw Table Headers
        contentStream.setFont(font, 12);
        drawText(contentStream, "Employee Name:", textMargin, yPosition);
        drawText(contentStream, employeeName, textMargin + 130, yPosition);

        yPosition -= 20;
        drawText(contentStream, "Basic Salary:", textMargin, yPosition);
        drawText(contentStream, basicSalary, textMargin + 130, yPosition);

        yPosition -= 20;
        drawText(contentStream, "Present Days:", textMargin, yPosition);
        drawText(contentStream, presents, textMargin + 130, yPosition);

        drawText(contentStream, "Absent Days:", textMargin + 250, yPosition);
        drawText(contentStream, absents, textMargin + 400, yPosition);

        yPosition -= 20;
        drawText(contentStream, "Half Days:", textMargin, yPosition);
        drawText(contentStream, half, textMargin + 130, yPosition);

        yPosition -= 20;
        drawText(contentStream, "Overtime Hours:", textMargin, yPosition);
        drawText(contentStream, overtimeHours, textMargin + 130, yPosition);

        drawText(contentStream, "Rate Of Per Hour:", textMargin + 250, yPosition);
        drawText(contentStream, overtimeRate, textMargin + 400, yPosition);

        yPosition -= 20;
        drawText(contentStream, "Overtime Amount:", textMargin, yPosition);
        drawText(contentStream, overtimeAmount, textMargin + 130, yPosition);

        yPosition -= 20;
        drawText(contentStream, "Advance Taken:", textMargin, yPosition);
        drawText(contentStream, advance, textMargin + 130, yPosition);

        drawText(contentStream, "Deposit Amount:", textMargin + 250, yPosition);
        drawText(contentStream, deposit, textMargin + 400, yPosition);

        yPosition -= 20;
        drawText(contentStream, "Pending Amount:", textMargin, yPosition);
        drawText(contentStream, pending, textMargin + 130, yPosition);

        yPosition -= 30;
        // Payable Amount (Bold & Larger)
        contentStream.setFont(font, 15);
        drawText(contentStream, "Total Payable Amount:", textMargin, yPosition);
        contentStream.setFont(font, 17);
        drawText(contentStream, payableAmount + "/-", textMargin + 200, yPosition);

        yPosition -= 60;

        // Draw Signature Section
        contentStream.setFont(font, 12);
        drawText(contentStream, "Signature (Payroll Generator)", textMargin, yPosition);
        drawText(contentStream, "Signature (Owner)", x + width - 150, yPosition);
    }

    // Utility to draw centered text
    private void drawCenteredText(PDPageContentStream contentStream, String text, float centerX, float y)
            throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(centerX - (text.length() * 3), y);
        contentStream.showText(text);
        contentStream.endText();
    }

    // Utility to draw text at specific position
    private void drawText(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }
}
/**
 * This class end here...
 */