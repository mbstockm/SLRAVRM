package edu.utica.jobsub.slce.service;

import edu.utica.jobsub.slce.model.HousingAvailableRoom;
import edu.utica.jobsub.slce.dao.HousingAvailableRoomsDao;
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
