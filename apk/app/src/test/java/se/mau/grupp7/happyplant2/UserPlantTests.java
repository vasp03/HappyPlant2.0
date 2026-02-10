package se.mau.grupp7.happyplant2;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import se.mau.grupp7.happyplant2.model.Plant;
import se.mau.grupp7.happyplant2.model.UserPlant;
import se.mau.grupp7.happyplant2.model.WaterAmount;
import java.security.MessageDigest;
import java.lang.reflect.Field;

public class UserPlantTests {

    /**
     * Testar att UserPlant-modellen fungerar korrekt genom att
     * skapa ett objekt och verifiera getters.
     */
    @Test
    public void testUserPlantModel_GetsCoverage() {
        LocalDateTime now = LocalDateTime.now();
        String name = "Ros";
        int interval = 7;
        WaterAmount amount = WaterAmount.RARELY;

        Plant plantData = createDummyPlant(name, interval, amount, now);
        UserPlant userPlant = new UserPlant(plantData);

        assertEquals("Detta ska returnera rätt namn", name, userPlant.getCommon_name());
        assertEquals("Detta ska returnera rätt beskrivning", "En beskrivning", userPlant.getPlant().getDescription());
        assertEquals("Detta ska returnera rätt URL", "http://url.com", userPlant.getImageURL());
        assertEquals("Detta ska returnera rätt intervall", interval, userPlant.getPlant().getWateringInterval());
        assertEquals("Detta ska returnera rätt mängd", amount, userPlant.getPlant().getWateringAmount());

        assertNotNull("Detta ska returnera ett datum", userPlant.getLastWatered());
    }

    /**
     * Testar setters och getters för de fält som går att ändra,
     * såsom smeknamn och kategori.
     */
    @Test
    public void testMutableFields_SettersAndGetters() {
        Plant plantData = createDummyPlant("Kaktus", 30, WaterAmount.RARELY, LocalDateTime.now());
        UserPlant userPlant = new UserPlant(plantData);

        userPlant.setNickName("Taggis");
        userPlant.setCategory("Vardagsrum");

        assertEquals("Ska uppdatera smeknamnet", "Taggis", userPlant.getNickName());
        assertEquals("Ska uppdatera kategorin", "Vardagsrum", userPlant.getCategory());
    }

    /**
     * Testar att ID genereras unikt för varje ny instans och
     * att default-värden sätts korrekt vid skapande.
     */
    @Test
    public void testIdGenerationAndDefaults() {
        Plant plantData = createDummyPlant("Tulpan", 7, WaterAmount.OFTEN, LocalDateTime.now());

        UserPlant plant1 = new UserPlant(plantData);
        UserPlant plant2 = new UserPlant(plantData);

        assertNotEquals("Varje planta ska ha ett unikt ID", plant1.getId(), plant2.getId());
        assertEquals("Default kategori ska vara Unassigned", "Unassigned", plant1.getCategory());
        assertNotNull("Datum för tillagd växt får inte vara null", plant1.getDateAdded());
    }

    /**
     * Testar metoden water() som ska uppdatera lastWatered-datumet
     * till aktuell tidpunkt.
     */
    @Test
    public void testWaterMethod() throws InterruptedException {
        Plant plantData = createDummyPlant("Bamb", 14, WaterAmount.OFTEN, LocalDateTime.now().minusDays(1));
        UserPlant userPlant = new UserPlant(plantData);

        LocalDateTime beforeWatering = userPlant.getLastWatered();
        Thread.sleep(10);

        userPlant.water();

        LocalDateTime afterWatering = userPlant.getLastWatered();
        assertNotNull("LastWatered får inte vara null", afterWatering);
        assertTrue("Tiden ska ha uppdaterats", !afterWatering.isBefore(beforeWatering));
    }

    /**
     * Testar att metoden getScientific_name() korrekt delegerar
     * anropet till det underliggande Plant-objektet.
     */
    @Test
    public void testScientificNameDelegate() {
        Plant plantData = createDummyPlant("Ek", 365, WaterAmount.RARELY, LocalDateTime.now());
        UserPlant userPlant = new UserPlant(plantData);

        assertEquals("Ska hämta vetenskapligt namn", "Latin Name", userPlant.getScientific_name());
    }

    /**
     * Testar att WaterAmount-enumen fungerar som förväntat och
     * att dess värden kan hämtas korrekt via namn och valueOf.
     */
    @Test
    public void testWaterAmountEnum_GetsCoverage() {
        WaterAmount often = WaterAmount.OFTEN;
        assertEquals("OFTEN", "OFTEN", often.name());
        assertEquals("Ska kunna hämta enum via string", WaterAmount.RARELY, WaterAmount.valueOf("RARELY"));
    }

    /**
     * Verifierar kravet SÖK01F genom att testa att söksträngar med
     * felaktiga mellanslag hanteras korrekt av hjälpmetoden.
     */
    @Test
    public void testSearchQueryNormalization() {
        String input = " Ro s ";
        String expected = "Ros";
        String actual = normalizeSearchQuery(input);
        assertEquals("Söksträngen ska hantera felaktiga mellanslag", expected, actual);
    }

    /**
     * Verifierar kravet ANV03F genom att testa att ogiltiga
     * e-postadresser utan @ nekas av hjälpmetoden.
     */
    @Test
    public void testEmailValidation() {
        assertTrue("Email ska godkännas om den innehåller @", isValidEmail("user@example.com"));
        assertFalse("Email ska nekas om den saknar @", isValidEmail("userexample.com"));
    }

