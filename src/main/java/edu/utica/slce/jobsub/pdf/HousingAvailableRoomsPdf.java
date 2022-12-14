package edu.utica.slce.jobsub.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
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
import edu.utica.slce.jobsub.model.HousingAvailableRoom;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
public class HousingAvailableRoomsPdf {

    final PdfFont font;
    final PdfFont bold;

    public HousingAvailableRoomsPdf() throws IOException {
        font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
    }

    public void createPdf(Path pdf, List<HousingAvailableRoom> list) throws IOException{
        try (PdfWriter writer = new PdfWriter(pdf.toFile());
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument,PageSize.A4.rotate())) {

            document.setMargins(100,20,40,20);
            document.add(createReportTable(list));

        } catch (IOException ioException) {
            throw ioException;
        }
    }

    public Table createReportTable(List<HousingAvailableRoom> list) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{10,10})).useAllAvailableWidth();
        table.setFontSize(10);
        table.addHeaderCell(new Cell().add(new Paragraph("Building").setFont(bold).setUnderline()).setBorder(null));
        table.addHeaderCell(new Cell().add(new Paragraph("Room").setFont(bold).setUnderline()).setBorder(null));
        list
                .stream()
                .collect(groupingBy(HousingAvailableRoom::getBuildingCode, LinkedHashMap::new, Collectors.toList()))
                .forEach((k,v) -> {
                    table.addCell(new Cell());
                    table.addCell(new Cell());
                    v.forEach(room -> {
                            table.addCell(new Cell().add(new Paragraph(room.getBuildingCode()).setFont(font)).setBorder(null));
                            table.addCell(new Cell().add(new Paragraph(room.getRoomCode()).setFont(font)).setBorder(null));
                    });
                });
        return table;
    }

}
