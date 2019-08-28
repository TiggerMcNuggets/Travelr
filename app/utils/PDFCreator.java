package utils;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PDFCreator {

    public File generatePDF() {

        try {
            File tempFile = File.createTempFile("out", ".pdf");
            OutputStream os = new FileOutputStream(tempFile);
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent("<html><h1>HELLO</h1><h1>WORLD</h1></html>", "");
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
