package se.mau.grupp7.happyplant2;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import se.mau.grupp7.happyplant2.oldfiles.Plant;
import se.mau.grupp7.happyplant2.model.UserPlant;
import se.mau.grupp7.happyplant2.model.WaterAmount;

public class UserPlantTests {

    /**
     * Testar att UserPlant-modellen fungerar korrekt genom att
     * skapa ett objekt och verifiera getters.
     */
//    @Test
//    public void testUserPlantModel_GetsCoverage() {
//        LocalDateTime now = LocalDateTime.now();
//        String name = "Ros";
//        int interval = 7;
//        WaterAmount amount = WaterAmount.RARELY;
//
//        Plant plantData = createDummyPlant(name, interval, amount, now);
//        UserPlant userPlant = new UserPlant(plantData);
//
//        assertEquals("Detta ska returnera rätt namn", name, userPlant.getCommon_name());
//        assertEquals("Detta ska returnera rätt beskrivning", "En beskrivning", userPlant.getPlant().getDescription());
//        assertEquals("Detta ska returnera rätt URL", "http://url.com", userPlant.getImageURL());
//        assertEquals("Detta ska returnera rätt intervall", interval, userPlant.getPlant().getWateringInterval());
//        assertEquals("Detta ska returnera rätt mängd", amount, userPlant.getPlant().getWateringAmount());
//
//        assertNotNull("Detta ska returnera ett datum", userPlant.getLastWatered());
//    }

    /**
     * Testar setters och getters för de fält som går att ändra,
     * såsom smeknamn och kategori.
     */
    //@Test
//    public void testMutableFields_SettersAndGetters() {
//        Plant plantData = createDummyPlant("Kaktus", 30, WaterAmount.RARELY, LocalDateTime.now());
//        UserPlant userPlant = new UserPlant(plantData);
//
//        userPlant.setNickName("Taggis");
//        userPlant.setCategory("Vardagsrum");
//
//        assertEquals("Ska uppdatera smeknamnet", "Taggis", userPlant.getNickName());
//        assertEquals("Ska uppdatera kategorin", "Vardagsrum", userPlant.getCategory());
//    }

    /**
     * Testar att ID genereras unikt för varje ny instans och
     * att default-värden sätts korrekt vid skapande.
     */
//    @Test
//    public void testIdGenerationAndDefaults() {
//        Plant plantData = createDummyPlant("Tulpan", 7, WaterAmount.OFTEN, LocalDateTime.now());
//
//        UserPlant plant1 = new UserPlant(plantData);
//        UserPlant plant2 = new UserPlant(plantData);
//
//        assertNotEquals("Varje planta ska ha ett unikt ID", plant1.getId(), plant2.getId());
//        assertEquals("Default kategori ska vara Unassigned", "Unassigned", plant1.getCategory());
//        assertNotNull("Datum för tillagd växt får inte vara null", plant1.getDateAdded());
//    }

    /**
     * Testar metoden water() som ska uppdatera lastWatered-datumet
     * till aktuell tidpunkt.
     */
    //   @Test
//    public void testWaterMethod() {
//        Plant plantData = createDummyPlant("Bamb", 14, WaterAmount.OFTEN, LocalDateTime.now().minusDays(1));
//        UserPlant userPlant = new UserPlant(plantData);
//
//        LocalDateTime beforeWatering = userPlant.getLastWatered();
//
//        userPlant.water();
//
//        LocalDateTime afterWatering = userPlant.getLastWatered();
//        assertNotNull("LastWatered får inte vara null", afterWatering);
//        assertTrue("Tiden ska ha uppdaterats", !afterWatering.isBefore(beforeWatering));
//    }

    /**
     * Testar att metoden getScientific_name() korrekt delegerar
     * anropet till det underliggande Plant-objektet.
     */
//    @Test
//    public void testScientificNameDelegate() {
//        Plant plantData = createDummyPlant("Ek", 365, WaterAmount.RARELY, LocalDateTime.now());
//        UserPlant userPlant = new UserPlant(plantData);
//
//        assertEquals("Ska hämta vetenskapligt namn", "Latin Name", userPlant.getScientific_name());
//    }

    /**
     * Testar att WaterAmount-enumen fungerar som förväntat och
     * att dess värden kan hämtas korrekt via namn och valueOf.
     */
//    @Test
//    public void testWaterAmountEnum_GetsCoverage() {
//        WaterAmount often = WaterAmount.OFTEN;
//        assertEquals("OFTEN", "OFTEN", often.name());
//        assertEquals("Ska kunna hämta enum via string", WaterAmount.RARELY, WaterAmount.valueOf("RARELY"));
//    }

    /**
     * Testar att metoden getLastWateredString() returnerar datumet
     * i rätt format (YYYY-M-D H:mm).
     */
//    @Test
//    public void testGetLastWateredString_Formatting() {
//        Plant plantData = createDummyPlant("Test", 7, WaterAmount.RARELY, LocalDateTime.now());
//        UserPlant userPlant = new UserPlant(plantData);
//
//        LocalDateTime ldt = userPlant.getLastWatered();
//
//        String expectedMinute = ldt.getMinute() < 10 ? "0" + ldt.getMinute() : String.valueOf(ldt.getMinute());
//        String expected = ldt.getYear() + "-" +
//                ldt.getMonthValue() + "-" +
//                ldt.getDayOfMonth() + " " +
//                ldt.getHour() + ":" +
//                expectedMinute;
//
//        assertEquals("Datumsträngen ska vara korrekt formaterad", expected, userPlant.getLastWateredString());
//    }

    /*  --------------------------------------------------------------------------------------------
        Nedan kommer Hjälpmetod som använts för kod ovan!
        Detta hjälpmetod används för att det ska se snyggare ut i testerna ovan.
     */

    /**
     * Hjälpmetod för att skapa ett Plant-objekt med fördefinierad data.
     * Använder detta för att hålla testerna rena från långa konstruktoranrop.
     */
//    private Plant createDummyPlant(String name, int interval, WaterAmount amount, LocalDateTime lastWatered) {
//        return new Plant(
//                1, name, "Latin Name", "Genus", "Family",
//                "En beskrivning", "http://url.com", amount,
//                lastWatered, interval, "Category", 10,
//                "Comment", "picUrl", "Pot",
//                "Facts", name
//        );
//    }
}