package utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.FileOutputStream;
import java.io.OutputStream;

import static java.lang.System.out;
import java.util.*;

public class PDFCreator {

    public static void generatePDF() {
        System.out.println("called");
        try (OutputStream os = new FileOutputStream("/Users/Tiger/OneDrive/Documents/pdf/out.pdf")) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withUri("file:///Users/Tiger/OneDrive/Documents/pdf/in.htm");
            builder.toStream(os);
            builder.run();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
