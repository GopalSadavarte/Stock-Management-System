/**
 * 
 * 
 * 
 * 
 * This File contains the class which create a pdf file of given data which display in the tabular format in the PDF file.
 * 
 */

package components.report.generate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.awt.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import partial.interfaces.FontInterface;

/**
 * The class PDFMaker are create an pdf file according to the given data.
 */
public class PDFMaker implements FontInterface {

    // instance variables.
    private String reportHeading, path, employeeName, fromDate, toDate, pageTotalColumn;
    private List<String> headers;
    private List<List<String>> data;
    private boolean isA4, havePageTotalColumn;
    private int pageTotalColumnIndex;

    /**
     * A constructor for creating an instance of PDFMaker without employee name and
     * without from date and to date and page total.
     * 
     * @param reportHeading is heading of report.
     * @param headers       is fields of the table.
     * @param data          is data of the table,which will display.
     * @param path          is path of document where it will save(folder path).
     * @param isA4          is to set the true for A4 size page and false for A3
     *                      size page.
     */
    public PDFMaker(String reportHeading, List<String> headers, List<List<String>> data, String path, boolean isA4) {
        this(reportHeading, null, headers, data, path, isA4, null, null, false, null);
    }

    /**
     * A constructor for creating an instance of PDFMaker with employee name,from
     * date and to date and page total.
     * 
     * @param reportHeading       is heading of report.
     * @param employeeName        is name of employee in which data will show.
     * @param headers             is the fields of table.
     * @param data                is the data of table ,which will display.
     * @param path                is the directory path where a pdf will save.
     * @param isA4                is true for A4 size page and false for A3 size
     *                            page.
     * @param fromDate            is start string formatted date value which will
     *                            display
     *                            in report.
     * @param toDate              is end date value of report which will display in
     *                            report.
     * @param havePageTotalColumn is true value if need to add total of particular
     *                            column in pdf.
     * @param pageTotalColumn     is name of the column which have to calculate
     *                            total.
     */
    public PDFMaker(String reportHeading, String employeeName, List<String> headers, List<List<String>> data,
            String path, boolean isA4, String fromDate, String toDate, boolean havePageTotalColumn,
            String pageTotalColumn) {
        this.data = data;
        this.reportHeading = reportHeading;
        this.headers = headers;
        this.path = path;
        this.employeeName = employeeName;
        this.isA4 = isA4;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.havePageTotalColumn = havePageTotalColumn;
        this.pageTotalColumn = pageTotalColumn;
    }

