package com.server.model;

public class FlowerTypes {
    private int id;
    private String common_name;
    private String[] scientific_name;
    private String[] other_name; 
    private String family;
    private String hybrid;
    private String authority;
    private String subspecies;
    private String cultivar;
    private String variety;
    private String species_epithet;
    private String genus;
    private String original_url;
    private String regular_url;
    private String medium_url;
    private String small_url;
    private String thumbnail;
    
    public FlowerTypes(int id, String common_name, String[] scientific_name, String[] other_name, String family,
            String hybrid, String authority, String subspecies, String cultivar, String variety, String species_epithet,
            String genus, String original_url, String regular_url, String medium_url, String small_url,
            String thumbnail) {
        this.id = id;
        this.common_name = common_name;
        this.scientific_name = scientific_name;
        this.other_name = other_name;
        this.family = family;
        this.hybrid = hybrid;
        this.authority = authority;
        this.subspecies = subspecies;
        this.cultivar = cultivar;
        this.variety = variety;
        this.species_epithet = species_epithet;
        this.genus = genus;
        this.original_url = original_url;
        this.regular_url = regular_url;
        this.medium_url = medium_url;
        this.small_url = small_url;
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public String[] getScientific_name() {
        return scientific_name;
    }

    public String[] getOther_name() {
        return other_name;
    }

    public String getFamily() {
        return family;
    }

    public String getHybrid() {
        return hybrid;
    }

    public String getAuthority() {
        return authority;
    }

    public String getSubspecies() {
        return subspecies;
    }

    public String getCultivar() {
        return cultivar;
    }

    public String getVariety() {
        return variety;
    }

    public String getSpecies_epithet() {
        return species_epithet;
    }

    public String getGenus() {
        return genus;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public String getRegular_url() {
        return regular_url;
    }   

    public String getMedium_url() {
        return medium_url;
    }

    public String getSmall_url() {
        return small_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
