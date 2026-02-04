package com.server.controller;

import java.lang.Thread.State;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

import com.server.model.FlowerTypes;

public class CacheDatabase {
    private Controller controller;
    private Connection connection;

    public CacheDatabase(Controller controller) {
        this.controller = controller;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:cache.db");
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error connecting to com.moviefinder.database");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(500);
        }
    }

    private boolean createTables() {
        String createSearchQueryTableSQL = "CREATE TABLE IF NOT EXISTS search_query (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "query TEXT UNIQUE," +
                "timestamp INTEGER" +
                ");";

        try (PreparedStatement stmt = connection.prepareStatement(createSearchQueryTableSQL)) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Error creating search_query table");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }

        String createConnectionSearchQueryFlowerTypesTableSQL = "CREATE TABLE IF NOT EXISTS connection_search_query_flower_types ("
                +
                "search_query_id INTEGER," +
                "flower_type_id INTEGER," +
                "FOREIGN KEY(search_query_id) REFERENCES search_query(id)," +
                "FOREIGN KEY(flower_type_id) REFERENCES flower_types(id)" +
                ");";

        try (PreparedStatement stmt = connection
                .prepareStatement(createConnectionSearchQueryFlowerTypesTableSQL)) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Error creating connection_search_query_flower_types table");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }

        String createFlowerTypesTableSQL = "CREATE TABLE IF NOT EXISTS flower_types (" +
                "id INTEGER PRIMARY KEY," +
                "common_name TEXT," +
                "scientific_name TEXT[]," +    // store arrays as JSON/text
                "other_names TEXT[]," +        // store arrays as JSON/text
                "family TEXT," +
                "hybrid TEXT," +
                "authority TEXT," +
                "subspecies TEXT," +
                "cultivar TEXT," +
                "variety TEXT," +
                "species_epithet TEXT," +
                "genus TEXT," +
                "original_url TEXT," +
                "regular_url TEXT," +
                "medium_url TEXT," +
                "small_url TEXT," +
                "thumbnail TEXT" +
                ");";

        try (PreparedStatement stmt = connection.prepareStatement(createFlowerTypesTableSQL)) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Error creating flower_types table");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public ArrayList<FlowerTypes> getFlowerTypesSearchResult(String searchQuery) {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Database connection is not established.");
                return null;
            }

            if (searchQuery == null || searchQuery.isBlank()) {
                System.out.println("Search query is null or blank.");
                return null;
            }

            ArrayList<FlowerTypes> flowerTypes = new ArrayList<>();

            String getFlowerTypesForSearchQuerySQL = "SELECT ft.* FROM flower_types ft " +
                    "JOIN connection_search_query_flower_types csqft ON ft.id = csqft.flower_type_id " +
                    "JOIN search_query sq ON csqft.search_query_id = sq.id " +
                    "WHERE sq.query = ?;";
            PreparedStatement ps = connection.prepareStatement(getFlowerTypesForSearchQuerySQL);
            ps.setString(1, searchQuery);
            ResultSet flowerTypesRS = ps.executeQuery();

            while (flowerTypesRS.next()) {
                System.out.println("Found cached flower type for query '" + searchQuery + "': "
                        + flowerTypesRS.getString("common_name"));
                FlowerTypes flowerType = new FlowerTypes(
                        flowerTypesRS.getInt("id"),
                        flowerTypesRS.getString("common_name"),
                        (String[]) flowerTypesRS.getArray("scientific_name").getArray(),
                        (String[]) flowerTypesRS.getArray("other_names").getArray(),
                        flowerTypesRS.getString("family"),
                        flowerTypesRS.getString("hybrid"),
                        flowerTypesRS.getString("authority"),
                        flowerTypesRS.getString("subspecies"),
                        flowerTypesRS.getString("cultivar"),
                        flowerTypesRS.getString("variety"),
                        flowerTypesRS.getString("species_epithet"),
                        flowerTypesRS.getString("genus"),
                        flowerTypesRS.getString("original_url"),
                        flowerTypesRS.getString("regular_url"),
                        flowerTypesRS.getString("medium_url"),
                        flowerTypesRS.getString("small_url"),
                        flowerTypesRS.getString("thumbnail"));

                flowerTypes.add(flowerType);
            }

            return flowerTypes;
        } catch (SQLException e) {
            System.out
                    .println("Error fetching flower types for search query '" + searchQuery + "' from database");
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public boolean addFlowerTypeSearchResult(String searchQuery, ArrayList<FlowerTypes> flowerType) {
        System.out.println("Caching flower types for search query: " + searchQuery);
        for (FlowerTypes ft : flowerType) {
            System.out.println(" - " + ft.getCommon_name());
        }

        String insertSearchQuerySQL = "INSERT OR IGNORE INTO search_query (query, timestamp) VALUES (?, ?);";
        String insertFlowerTypeSQL = "INSERT OR IGNORE INTO flower_types (id, common_name, scientific_name, other_names, family, hybrid, authority, subspecies, cultivar, variety, species_epithet, genus, original_url, regular_url, medium_url, small_url, thumbnail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String insertConnectionSQL = "INSERT INTO connection_search_query_flower_types (search_query_id, flower_type_id) VALUES ((SELECT id FROM search_query WHERE query = ?), ?);";

        try {
            connection.setAutoCommit(false);

            PreparedStatement psInsertSearchQuery = connection.prepareStatement(insertSearchQuerySQL);
            psInsertSearchQuery.setString(1, searchQuery);
            psInsertSearchQuery.setLong(2, System.currentTimeMillis());
            psInsertSearchQuery.executeUpdate();

            for (FlowerTypes ft : flowerType) {
                PreparedStatement psInsertFlowerType = connection.prepareStatement(insertFlowerTypeSQL);
                psInsertFlowerType.setInt(1, ft.getId());
                psInsertFlowerType.setString(2, ft.getCommon_name());
                psInsertFlowerType.setArray(3, connection.createArrayOf("TEXT", ft.getScientific_name()));
                psInsertFlowerType.setArray(4, connection.createArrayOf("TEXT", ft.getOther_name()));
                psInsertFlowerType.setString(5, ft.getFamily());
                psInsertFlowerType.setString(6, ft.getHybrid());
                psInsertFlowerType.setString(7, ft.getAuthority());
                psInsertFlowerType.setString(8, ft.getSubspecies());
                psInsertFlowerType.setString(9, ft.getCultivar());
                psInsertFlowerType.setString(10, ft.getVariety());
                psInsertFlowerType.setString(11, ft.getSpecies_epithet());
                psInsertFlowerType.setString(12, ft.getGenus());
                psInsertFlowerType.setString(13, ft.getOriginal_url());
                psInsertFlowerType.setString(14, ft.getRegular_url());
                psInsertFlowerType.setString(15, ft.getMedium_url());
                psInsertFlowerType.setString(16, ft.getSmall_url());
                psInsertFlowerType.setString(17, ft.getThumbnail());
                psInsertFlowerType.executeUpdate();

                PreparedStatement psInsertConnection = connection.prepareStatement(insertConnectionSQL);
                psInsertConnection.setString(1, searchQuery);
                psInsertConnection.setInt(2, ft.getId());
                psInsertConnection.executeUpdate();
            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.out.println("Error caching flower types for search query '" + searchQuery + "'");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
