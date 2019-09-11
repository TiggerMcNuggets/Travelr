package utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import models.DestinationNode;
import models.TripNode;
import models.UserGroup;
import org.jsoup.*;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PDFCreator {

    public File generateTripEmailPDF(TripNode trip, List<HashMap<TripNode, DestinationNode>> dests) {
        Document doc = null;
        try {
            //Grabs the html file for trip email to be converted to a string
            File input = new File("resources/templates/trip_template_test.html");
            doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String html_template = doc.toString();

        //Returns a string representation of the html for trip email to be converted to a pdf
        String destinations_and_details = "";
        for (HashMap dest: dests) {
            SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd");
            TripNode tripNode = (TripNode) dest.keySet().toArray()[0];
            DestinationNode node = (DestinationNode) dest.get(dest.keySet().toArray()[0]);
            String arrival = jdf.format(new Date(node.getArrivalDate() * 1000L));
            String departure = jdf.format(new Date(node.getDepartureDate() * 1000L));
            destinations_and_details = destinations_and_details + "<tr>\n" +
                    "<td style=\"width: 25%; text-align: center;\">" + tripNode.getName() + "</td>\n" +
                    "<td style=\"width: 25%; text-align: center;\">" + node.getDestination().getName() + "</td>\n" +
                    "<td style=\"width: 25%; text-align: center;\">" + arrival + "</td>\n" +
                    "<td style=\"width: 25%; text-align: center;\">" + departure + "</td>\n" +
                    "</tr>";
        }
        html_template = html_template.replace("destinations_and_details", destinations_and_details);
        String start_date = "<td style=\"width: 65.9508%; text-align: center;\"><span>30-07-1998</span></td>";

        html_template = html_template.replace("start_date", start_date);
        String end_date = "<td style=\"width: 65.9508%; text-align: center;\"><span>25-12-2019</span></td>";

        html_template = html_template.replace("end_date", end_date);


        if (trip.getUserGroup() != null) {
            String num_of_travellers = "<td style=\"width: 65.9508%; text-align: center;\"><span>" + Integer.toString(trip.getUserGroup().getUserGroups().size()) + "</span></td>";
            html_template = html_template.replace("num_of_travellers", num_of_travellers);
            String traveller_list = "";
            for (UserGroup group : trip.getUserGroup().getUserGroups()) {
                System.out.println(group.getUser().getFirstName());
                traveller_list = traveller_list + "<tr>\n" +
                        "<td>" + group.getUser().getFirstName() + " " + group.getUser().getLastName() + "</td>\n" +
                        "<td>" + group.getUser().getDateOfBirth() + "</td>\n" +
                        "</tr>";
            }
            html_template = html_template.replace("traveller_list", traveller_list);
        } else {
            html_template = html_template.replace("num_of_travellers", "<td style=\"width: 65.9508%; text-align: center;\"><span>0</span></td>");
            html_template = html_template.replace("traveller_list", "<tr><td>No Travellers</td><td></td></tr>");
        }
        Document newDoc = Jsoup.parse(html_template);
        return generatePDF(new W3CDom().fromJsoup(newDoc));
    }

    private File generatePDF(org.w3c.dom.Document htmlTemplate) {
        /**
         * Creates a pdf file when given a string representation of an html file.
         */
        try {
            File tempFile = File.createTempFile("out", ".pdf");
            OutputStream os = new FileOutputStream(tempFile);
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withW3cDocument(htmlTemplate, "");
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
