package edu.utica.slce.jobsub.service;

import edu.utica.slce.jobsub.dao.HousingAvailableRoomsDao;
import edu.utica.slce.jobsub.model.HousingAvailableRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HousingAvailableRoomsService {

    private HousingAvailableRoomsDao housingAvailableRoomsDao;

    @Autowired
    HousingAvailableRoomsService(HousingAvailableRoomsDao housingAvailableRoomsDao) {
        this.housingAvailableRoomsDao = housingAvailableRoomsDao;
    }

    public List<HousingAvailableRoom> getHousingAvailableRooms(String term) {
        return housingAvailableRoomsDao.findHousingAvailableRooms(term);
    }

}