    /**
     * This method are create an actual PDF document with given data.
     * 
     * @return String - path where pdf are save.
     */
    public String generatePDF() {
        String result = null;
        try {
            // Create new PDF document.
            PDDocument document = new PDDocument();

            // Load an custom font from system.
            PDType0Font customFont = PDType0Font.load(document,
                    new File("C:\\Users\\user5\\AppData\\Local\\Microsoft\\Windows\\Fonts\\FiraCode-Medium.ttf"));

            String owner = "KK Enterprises";

            String address = "Shrirampur MIDC,belapur road,near prabhat dairy,shrirampur-431709";

            String time = new SimpleDateFormat("h:m a").format(new Date());
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            int pages = 1;
            /*
             * check the employee name are given or not if yes then set the 20 rows of each
             * page for table otherwise set 25 rows and number of pages.
             */
            if ((employeeName != null || havePageTotalColumn) && data.size() > 20) {
                pages = (int) Math.ceil(data.size() / 23) + 1;
            } else if (data.size() > 25) {
                pages = (int) Math.ceil(data.size() / 25) + 1;
            }
            // create a number of pages according to the data.
            PDPage page[] = new PDPage[pages];

            // create multiple pages according to the amount of data.
            for (int i = 0; i < page.length; i++) {
                // check the request for A4 size or not.
                if (isA4)
                    page[i] = new PDPage(PDRectangle.A4);
                else
                    page[i] = new PDPage(PDRectangle.A3);
                // add page in to document.
                document.addPage(page[i]);
            }

            // set left margin for pdf.
            float margin = 10;
            float yStart = 790; // Start from the top
            // modify starting from top.
            if (!isA4) {
                yStart = 1150;
            }
            // set the table width according to the width of page and margin from both side.
            float tableWidth = page[0].getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            // height of each row.
            float rowHeight = 25;

            // create an array of PDPageContentStream to draw the text.
            PDPageContentStream contentStream[] = new PDPageContentStream[pages];
            for (int i = 0; i < pages; i++) {
                // create content stream for each page.
                contentStream[i] = new PDPageContentStream(document, page[i]);
            }
            // set the number of records for each page.
            int startVal = 0, endVal = 25;
            // check the employee name not should be null if true then the number records
            // will reduce.
            if (employeeName != null || havePageTotalColumn) {
                endVal = 23;
                if (data.size() < 23)
                    endVal = data.size();
            } else {
                if (data.size() < 25) {
                    endVal = data.size();
                }
            }

            // get the width of the text.
            float textWidth = customFont.getStringWidth(owner) / 1000 * 25;

            // get the center position for the text.
            float xPositionOfHeading = (page[0].getMediaBox().getWidth() - textWidth) / 2;
            // draw the title.
            contentStream[0].setFont(customFont, 25);
            contentStream[0].beginText();
            contentStream[0].newLineAtOffset(xPositionOfHeading, yPosition);
            contentStream[0].showText(owner);
            contentStream[0].endText();

            // get the width of address text.
            textWidth = customFont.getStringWidth(address) / 1000 * 10;

            // get the center position of address just below of owner heading.
            xPositionOfHeading = (page[0].getMediaBox().getWidth() - textWidth) / 2;
            // move y axis position to below.
            yPosition -= 30;

            // draw address text.
            contentStream[0].setFont(customFont, 10);
            contentStream[0].beginText();
            contentStream[0].newLineAtOffset(xPositionOfHeading, yPosition);
            contentStream[0].showText(address);
            contentStream[0].endText();

            // create heading.
            String heading = "Time :" + time + "     " + reportHeading + "      Date :" + date;

            // get the width of heading.
            textWidth = customFont.getStringWidth(heading) / 1000 * 14;

            // get the center position of heading.
            xPositionOfHeading = (page[0].getMediaBox().getWidth() - textWidth) / 2;

            // move y axis position to below.
            yPosition -= 30;

            // draw the heading.
            contentStream[0].setFont(customFont, 14);
            contentStream[0].beginText();
            contentStream[0].newLineAtOffset(xPositionOfHeading, yPosition);
            contentStream[0].showText(heading);
            contentStream[0].endText();

            // check the employee name not should be null if yes then employee name text
            // will draw.
            if (employeeName != null) {
                // move y axis to below.
                yPosition -= 30;
                // Title
                contentStream[0].setFont(customFont, 12);
                contentStream[0].beginText();
                contentStream[0].newLineAtOffset(margin, yPosition);
                contentStream[0].showText("Employee Name :" + employeeName);
                contentStream[0].endText();
            }
            if (fromDate != null) {
                // move y axis to below.
                yPosition -= 30;
                String d[] = fromDate.split("\\-");
                fromDate = d[2] + "-" + d[1] + "-" + d[0];
                // Title
                contentStream[0].setFont(customFont, 12);
                contentStream[0].beginText();
                contentStream[0].newLineAtOffset(margin, yPosition);
                contentStream[0].showText("From :" + fromDate);
                contentStream[0].endText();
            }
            if (toDate != null) {
                String d[] = toDate.split("\\-");
                toDate = d[2] + "-" + d[1] + "-" + d[0];
                // Title
                contentStream[0].setFont(customFont, 12);
                contentStream[0].beginText();
                contentStream[0].newLineAtOffset(margin + 130, yPosition);
                contentStream[0].showText("To :" + toDate);
                contentStream[0].endText();
            }
            // loop for each page to draw table and footer.
            for (int i = 0; i < pages; i++) {

                if (!isA4 && i != 0) {
                    yPosition = 1150;
                }
                if (isA4 && i != 0) {
                    yPosition = 790;
                }
                // Draw Table with Borders
                float colWidth = tableWidth / headers.size();
                Object[] objects = drawTable(contentStream[i], page[i], margin, yPosition, tableWidth, colWidth,
                        rowHeight, headers, data,
                        document,
                        customFont, startVal, endVal);

                double pageTotalVal = Double.parseDouble(objects[0].toString());
                float yPositionForPageTotal = Float.parseFloat(objects[1].toString());

                // Footer
                drawFooter(contentStream[i], page[i], Integer.toString(i + 1), margin, 30, document, customFont,
                        pageTotalVal, yPositionForPageTotal);

                contentStream[i].close();
                // check for employee name.
                if (employeeName != null || havePageTotalColumn) {
                    if (data.size() <= 23) {
                        break;
                    }
                    startVal += 23;
                    endVal += 23;
                    if (data.size() < endVal)
                        endVal = data.size();
                } else {
                    if (data.size() <= 25) {
                        break;
                    }
                    startVal += 25;
                    endVal += 25;
                    if (data.size() < endVal)
                        endVal = data.size();
                }
            }

            // create format for unique name of pdf.
            String format = new SimpleDateFormat("_dd-MM-yyyy_h-m-s_a").format(new Date());
            String generatedPath = "";
            // check the employee name not should be null.
            if (employeeName != null)
                // path of file with employee name.
                generatedPath = path.trim() + "\\" + reportHeading.trim() + "_" + employeeName + format + ".pdf";
            else
                // file path without employee name.
                generatedPath = path.trim() + "\\" + reportHeading.trim() + format + ".pdf";
            // save pdf file at given path.
            document.save(generatedPath);
            // close the document.
            document.close();

            System.out.println("PDF Created Successfully: " + generatedPath);
            result = generatedPath;
        } catch (IOException e) {
            // execute when exception or directory not found.
            e.printStackTrace();
        }
        // return the resulted file path.
        return result;
    }

