package DBAcess;
import database.DBConnection;
import model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * This class is used to manage the countries of the database
 */
public class DBCountries {
    /**
     * Retrieves all countries from the database
     * @return Observable list of country objects
     */
    public static ObservableList<Country> getAllCountries(){
        ObservableList<Country> cList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country c = new Country(countryId, countryName);
                cList.add(c);
            }

        }catch (SQLException throwable){
            throwable.printStackTrace();
        }

        return cList;
    }

    /**
     * Retrieves country by selected country ID
     * @param id selected country ID
     * @return given country with the selected country ID
     */
    public static Country getCountryById(int id) {
        Country country = new Country();
        try {
            String sqlGet = "SELECT * FROM countries " +
                    "WHERE Country_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                country = new Country(countryId, countryName);
            }


        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return country;
    }

}
