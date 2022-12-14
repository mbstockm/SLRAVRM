package edu.utica.slce.jobsub;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sct.messaging.bif.BatchProcessorException;
import com.sct.messaging.bif.BatchResourceHolder;
import com.sct.messaging.bif.banner.BannerBatchProcessor;
import edu.utica.slce.jobsub.csv.HousingAvailableRoomsCsv;
import edu.utica.slce.jobsub.model.HousingAvailableRoom;
import edu.utica.slce.jobsub.pdf.HousingAvailableRoomsPdf;
import edu.utica.slce.jobsub.service.HousingAvailableRoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    @Value("${csv.output.path}")
    private String csvPath;
    @Value("${pdf.output.path}")
    private String pdfPath;

    @Override
    public void processJob() {
        SpringApplication.run(Slravrm.class,new String[]{getJobName(),getJobNumber()});
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
