package edu.utica.jobsub.slce.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import edu.utica.jobsub.slce.logo.Logo;
import edu.utica.jobsub.slce.model.HousingAvailableRoom;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

@Component
public class HousingAvailableRoomsPdf {

    final private PdfFont font;
    final private PdfFont bold;

    private Logo logo;

    @Autowired
    public HousingAvailableRoomsPdf(Logo logo) throws IOException{
        this();
        this.logo = logo;
    }
    public HousingAvailableRoomsPdf() throws IOException {
        font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
    }

    /**
     * Create the report PDF document using iText library.
     * @param pdf
     * @param list
     * @throws IOException
     */
    public void createPdf(Path pdf, List<HousingAvailableRoom> list) throws IOException{
        String termDescription =
                list.stream()
                .findFirst()
                .get()
                .getTermDescription();

        try (PdfWriter writer = new PdfWriter(pdf.toFile());
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument,PageSize.A4.rotate())) {

            document.setMargins(100,20,40,20);
            HousingAvailableRoomHeaderEvnt header = new HousingAvailableRoomHeaderEvnt(logo, document, bold,10, termDescription);
            pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE,header);
            HousingAvailableRoomsFooterEvnt footer = new HousingAvailableRoomsFooterEvnt(document,font,10);
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,footer);
            document.add(createReportTable(list));
            footer.pageTotal(pdfDocument);
        } catch (IOException ioException) {
            throw ioException;
        }
    }

    /**
     * Return iText Layout Table Element containing housing available rooms data.
     * @param list
     * @return
     */
    public Table createReportTable(List<HousingAvailableRoom> list) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{12,10,28,14,8,8,8,8})).useAllAvailableWidth();
        table.setFontSize(10);
        table.addHeaderCell(new Cell().add(new Paragraph("Building").setFont(bold).setUnderline()).setBorder(null));
        table.addHeaderCell(new Cell().add(new Paragraph("Room").setFont(bold).setUnderline()).setBorder(null));
        table.addHeaderCell(new Cell().add(new Paragraph("Description").setFont(bold).setUnderline()).setBorder(null));
        table.addHeaderCell(new Cell().add(new Paragraph("Category").setFont(bold).setUnderline()).setBorder(null));
        table.addHeaderCell(new Cell().add(new Paragraph("Gender").setFont(bold).setUnderline()).setBorder(null));
        table.addHeaderCell(new Cell().add(new Paragraph("Capacity").setFont(bold).setUnderline()).setBorder(null));
        table.addHeaderCell(new Cell().add(new Paragraph("Occupancy").setFont(bold).setUnderline()).setBorder(null));
        table.addHeaderCell(new Cell().add(new Paragraph("Available").setFont(bold).setUnderline()).setBorder(null));
        list
                .stream()
                .collect(groupingBy(HousingAvailableRoom::getBuildingCode, LinkedHashMap::new, Collectors.toList()))
                .forEach((k,v) -> {
                    IntStream.range(0,table.getNumberOfColumns())
                            .forEach( i -> table.addCell(new Cell().setBorder(null).setPaddingBottom(10f)));
                    v.forEach(room -> {
                            table.addCell(new Cell().add(new Paragraph(room.getBuildingCode()).setFont(font).setMultipliedLeading(1f)).setBorder(null));
                            table.addCell(new Cell().add(new Paragraph(room.getRoomCode()).setFont(font).setMultipliedLeading(1f)).setBorder(null));
                            table.addCell(new Cell().add(new Paragraph(room.getRoomDescription()).setFont(font).setMultipliedLeading(1f)).setBorder(null));
                            table.addCell(new Cell().add(new Paragraph(StringUtils.defaultString(room.getCategoryDescription(),"")).setFont(font).setMultipliedLeading(1f)).setBorder(null));
                            table.addCell(new Cell().add(new Paragraph(room.getRoomGender()).setFont(font).setMultipliedLeading(1f)).setBorder(null));
                            table.addCell(new Cell().add(new Paragraph(room.getCapacity().toString()).setFont(font).setMultipliedLeading(1f)).setBorder(null));
                            table.addCell(new Cell().add(new Paragraph(room.getOccupancy().toString()).setFont(font).setMultipliedLeading(1f)).setBorder(null));
                            table.addCell(new Cell().add(new Paragraph(room.getAvailable().toString()).setFont(font).setMultipliedLeading(1f)).setBorder(null));
                    });
                });
        return table;
    }


}
