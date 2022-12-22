package edu.utica.slce.jobsub.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import edu.utica.slce.jobsub.logo.Logo;

public class HousingAvailableRoomHeaderEvnt implements IEventHandler {
    private final Logo logo;
    private Document document;
    private final PdfFont font;
    private final float fontSize;

    private final String termDescription;


    public HousingAvailableRoomHeaderEvnt(Logo logo, Document document, PdfFont font, float fontSize, String termDescription) {
        this.logo = logo;
        this.document = document;
        this.font = font;
        this.fontSize = fontSize;
        this.termDescription = termDescription;
    }

    @Override
    public void handleEvent(Event event) {
        byte[] barr = logo.getLogo();
        Image logo = new Image(ImageDataFactory.create(barr));
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = documentEvent.getDocument();
        PdfPage page = documentEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(),page.getResources(),pdfDocument);
        try (Canvas canvas = new Canvas(pdfCanvas,pageSize)) {
//            logo.scaleToFit(120f,100f)
//                    .setFixedPosition((pageSize.getLeft() + document.getLeftMargin()),(pageSize.getTop() - 80));
            logo.scale(0.1f,0.1f)
                    .setFixedPosition((pageSize.getLeft() + document.getLeftMargin()),(pageSize.getTop() - 80));
            canvas.add(logo);
            canvas.setFont(font);
            canvas.setFontSize(fontSize);
            canvas.showTextAligned(new Paragraph("SLRAVRM Housing Available Rooms"), pageSize.getWidth() / 2, (pageSize.getTop() - 40), TextAlignment.CENTER);
            canvas.showTextAligned(new Paragraph(termDescription).setMultipliedLeading(1.5f), pageSize.getWidth() / 2, ((pageSize.getTop() - 40)), TextAlignment.CENTER, VerticalAlignment.TOP);
        }
    }
}
