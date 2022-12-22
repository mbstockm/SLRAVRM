package edu.utica.slce.jobsub.dao;

import edu.utica.slce.jobsub.model.HousingAvailableRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HousingAvailableRoomsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    HousingAvailableRoomsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Using Spring JdbcTemplate query the Banner database to return List of HousingAvailableRoom.
     * @param term
     * @return
     */
    public List<HousingAvailableRoom> findHousingAvailableRooms(String term) {
        List<HousingAvailableRoom> list =
                jdbcTemplate.query("""
      select term.code term_code 
            ,term.description term_description 
            ,rd.slbrdef_bldg_code building_code 
            ,rd.slbrdef_room_number room_code 
            ,rd.slbrdef_desc room_description 
            ,slrbcat_desc category_description 
            ,nvl(rd.slbrdef_sex,'N/A') room_gender 
            ,rd.slbrdef_capacity capacity 
            ,count(decode(stvascd_count_in_usage,'Y',ra.slrrasg_pidm,null)) occupancy 
            ,rd.slbrdef_capacity - count(decode(stvascd_count_in_usage,'Y',ra.slrrasg_pidm,null)) available 
        from slbrdef rd, slrbcat, slrrasg ra, stvascd, 
             (select stvterm_code code, stvterm_desc description 
                from stvterm) term 
       where term.code = ? 
         and rd.slbrdef_term_code_eff = (select max(rd2.slbrdef_term_code_eff) from slbrdef rd2 
                                          where rd2.slbrdef_bldg_code = rd.slbrdef_bldg_code 
                                            and rd2.slbrdef_room_number = rd.slbrdef_room_number 
                                            and rd2.slbrdef_term_code_eff <= term.code) 
         and rd.slbrdef_room_number <> 'UA' 
         and rd.slbrdef_rmst_code = 'AC' 
         and rd.slbrdef_room_type = 'D' 
         and rd.slbrdef_bldg_code = slrbcat_bldg_code(+) 
         and rd.slbrdef_bcat_code = slrbcat_code(+) 
         and term.code = ra.slrrasg_term_code(+) 
         and rd.slbrdef_bldg_code = ra.slrrasg_bldg_code(+) 
         and rd.slbrdef_room_number = ra.slrrasg_room_number(+) 
         and ra.slrrasg_ascd_code = stvascd_code(+) 
      group by term.code 
              ,term.description 
              ,rd.slbrdef_bldg_code 
              ,rd.slbrdef_room_number 
              ,rd.slbrdef_desc 
              ,rd.slbrdef_sex 
              ,rd.slbrdef_capacity 
              ,slrbcat_desc 
      having rd.slbrdef_capacity > count(decode(stvascd_count_in_usage,'Y',ra.slrrasg_pidm,null)) 
      order by building_code,room_code
""",
                        new BeanPropertyRowMapper(HousingAvailableRoom.class),
                        new Object[]{term}
                );
        return list;
    }


}
