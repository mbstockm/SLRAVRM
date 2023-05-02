package edu.utica.jobsub.slce;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sct.messaging.bif.BatchProcessorException;
import com.sct.messaging.bif.BatchResourceHolder;
import com.sct.messaging.bif.banner.BannerBatchProcessor;
import edu.utica.jobsub.slce.logo.Logo;
import edu.utica.jobsub.slce.model.HousingAvailableRoom;
import edu.utica.jobsub.slce.service.HousingAvailableRoomsService;
import edu.utica.jobsub.slce.csv.HousingAvailableRoomsCsv;
import edu.utica.jobsub.slce.pdf.HousingAvailableRoomsPdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Banner Job Submission Report SLRAVRM Housing Available Rooms .
 * Example Java process using Ellucian Banner Batch Processor & Spring Boot with OpenCsv and iText libraries.
 * @author Michael Stockman
 */
@SpringBootApplication
public class Slravrm extends BannerBatchProcessor implements CommandLineRunner {

    public static String jobName;
    public static String jobNumber;
    @Autowired
    private HousingAvailableRoomsService housingAvailableRoomsService;
    @Autowired
    private HousingAvailableRoomsCsv housingAvailableRoomsCsv;
    @Autowired
    private HousingAvailableRoomsPdf housingAvailableRoomsPdf;
    @Autowired
    private Logo logo;
    @Value("${csv.output.path}")
    private String csvPath;
    @Value("${pdf.output.path}")
    private String pdfPath;

    @Override
    public void processJob() {
        new SpringApplicationBuilder(Slravrm.class)
                .web(WebApplicationType.NONE)
                .run(getJobName(),getJobNumber());
    }

    @Override
    public void run(String[] args) throws BatchProcessorException {
        Slravrm.jobName = args[0];
        Slravrm.jobNumber = args[1];

        String termCode = (String) BatchResourceHolder.getJobParameterMap().get("01");

        Path csv = Paths.get(csvPath,Slravrm.jobName.toLowerCase() + "_" + Slravrm.jobNumber + ".csv");
        Path pdf = Paths.get(pdfPath,Slravrm.jobName.toLowerCase() + "_" + Slravrm.jobNumber + ".pdf");

        try {
            List<HousingAvailableRoom> housingAvailableRooms =
                    housingAvailableRoomsService.getHousingAvailableRooms(termCode);

            housingAvailableRoomsCsv.createCsv(csv, housingAvailableRooms);
            housingAvailableRoomsPdf.createPdf(pdf, housingAvailableRooms);

        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException csvEx) {
            throw new BatchProcessorException("Error creating CSV", csvEx);
        } catch (IOException ioException) {
            throw new BatchProcessorException("Error with file processing.",ioException);
        }

    }
}
