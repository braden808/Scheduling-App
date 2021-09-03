package DBAcess;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;
import model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This class is used to manage the first level divisions of the database
 */
public class DBFirstLevelDivision {

    /**
     * Retrieves all first level divisions from the database
     * @return Observable list of first level division objects
     */
    public static ObservableList<FirstLevelDivision> getAllDivisions(){
        ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();
        try {
            String sqlGet = "SELECT * FROM first_level_divisions, countries WHERE first_level_divisions.COUNTRY_ID = countries.Country_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int divisionId = rs.getInt("Division_Id");
                String divisionName = rs.getString("Division");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                LocalDateTime createDateFinal = createDate.toLocalDateTime();
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdateFinal = lastUpdate.toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryIdFinal = rs.getInt("Country_ID");

                FirstLevelDivision fLD = new FirstLevelDivision();
                fLD.setDivisionId(divisionId);
                fLD.setDivisionName(divisionName);
                fLD.setCreateDate(createDateFinal);
                fLD.setLastUpdate(lastUpdateFinal);
                fLD.setLastUpdatedBy(lastUpdatedBy);
                fLD.setCountryId(countryIdFinal);
                divisionList.add(fLD);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return divisionList;
    }

    public static void insertDivision(FirstLevelDivision fLD){
            try {
                String sqlIn = "INSERT INTO first_level_divisions VALUES(NULL,?, NOW(), NOW(), ?, ?)";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlIn);

                ps.setString(1,fLD.getDivisionName());
                ps.setString(2,fLD.getLastUpdatedBy());
                ps.setInt(3,fLD.getCountryId());
                ps.execute();

            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
    }


    /**
     * Retrieves all first level divisions from the database with the given country ID
     * @param countryId
     * @return Observable list of first level division objects
     */
    public static ObservableList<FirstLevelDivision> getByCountryId(int countryId){
        ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();
        try {
            String sqlGet = "SELECT * FROM first_level_divisions WHERE first_level_divisions.COUNTRY_ID = " + countryId;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int divisionId = rs.getInt("Division_Id");
                String divisionName = rs.getString("Division");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                LocalDateTime createDateFinal = createDate.toLocalDateTime();
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdateFinal = lastUpdate.toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryIdFinal = rs.getInt("Country_ID");

                FirstLevelDivision fLD = new FirstLevelDivision();
                fLD.setDivisionId(divisionId);
                fLD.setDivisionName(divisionName);
                fLD.setCreateDate(createDateFinal);
                fLD.setLastUpdate(lastUpdateFinal);
                fLD.setLastUpdatedBy(lastUpdatedBy);
                fLD.setCountryId(countryIdFinal);
                divisionList.add(fLD);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return divisionList;
    }

    /**
     * Retrieves first level division from division ID
     * @param id first level division ID
     * @return first level division
     */
    public static FirstLevelDivision getDivisionById(int id) {
        FirstLevelDivision fLD = new FirstLevelDivision();
        try {
            String sqlGet = "SELECT * FROM first_level_divisions " +
                    "WHERE Division_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int divisionId = rs.getInt("Division_Id");
                String divisionName = rs.getString("Division");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                LocalDateTime createDateFinal = createDate.toLocalDateTime();
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdateFinal = lastUpdate.toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int countryIdFinal = rs.getInt("Country_ID");

                fLD.setDivisionId(divisionId);
                fLD.setDivisionName(divisionName);
                fLD.setCreateDate(createDateFinal);
                fLD.setLastUpdate(lastUpdateFinal);
                fLD.setLastUpdatedBy(lastUpdatedBy);
                fLD.setCountryId(countryIdFinal);
            }


        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return fLD;
    }

}