    // Method to Draw Table
    private Object[] drawTable(PDPageContentStream contentStream, PDPage page, float xStart, float yStart,
            float tableWidth,
            float colWidth, float rowHeight, List<String> headers, List<List<String>> data,
            PDDocument document, PDType0Font font, int startVal, int endVal) throws IOException {
        float yPosition = yStart - 30;
        double pageTotal = 0d;
        // Draw Header Row
        contentStream.setFont(font, 10);
        contentStream.setLineWidth(1f);
        contentStream.moveTo(xStart, yPosition);
        contentStream.lineTo(xStart + tableWidth, yPosition);
        contentStream.stroke();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).equals(pageTotalColumn) && havePageTotalColumn) {
                pageTotalColumnIndex = i;
            }
            contentStream.beginText();
            contentStream.newLineAtOffset(xStart + (i * colWidth) + 5, yPosition - 15);
            contentStream.setNonStrokingColor(navyBlue);
            contentStream.showText(headers.get(i));
            contentStream.setStrokingColor(Color.darkGray);
            contentStream.endText();
        }

        yPosition -= rowHeight;

        // Draw Data Rows
        contentStream.setFont(font, 10);

        for (int n = startVal; n < endVal; n++) {
            List<String> row = data.get(n);
            contentStream.moveTo(xStart, yPosition);
            contentStream.lineTo(xStart + tableWidth, yPosition);
            contentStream.stroke();

            for (int i = 0; i < row.size(); i++) {
                if (i == pageTotalColumnIndex && havePageTotalColumn) {
                    pageTotal += Double.parseDouble(row.get(i));
                }
                contentStream.beginText();
                contentStream.setNonStrokingColor(Color.darkGray);
                contentStream.newLineAtOffset(xStart + (i * colWidth) + 5, yPosition - 15);
                contentStream.showText(row.get(i));
                contentStream.endText();
            }

            yPosition -= rowHeight;
        }

        // Draw Bottom Line of the Table
        contentStream.moveTo(xStart, yPosition);
        contentStream.lineTo(xStart + tableWidth, yPosition);
        contentStream.stroke();

        // Draw Vertical Lines
        for (int i = 0; i <= headers.size(); i++) {
            float xPosition = xStart + (i * colWidth);
            contentStream.moveTo(xPosition, yStart - 30);
            contentStream.lineTo(xPosition, yPosition);
            contentStream.stroke();
        }
        return new Object[] { pageTotal, yPosition };
    }

    // Method to Draw Footer
    private void drawFooter(PDPageContentStream contentStream, PDPage page, String footerText,
            float xStart, float yPosition, PDDocument document, PDType0Font font, double pageTotalVal,
            float yPositionForPageTotal)
            throws IOException {
        float textWidth = font.getStringWidth(footerText) / 1000 * 12;
        contentStream.setFont(font, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset((page.getMediaBox().getWidth() - textWidth) / 2, yPosition);
        contentStream.showText(footerText);
        contentStream.endText();

        if (havePageTotalColumn) {
            String amount = "Total Amount: " + String.format("%.2f", pageTotalVal);
            float width = font.getStringWidth(amount) / 1000 * 14;
            xStart = page.getMediaBox().getWidth() - xStart - width;
            contentStream.setFont(font, 14);
            contentStream.setNonStrokingColor(Color.darkGray);
            contentStream.beginText();
            contentStream.newLineAtOffset(xStart, yPositionForPageTotal - 30);
            contentStream.showText(amount);
            contentStream.endText();
        }
    }
}
/**
 * This class end here...
 */