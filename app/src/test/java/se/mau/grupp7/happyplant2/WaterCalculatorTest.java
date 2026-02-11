package se.mau.grupp7.happyplant2;

import org.junit.Test;
import static org.junit.Assert.*;

import se.mau.grupp7.happyplant2.oldfiles.WaterCalculator;

/**
 * Tester för WaterCalculator. BVM är implementerad för gränserna.
 */

public class WaterCalculatorTest {

    private final long WEEK = 7L * 24 * 60 * 60 * 1000;

    @Test
    public void testCalculateWaterFrequency_Boundary200() {
        assertEquals(WEEK * 4, WaterCalculator.calculateWaterFrequencyForWatering(199));

        assertEquals(WEEK * 4, WaterCalculator.calculateWaterFrequencyForWatering(200));

        assertEquals(WEEK * 3, WaterCalculator.calculateWaterFrequencyForWatering(201));
    }


    @Test
    public void testCalculateWaterFrequency_Boundary400() {
        assertEquals(WEEK * 3, WaterCalculator.calculateWaterFrequencyForWatering(399));

        assertEquals(WEEK * 3, WaterCalculator.calculateWaterFrequencyForWatering(400));

        assertEquals(WEEK * 2, WaterCalculator.calculateWaterFrequencyForWatering(401));
    }

    @Test
    public void testCalculateWaterFrequency_Boundary600() {
        assertEquals(WEEK * 2, WaterCalculator.calculateWaterFrequencyForWatering(599));

        assertEquals(WEEK * 2, WaterCalculator.calculateWaterFrequencyForWatering(600));

        assertEquals(WEEK, WaterCalculator.calculateWaterFrequencyForWatering(601));
    }

    @Test
    public void testCalculateWaterFrequency_Boundary800() {
        assertEquals(WEEK, WaterCalculator.calculateWaterFrequencyForWatering(799));

        assertEquals(WEEK, WaterCalculator.calculateWaterFrequencyForWatering(800));

        assertEquals(WEEK / 2, WaterCalculator.calculateWaterFrequencyForWatering(801));
    }

    @Test
    public void testCalculateWaterFrequency_Zero() {
        assertEquals(WEEK * 4, WaterCalculator.calculateWaterFrequencyForWatering(0));
    }
}