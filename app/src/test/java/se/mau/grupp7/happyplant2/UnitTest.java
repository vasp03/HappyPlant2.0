package se.mau.grupp7.happyplant2;

import org.junit.Test;
import static org.junit.Assert.*;

import kotlin.Unit;
import se.mau.grupp7.happyplant2.model.PlantDetails;
import se.mau.grupp7.happyplant2.model.PlantType;
import se.mau.grupp7.happyplant2.viewmodel.PlantViewModel;

public class UnitTest {

   @Test
   public void plantTypeTest() {
       String name = "Rose";
       String description = "Rose is a plant.";
       String url = "http://url.url";

       PlantType plantData = new PlantType(name, description, url);

       assertEquals("Name: ", name, plantData.getName());
       assertEquals("Description: ", description, plantData.getDescription());
       assertEquals("Image URL: ", url, plantData.getImageURL());
   }

   @Test
   public void plantDetailsTest() {
       int id = 1;
       String name = "Rose";
       String description = "Rose is a plant.";
       String genus = "Genus";
       String url = "http://url.url";

       PlantDetails plantDetails = new PlantDetails(id, name, description, genus, url);

       assertEquals("ID: ", id, plantDetails.getId());
       assertEquals("Name: ", name, plantDetails.getCommon_name());
       assertEquals("Description: ", description, plantDetails.getScientific_name());
       assertEquals("Genus: ", genus, plantDetails.getGenus());
       assertEquals("Image URL: ", url, plantDetails.getImageUrl());
   }
}