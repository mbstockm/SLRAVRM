package edu.utica.jobsub.slce.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HousingAvailableRoom {

    @CsvIgnore
    private String termCode;

    @CsvBindByName(column = "Term")
    private String termDescription;

    @CsvBindByName(column = "Building Code")
    private String buildingCode;

    @CsvBindByName(column = "Room")
    private String roomCode;

    @CsvBindByName(column = "Room Description")
    private String roomDescription;

    @CsvBindByName(column = "Category")
    private String categoryDescription;

    @CsvBindByName(column = "Room Gender")
    private String roomGender;

    @CsvBindByName(column = "Capacity")
    private Integer capacity;

    @CsvBindByName(column = "Occupancy")
    private Integer occupancy;

    @CsvBindByName(column = "Available")
    private Integer available;

}
