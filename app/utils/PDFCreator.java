package utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import models.TripNode;
import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class PDFCreator {

    public File generateTripEmailPDF() {
        Document doc = null;
        try {
            //Grabs the html file for trip email to be converted to a string
            File input = new File("resources/templates/trip_template_test.html");
            System.out.println(input.toString());
            doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String html_template = doc.toString();
        //Returns a string representation of the html for trip email to be converted to a pdf
        String destinations_and_details = "<tr>\n" +
                "<td style=\"width: 25%; text-align: center;\">North Pole</td>\n" +
                "<td style=\"width: 25%; text-align: center;\">24-12-2019</td>\n" +
                "<td style=\"width: 25%; text-align: center;\">26-12-2019</td>\n" +
                "<td style=\"width: 25%; text-align: center;\">Santa doesn't exist</td>\n" +
                "</tr>";
        html_template = html_template.replace("destinations_and_details", destinations_and_details);
        String start_date = "<td style=\"width: 65.9508%; text-align: center;\"><span>30-07-1998</span></td>";
        html_template = html_template.replace("start_date", start_date);
        String end_date = "<td style=\"width: 65.9508%; text-align: center;\"><span>25-12-2019</span></td>";
        html_template = html_template.replace("end_date", end_date);
        String num_of_travellers = "<td style=\"width: 65.9508%; text-align: center;\"><span>3</span></td>";
        html_template = html_template.replace("num_of_travellers", num_of_travellers);
        String traveller_list = "<tr>\n" +
                                    "<td>Tiger Huang</td>\n" +
                                    "<td>09-09-1998</td>\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                    "<td>Dana Lambert</td>\n" +
                                    "<td>13-07-1998</td>\n" +
                                "</tr>";
        html_template = html_template.replace("traveller_list", traveller_list);
//        return generatePDF(doc.toString());
        return generatePDF(html_template);
    }

    private File generatePDF(String htmlTemplate) {
        /**
         * Creates a pdf file when given a string representation of an html file.
         */
        try {
            File tempFile = File.createTempFile("out", ".pdf");
            OutputStream os = new FileOutputStream(tempFile);
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            //takes a string object representing an html file and converts it to a pdf
            builder.withHtmlContent(htmlTemplate, "");
            builder.toStream(os);
            builder.run();
            os.close();
            return tempFile;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
