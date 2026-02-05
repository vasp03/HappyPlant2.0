package se.mau.grupp7.happyplant2;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.security.MessageDigest;

import se.mau.grupp7.happyplant2.model.UserPlant;
import se.mau.grupp7.happyplant2.model.WaterAmount;

public class RequirementTests {

    /**
     * Testar att UserPlant-modellen fungerar korrekt genom att
     * skapa ett objekt och verifiera getters.
     */
    @Test
    public void testUserPlantModel_GetsCoverage() {
        Date now = new Date();
        String name = "Ros";
        String desc = "En vacker ros";
        String url = "https://www.cool-mania.net/mini/w-980/data/product/58/89dc303b85740da3404fab3ec3cfa1.jpg";
        int interval = 7;
        WaterAmount amount = WaterAmount.RARELY;

        UserPlant plant = new UserPlant(name, desc, url, interval, amount, now);

        assertEquals("Detta ska returnera rätt namn", name, plant.getName());
        assertEquals("Detta ska returnera rätt beskrivning", desc, plant.getDescription());
        assertEquals("Detta ska returnera rätt URL", url, plant.getImageURL());
        assertEquals("Detta ska returnera rätt intervall", interval, plant.getWateringInterval());
        assertEquals("Detta ska returnera rätt mängd", amount, plant.getWateringAmount());
        assertEquals("Detta ska returnera rätt datum", now, plant.getLastTimeWatered());
    }

    /**
     * Testar att WaterAmount-enumen fungerar som förväntat och
     * att dess värden kan hämtas korrekt.
     */
    @Test
    public void testWaterAmountEnum_GetsCoverage() {
        WaterAmount often = WaterAmount.OFTEN;
        assertEquals("OFTEN", "OFTEN", often.name());
        assertEquals("Ska kunna hämta enum via string", WaterAmount.RARELY, WaterAmount.valueOf("RARELY"));
    }

    /**
     * Verifierar kravet SÖK01F genom att testa att söksträngar med
     * felaktiga mellanslag hanteras korrekt. Detta är INTE
     * implementerat och därav har vi metoden normalizeSearchQuery
     * nedan till hjälp!
     */
    @Test
    public void testSearchQueryNormalization() {
        String input = " Ro s ";
        String expected = "Ros";
        String actual = normalizeSearchQuery(input);

        assertEquals("Söksträngen ska hantera felaktiga mellanslag", expected, actual);
    }

    /**
     * Gör normalisering av söksträngar genom att ta bort mellanslag som
     * tillkommer före, efter och i ordet. Detta ska bort! Har skapats
     * för att kunna se att testerna är korrekt skapade!
     */
    private String normalizeSearchQuery(String query) {
        if (query == null) return "";
        return query.replace(" ", "");

    }

    /**
     * Verifierar kravet ANV03F genom att testa att ogiltiga
     * e-postadresser utan @ nekas. Detta är INTE
     * implementerat och därav har vi metoden isValidEmail nedan till hjälp!
     */
    @Test
    public void testEmailValidation() {
        assertTrue("Email ska godkännas om den innehåller @", isValidEmail("user@example.com"));
        assertFalse("Email ska nekas om den saknar @", isValidEmail("userexample.com"));
    }

    /**
     * Simulerar validering av e-post genom att kontrollera om
     * strängen innehåller ett @-tecken. Detta ska bort! Har skapats
     * för att kunna se att testerna är korrekt skapade!
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }


    /**
     * Verifierar kravet INF04F genom att testa att vattningsberäkningen
     * stämmer för en växt som vattnades för 5 dagar sedan. Detta är INTE
     * implementerat och därav har vi metoden daysUntilWatering nedan till hjälp!
     */
    @Test
    public void testDaysUntilWatering() {
        long fiveDaysInMillis = 5L * 24 * 60 * 60 * 1000;
        Date lastWateredDate = new Date(System.currentTimeMillis() - fiveDaysInMillis);

        UserPlant plant = new UserPlant("Test", "Desc", "URL", 7, WaterAmount.RARELY, lastWateredDate);

        long daysLeft = daysUntilWatering(plant.getLastTimeWatered(), plant.getWateringInterval());

        assertEquals("Ska vara 2 dagar kvar till vattning", 2, daysLeft);
    }