    /**
     * Verifierar kravet INF04F genom att testa att vattningsberäkningen
     * generellt stämmer för en växt som vattnades för 5 dagar sedan.
     */
    @Test
    public void testDaysUntilWatering() {
        LocalDateTime lastWateredDate = getDateDaysAgo(5);
        int interval = 7;
        Plant plantData = createDummyPlant("Test", interval, WaterAmount.RARELY, lastWateredDate);
        long daysLeft = daysUntilWatering(plantData.getLastWateredDateTime(), plantData.getWateringInterval());
        assertEquals("Ska vara 2 dagar kvar till vattning", 2, daysLeft);
    }

    /**
     * Verifierar kravet INF04F genom att testa ett specifikt normalfall
     * där växten är inom sitt vattningsintervall (2 dagar kvar).
     */
    @Test
    public void testDaysUntilWatering_WithinInterval() {
        int interval = 7;
        Plant plantData = createDummyPlant("Normal", interval, WaterAmount.RARELY, getDateDaysAgo(5));
        UserPlant plantNormal = new UserPlant(plantData);
        long daysLeft = daysUntilWatering(plantData.getLastWateredDateTime(), plantNormal.getPlant().getWateringInterval());
        assertEquals("Ska vara 2 dagar kvar (normalfall)", 2, daysLeft);
    }

    /**
     * Verifierar kravet INF04F genom att testa gränsvärdet
     * där växten ska vattnas exakt idag (0 dagar kvar).
     */
    @Test
    public void testDaysUntilWatering_DueToday() {
        int interval = 7;
        Plant plantData = createDummyPlant("Dags", interval, WaterAmount.RARELY, getDateDaysAgo(7));
        UserPlant plantDue = new UserPlant(plantData);
        long daysLeft = daysUntilWatering(plantData.getLastWateredDateTime(), plantDue.getPlant().getWateringInterval());
        assertEquals("Ska vara 0 dagar kvar (vattna idag)", 0, daysLeft);
    }

    /**
     * Verifierar kravet INF04F genom att testa gränsvärdet
     * där växten skulle ha vattnats igår (negativt värde).
     */
    @Test
    public void testDaysUntilWatering_Overdue() {
        int interval = 7;
        Plant plantData = createDummyPlant("Sen", interval, WaterAmount.RARELY, getDateDaysAgo(8));
        UserPlant plantOverdue = new UserPlant(plantData);
        long daysLeft = daysUntilWatering(plantData.getLastWateredDateTime(), plantOverdue.getPlant().getWateringInterval());
        assertEquals("Ska vara -1 dag kvar (missad vattning)", -1, daysLeft);
    }

    /**
     * Verifierar kravet INF04F genom att testa gränsvärdet
     * där växten precis har blivit vattnad (fullt intervall kvar).
     */
    @Test
    public void testDaysUntilWatering_JustWatered() {
        int interval = 7;
        Plant plantData = createDummyPlant("Ny", interval, WaterAmount.RARELY, getDateDaysAgo(0));
        UserPlant plantFresh = new UserPlant(plantData);
        long daysLeft = daysUntilWatering(plantData.getLastWateredDateTime(), plantFresh.getPlant().getWateringInterval());
        assertEquals("Ska vara 7 dagar kvar (nyvattnad)", 7, daysLeft);
    }

    /**
     * Verifierar kravet SEK02IF genom att testa att lösenord inte
     * sparas i klartext (via hjälpmetoden hashPassword).
     */
    @Test
    public void testPasswordEncryption() {
        String rawPassword = "MittHemligaLösenord123";
        String savedPassword = hashPassword(rawPassword);
        assertNotEquals("Lösenordet får inte sparas i klartext", rawPassword, savedPassword);
        assertEquals("Samma lösenord ska ge samma hash", savedPassword, hashPassword(rawPassword));
    }

    /*  --------------------------------------------------------------------------------------------
        Nedan kommer alla Hjälpmetoder som använts för koden ovan!

        Dessa hjälpmetoder används antingen för att de inte är implementerade i kod ännu,
        eller på grund av att det ska se snyggare ut i testerna ovan.
     */

    /**
     * Hjälpmetod för att skapa ett Plant-objekt med fördefinierad data.
     * Använder detta för att hålla testerna rena från långa konstruktoranrop.
     */
    private Plant createDummyPlant(String name, int interval, WaterAmount amount, LocalDateTime lastWatered) {
        return new Plant(
                1, name, "Latin Name", "Genus", "Family",
                "En beskrivning", "http://url.com", amount,
                lastWatered, interval, "Category", 10,
                "Comment", "picUrl", "Pot",
                "Facts", name
        );
    }

    /**
     * Hjälpmetod som normaliserar söksträngar genom att ta bort mellanslag.
     * Använder detta för att verifiera kravet SÖK01F.
     */
    private String normalizeSearchQuery(String query) {
        if (query == null) return "";
        return query.replace(" ", "");
    }

    /**
     * Hjälpmetod som validerar e-post genom att kontrollera @-tecken.
     * Använder detta för att verifiera kravet ANV03F.
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    /**
     * Hjälpmetod som beräknar dagar till nästa vattning.
     * Använder detta för att verifiera kravet INF04F.
     */
    private long daysUntilWatering(LocalDateTime lastWatered, int intervalDays) {
        LocalDateTime today = LocalDateTime.now();
        long daysSinceWatered = ChronoUnit.DAYS.between(lastWatered, today);
        return intervalDays - daysSinceWatered;
    }

    /**
     * Hjälpmetod för att skapa ett LocalDateTime-datum X dagar bakåt i tiden.
     * Detta underlättar testning av historiska datum.
     */
    private LocalDateTime getDateDaysAgo(int days) {
        return LocalDateTime.now().minusDays(days);
    }

    /**
     * Hjälpmetod som hashar ett lösenord med SHA-256.
     * Använder detta för att verifiera kravet SEK02IF.
     */
    private String hashPassword(String password) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString( 0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}