package edu.utica.slce.jobsub.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

public class HousingAvailableRoomsFooterEvnt implements IEventHandler {
    private final Document document;
    private final PdfFont font;
    private final float fontSize;
    private final PdfFormXObject placeHolder;

    public HousingAvailableRoomsFooterEvnt(Document document, PdfFont font, float fontSize) {
        this.document = document;
        this.font = font;
        this.fontSize = fontSize;
        placeHolder = new PdfFormXObject(new Rectangle(0, 0, 20, 20));
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = documentEvent.getDocument();
        PdfPage page = documentEvent.getPage();
        int pageNumber = pdfDocument.getPageNumber(page);
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(),page.getResources(),pdfDocument);
        try (Canvas canvas = new Canvas(pdfCanvas,pageSize)) {
            canvas.setFont(font);
            canvas.setFontSize(fontSize);
            String pageOf = "Page %d of ".formatted(pageNumber);
            Paragraph paragraph = new Paragraph()
                    .add(pageOf);
            canvas.showTextAligned(paragraph,(pageSize.getLeft() + document.getLeftMargin()),20,TextAlignment.JUSTIFIED);
            pdfCanvas.addXObjectAt(placeHolder,(pageSize.getLeft() + document.getLeftMargin() + font.getWidth(pageOf,fontSize)), 20);
            pdfCanvas.release();
        }
    }

    public void pageTotal(PdfDocument pdfDocument) {
        try (Canvas canvas = new Canvas(placeHolder, pdfDocument)) {
            canvas.setFont(font);
            canvas.setFontSize(fontSize);
            canvas.showTextAligned(String.valueOf(pdfDocument.getNumberOfPages()), 0, 0, TextAlignment.LEFT);
        }
    }

}