    /**
     * Beräknar antalet dagar som är kvar till nästa vattning
     * baserat på senaste vattningsdatum och intervall.
     * Detta ska bort! Har skapats för att kunna
     * se att testerna är korrekt skapade!
     */
    private long daysUntilWatering(Date lastWatered, int intervalDays) {
        Date today = new Date();
        long diffInMillies = today.getTime() - lastWatered.getTime();
        long daysSinceWatered = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return intervalDays - daysSinceWatered;
    }
    /**
     * Verifierar kravet INF04F genom att testa ett normalfall
     * där växten är inom sitt vattningsintervall. Detta är INTE
     * implementerat och därav har vi metoden getDateDaysAgo
     * nedan till hjälp!
     */
    @Test
    public void testDaysUntilWatering_WithinInterval() {
        int interval = 7;
        UserPlant plantNormal = new UserPlant("Normal", "", "", interval, WaterAmount.RARELY, getDateDaysAgo(5));

        long daysLeft = daysUntilWatering(plantNormal.getLastTimeWatered(), plantNormal.getWateringInterval());

        assertEquals("Ska vara 2 dagar kvar (normalfall)", 2, daysLeft);
    }

    /**
     * Verifierar kravet INF04F genom att testa gränsvärdet
     * där växten ska vattnas exakt idag. Detta är INTE
     * implementerat och därav har vi metoden getDateDaysAgo
     * nedan till hjälp!
     */
    @Test
    public void testDaysUntilWatering_DueToday() {
        int interval = 7;
        UserPlant plantDue = new UserPlant("Dags", "", "", interval, WaterAmount.RARELY, getDateDaysAgo(7));

        long daysLeft = daysUntilWatering(plantDue.getLastTimeWatered(), plantDue.getWateringInterval());

        assertEquals("Ska vara 0 dagar kvar (vattna idag)", 0, daysLeft);
    }

    /**
     * Verifierar kravet INF04F genom att testa gränsvärdet
     * där växten skulle ha vattnats igår (negativt värde).
     * Detta är INTE implementerat och därav har vi metoden
     * getDateDaysAgo nedan till hjälp!
     */
    @Test
    public void testDaysUntilWatering_Overdue() {
        int interval = 7;
        UserPlant plantOverdue = new UserPlant("Sen", "", "", interval, WaterAmount.RARELY, getDateDaysAgo(8));

        long daysLeft = daysUntilWatering(plantOverdue.getLastTimeWatered(), plantOverdue.getWateringInterval());

        assertEquals("Ska vara -1 dag kvar (missad vattning)", -1, daysLeft);
    }

    /**
     * Verifierar kravet INF04F genom att testa gränsvärdet
     * där växten precis har blivit vattnad. Detta är INTE
     * implementerat och därav har vi metoden getDateDaysAgo
     * nedan till hjälp!
     */
    @Test
    public void testDaysUntilWatering_JustWatered() {
        int interval = 7;
        UserPlant plantFresh = new UserPlant("Ny", "", "", interval, WaterAmount.RARELY, getDateDaysAgo(0));

        long daysLeft = daysUntilWatering(plantFresh.getLastTimeWatered(), plantFresh.getWateringInterval());

        assertEquals("Ska vara 7 dagar kvar (nyvattnad)", 7, daysLeft);
    }

    /**
     * Hjälpmetod för att skapa ett datum X dagar bakåt i tiden.
     * Detta ska bort! Har skapats för att kunna se att testerna
     * är korrekt skapade!
     */
    private Date getDateDaysAgo(int days) {
        long daysInMillis = days * 24L * 60 * 60 * 1000;
        return new Date(System.currentTimeMillis() - daysInMillis);
    }


    /**
     * Verifierar kravet SEK02IF genom att testa att lösenord inte
     * sparas i klartext. Detta är INTE implementerat och därav
     * har vi metoden hashPassword nedan till hjälp!
     */
    @Test
    public void testPasswordEncryption() {
        String rawPassword = "MittHemligaLösenord123";
        String savedPassword = hashPassword(rawPassword);

        assertNotEquals("Lösenordet får inte sparas i klartext", rawPassword, savedPassword);
        assertEquals("Samma lösenord ska ge samma hash", savedPassword, hashPassword(rawPassword));
    }

    /**
     * Simulerar lösenordskryptering genom att hasha strängen med SHA-256.
     * Har INTE tillämpat salting! Detta ska bort! Har skapats för att kunna
     * se att testerna är korrekt skapade!
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