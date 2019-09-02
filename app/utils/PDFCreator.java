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
            File input = new File("resources/pdfs/trip_email_title.html");
            doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Returns a string representation of the html for trip email to be converted to a pdf
        return generatePDF(doc.toString());
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
