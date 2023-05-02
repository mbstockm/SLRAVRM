package edu.utica.jobsub.slce.csv;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import edu.utica.jobsub.slce.model.HousingAvailableRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class HousingAvailableRoomsCsv {

    private ResourceLoader resourceLoader;

    @Autowired
    HousingAvailableRoomsCsv(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * For the path and list of beans passed in create a CSV file using open CSV library
     * @param csv
     * @param list
     * @throws Exception
     */
    public void createCsv(Path csv, List<HousingAvailableRoom> list)  throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try (Writer writer = Files.newBufferedWriter(csv)) {
            StatefulBeanToCsv<HousingAvailableRoom> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withMappingStrategy(headerTemplateStrategy())
                    .build();
            beanToCsv.write(list);
        }
    }

    /**
     * Using csv header template create a mapping strategy and read the header template file to capture the header
     * If the columns in the report need to change the bean annotations as well as the HeaderTemplate.csv would need to be modified
     * @return
     * @throws Exception
     */
    public HeaderColumnNameMappingStrategy headerTemplateStrategy() throws IOException {

        HeaderColumnNameMappingStrategy strategy =
                new HeaderColumnNameMappingStrategyBuilder().build();
        strategy.setType(HousingAvailableRoom.class);

        try (Reader reader = new BufferedReader(new InputStreamReader(csvHeaderTemplate().getInputStream()))) {
            CsvToBean<HousingAvailableRoom> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(HousingAvailableRoom.class)
                    .withMappingStrategy(strategy)
                    .build();
            csvToBean.parse();
        }
        return strategy;
    }

    /**
     * Get csv header template resource from classpath
     * @return
     */
    public Resource csvHeaderTemplate() {
        return resourceLoader.getResource("classpath:/csv/HousingAvailableRoomsCsvHeaderTemplate.csv");
    }




}
