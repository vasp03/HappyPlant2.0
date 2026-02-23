package se.mau.grupp7.happyplant2.model

object DefectList {

    val NONE = Defect(
        id = "none",
        displayName = "None",
        category = "None",
        subCategory = "None",
        healthImpact = 0
    )

    val DEAD = Defect(
        id = "DEAD",
        displayName = "Dead",
        category = "Dead",
        subCategory = "Dead",
        healthImpact = -5
    )

    val ALL = listOf(
        NONE,
        // LEAF SYMPTOMS
        // Spots / Blights
        Defect("grease_spot", "Grease spot (Pythium blight)", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("dollar_spot", "Dollar spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("leaf_spot_diseases", "Leaf spot diseases", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("alternaria_leaf_spot", "Alternaria leaf spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("alternaria_leaf_blight", "Alternaria leaf blight", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("alternaria_blight", "Alternaria blight", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("ascochyta_blight", "Ascochyta blight", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("cercospora_leaf_spot", "Cercospora leaf spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("cherry_leaf_spot", "Cherry leaf spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("common_leaf_spot", "Common leaf spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("curvularia_leaf_spot", "Curvularia leaf spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("entomosporium_leaf_spot", "Entomosporium leaf spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("fairy_ring_leaf_spot", "Fairy-ring leaf spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("gray_leaf_spot", "Gray leaf spot", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("leaf_spot_septoria", "Leaf spot Septoria", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("leaf_spot_stemphylium", "Leaf spot Stemphylium", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("leaf_spot_ramularia", "Leaf spot Ramularia", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("leaf_spot", "Leaf spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("angular_leaf_spot", "Angular leaf spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("phomopsis_leaf_blight", "Phomopsis leaf blight", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("stemphylium_leaf_blight", "Stemphylium leaf blight", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("alternaria_rot", "Alternaria rot", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("ovulinia_petal_blight", "Ovulinia petal blight", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("camellia_petal_and_flower_blight", "Camellia petal and flower blight", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("leaf_blotch", "Leaf blotch", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("early_blight", "Early blight", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("late_blight", "Late blight", "Leaf Symptoms", "Spots or Blights", -4),
        Defect("cryptomeria_blight", "Cryptomeria blight", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("fire_spot", "Fire spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("ink_spot", "Ink spot", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("needle_cast_fungi", "Needle cast fungi", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("common_scab", "Common scab", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("powdery_scab", "Powdery scab", "Leaf Symptoms", "Spots or Blights", -2),
        Defect("black_spot", "Black spot", "Leaf Symptoms", "Spots or Blights", -3),
        Defect("leaf_spot_and_phomopsis_cane", "Leaf spot and Phomopsis cane", "Leaf Symptoms", "Spots or Blights", -3),
        // Powdery / Downy
        Defect("powdery_mildew", "Powdery mildew", "Leaf Symptoms", "Powdery or Downy Coatings", -3),
        Defect("downy_mildew", "Downy mildew", "Leaf Symptoms", "Powdery or Downy Coatings", -3),
        // Rust-Like Pustules
        Defect("rust", "Rust", "Leaf Symptoms", "Rust-Like Pustules", -3),
        Defect("bean_rust", "Bean rust", "Leaf Symptoms", "Rust-Like Pustules", -3),
        Defect("orange_rust", "Orange rust", "Leaf Symptoms", "Rust-Like Pustules", -4),
        Defect("yellow_rust", "Yellow rust", "Leaf Symptoms", "Rust-Like Pustules", -3),
        Defect("purple_blotch", "Purple blotch", "Leaf Symptoms", "Rust-Like Pustules", -3),
        Defect("common_rust", "Common rust", "Leaf Symptoms", "Rust-Like Pustules", -3),
        Defect("pear_rust", "Pear rust", "Leaf Symptoms", "Rust-Like Pustules", -3),
        Defect("blister_rust", "Blister rust", "Leaf Symptoms", "Rust-Like Pustules", -4),
        Defect("cedar_apple_rust", "Cedar-apple rust", "Leaf Symptoms", "Rust-Like Pustules", -3),
        Defect("coffee_rust", "Coffee rust", "Leaf Symptoms", "Rust-Like Pustules", -4),
        // Scabs, Curls, or Blisters
        Defect("scab", "Scab", "Leaf Symptoms", "Scabs, Curls, or Blisters", -3),
        Defect("apple_scab", "Apple scab", "Leaf Symptoms", "Scabs, Curls, or Blisters", -3),
        Defect("pear_scab", "Pear scab", "Leaf Symptoms", "Scabs, Curls, or Blisters", -3),
        Defect("oak_leaf_blister", "Oak leaf blister (Taphrina leaf curl)", "Leaf Symptoms", "Scabs, Curls, or Blisters", -2),
        Defect("peach_leaf_curl", "Peach leaf curl", "Leaf Symptoms", "Scabs, Curls, or Blisters", -3),
        Defect("leaf_blister", "Leaf blister", "Leaf Symptoms", "Scabs, Curls, or Blisters", -2),
        // Scorching or Yellowing
        Defect("almond_leaf_scorch", "Almond leaf scorch (Bacterial leaf scorch)", "Leaf Symptoms", "Scorching or Yellowing", -3),
        Defect("leaf_scorch", "Leaf scorch", "Leaf Symptoms", "Scorching or Yellowing", -3),
        Defect("papery_bark", "Papery bark (Sappy bark)", "Leaf Symptoms", "Scorching or Yellowing", -3),
        // Mosaic, Mottling, or Distortions
        Defect("apple_mosaic", "Apple mosaic", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -3),
        Defect("mosaic_viruses", "Mosaic viruses", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -3),
        Defect("curly_top", "Curly top", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -4),
        Defect("maize_dwarf_mosaic", "Maize dwarf mosaic", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -4),
        Defect("fig_mosaic", "Fig mosaic", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -3),
        Defect("tobacco_mosaic", "Tobacco mosaic", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -4),
        Defect("spotted_wilt_virus", "Spotted wilt virus", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -4),
        Defect("virus_diseases", "Virus diseases", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -3),
        Defect("viruses", "Viruses", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -3),
        Defect("potato_leafroll", "Potato leafroll", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -3),
        Defect("sunblotch", "Sunblotch", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -4),
        Defect("big_vein", "Big vein", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -3),
        Defect("measles", "Measles", "Leaf Symptoms", "Mosaic, Mottling, or Distortions", -3),
        // STEM AND BRANCH SYMPTOMS
        // Cankers
        Defect("bacterial_canker", "Bacterial canker", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("coryneum_blight", "Coryneum blight (Shot hole disease)", "Stem and Branch Symptoms", "Cankers", -3),
        Defect("european_canker", "European canker (Necria canker)", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("eutypa_dieback", "Eutypa dieback", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("dothiorella_canker", "Dothiorella canker", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("pseudonectria_canker", "Pseudonectria canker (Volutella canker and blight)", "Stem and Branch Symptoms", "Cankers", -3),
        Defect("canker_stain", "Canker stain (Ceratocystis canker)", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("cytospora_canker", "Cytospora canker", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("pitch_canker", "Pitch canker", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("canker_diseases", "Canker diseases", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("canker", "Canker", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("black_knot", "Black knot", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("chestnut_blight", "Chestnut blight", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("phomopsis_canker", "Phomopsis canker", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("stem_cankers_and_dieback", "Stem cankers and dieback", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("deep_bark_canker", "Deep bark canker", "Stem and Branch Symptoms", "Cankers", -4),
        Defect("shallow_bark_canker", "Shallow bark canker", "Stem and Branch Symptoms", "Cankers", -4),
        // Dieback or Blights
        Defect("ash_dieback", "Ash dieback", "Stem and Branch Symptoms", "Dieback or Blights", -4),
        Defect("branch_and_twig_dieback", "Branch and twig dieback", "Stem and Branch Symptoms", "Dieback or Blights", -4),
        Defect("eastern_filbert_blight", "Eastern filbert blight", "Stem and Branch Symptoms", "Dieback or Blights", -4),
        Defect("twig_blights", "Twig blights", "Stem and Branch Symptoms", "Dieback or Blights", -3),
        Defect("escallonia_dieback", "Escallonia dieback", "Stem and Branch Symptoms", "Dieback or Blights", -3),
        Defect("oak_branch_dieback", "Oak branch dieback", "Stem and Branch Symptoms", "Dieback or Blights", -4),
        Defect("oak_twig_blight", "Oak twig blight", "Stem and Branch Symptoms", "Dieback or Blights", -3),
        Defect("fire_blight", "Fire blight", "Stem and Branch Symptoms", "Dieback or Blights", -4),
        Defect("branch_wilt", "Branch wilt", "Stem and Branch Symptoms", "Dieback or Blights", -4),
        Defect("dutch_elm_disease", "Dutch elm disease", "Stem and Branch Symptoms", "Dieback or Blights", -4),
        Defect("blight", "Blight", "Stem and Branch Symptoms", "Dieback or Blights", -4),
        Defect("rice_bacterial_blight", "Rice bacterial blight", "Stem and Branch Symptoms", "Dieback or Blights", -4),
        // Galls, Knots, or Abnormal Growths
        Defect("crown_gall", "Crown gall", "Stem and Branch Symptoms", "Galls, Knots, or Abnormal Growths", -3),
        Defect("clubroot", "Clubroot", "Stem and Branch Symptoms", "Galls, Knots, or Abnormal Growths", -3),
        Defect("oleander_knot", "Oleander knot", "Stem and Branch Symptoms", "Galls, Knots, or Abnormal Growths", -3),
        Defect("olive_knot", "Olive knot", "Stem and Branch Symptoms", "Galls, Knots, or Abnormal Growths", -3),
        Defect("leaf_gall", "Leaf gall", "Stem and Branch Symptoms", "Galls, Knots, or Abnormal Growths", -2),
        Defect("witches_broom", "Witches' broom", "Stem and Branch Symptoms", "Galls, Knots, or Abnormal Growths", -3),
        Defect("fasciation", "Fasciation", "Stem and Branch Symptoms", "Galls, Knots, or Abnormal Growths", -2),
        Defect("hairy_root", "Hairy root", "Stem and Branch Symptoms", "Galls, Knots, or Abnormal Growths", -3),
        // ROOT AND CROWN SYMPTOMS
        // Root Rots
        Defect("armillaria_root_rot", "Armillaria root rot (Oak root fungus)", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("phytophthora_root_and_crown_rot", "Phytophthora root and crown rot", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("fusarium_root_rot", "Fusarium root rot", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("phytophthora_root_rot", "Phytophthora root rot", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("avocado_root_rot", "Avocado root rot", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("annosus_root_disease", "Annosus root disease", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("southern_blight", "Southern blight", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("black_root_rot", "Black root rot", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("root_stem_and_crown_rot", "Root stem and crown rot", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("dematophora_root_rot", "Dematophora root rot (Rosellinia root rot)", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("basal_stem_rots", "Basal stem rots", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("damping_off_and_seed_rots", "Damping off and seed rots", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("damping_off", "Damping off", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("damping_off", "Damping-off", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("dry_rot", "Dry rot", "Root and Crown Symptoms", "Root Rots", -4),
        Defect("heart_rot", "Heart rot", "Root and Crown Symptoms", "Root Rots", -4),
        // Patches or Blights at Base
        Defect("brown_patch", "Brown patch (large patch or Rhizoctonia blight )", "Root and Crown Symptoms", "Patches or Blights at Base", -3),
        Defect("fusarium_patch", "Fusarium patch (Microdochium patch or pink snow mold)", "Root and Crown Symptoms", "Patches or Blights at Base", -3),
        Defect("take_all_patch", "Take-all patch", "Root and Crown Symptoms", "Patches or Blights at Base", -3),
        Defect("spring_dead_spot", "Spring dead spot", "Root and Crown Symptoms", "Patches or Blights at Base", -3),
        Defect("summer_patch", "Summer patch", "Root and Crown Symptoms", "Patches or Blights at Base", -3),
        Defect("red_thread", "Red thread", "Root and Crown Symptoms", "Patches or Blights at Base", -2),
        Defect("snow_mold", "Snow mold", "Root and Crown Symptoms", "Patches or Blights at Base", -3),
        // FRUIT, BULB, OR EAR SYMPTOMS
        // Rots or Molds
        Defect("brown_rot", "Brown rot", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("botrytis_rot", "Botrytis rot (gray mold)", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("gray_mold", "Gray mold (Botrytis rot)", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("white_mold", "White mold", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("lettuce_drop", "Lettuce drop (white mold)", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("bacterial_soft_rots_leaf_spots_blights_wilts", "Bacterial soft rots leaf spots blights wilts", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("bacterial_soft_rot", "Bacterial soft rot", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("bacterial_soft_rots", "Bacterial soft rots", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("black_mold", "Black mold", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -3),
        Defect("blue_mold_rot", "Blue mold rot", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -3),
        Defect("botrytis_bulb_rot", "Botrytis bulb rot (Neck rot)", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("white_rot", "White rot", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("leak", "Leak (Rhizopus rot)", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -3),
        Defect("varnish_fungus_rot", "Varnish fungus rot", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        Defect("smoulder", "Smoulder", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -3),
        Defect("souring", "Souring", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -3),
        Defect("bacterial_ring_rot", "Bacterial ring rot", "Fruit, Bulb, or Ear Symptoms", "Rots or Molds", -4),
        // Spots, Scabs, or Blights
        Defect("anthracnose", "Anthracnose", "Fruit, Bulb, or Ear Symptoms", "Spots, Scabs, or Blights", -3),
        Defect("black_blight", "Black blight (Ringspot)", "Fruit, Bulb, or Ear Symptoms", "Spots, Scabs, or Blights", -3),
        Defect("cavity_spot", "Cavity spot", "Fruit, Bulb, or Ear Symptoms", "Spots, Scabs, or Blights", -2),
        // Smuts or Ergot
        Defect("common_smut", "Common smut", "Fruit, Bulb, or Ear Symptoms", "Smuts or Ergot", -3),
        Defect("head_smut", "Head smut", "Fruit, Bulb, or Ear Symptoms", "Smuts or Ergot", -3),
        Defect("fusarium_ear_and_stalk_rot", "Fusarium ear and stalk rot", "Fruit, Bulb, or Ear Symptoms", "Smuts or Ergot", -3),
        Defect("corn_smut", "Corn smut", "Fruit, Bulb, or Ear Symptoms", "Smuts or Ergot", -3),
        Defect("bunt", "Bunt", "Fruit, Bulb, or Ear Symptoms", "Smuts or Ergot", -3),
        Defect("smut", "Smut", "Fruit, Bulb, or Ear Symptoms", "Smuts or Ergot", -3),
        Defect("ergot", "Ergot", "Fruit, Bulb, or Ear Symptoms", "Smuts or Ergot", -3),
        // WHOLE PLANT OR SYSTEMIC SYMPTOMS
        // Wilts
        Defect("verticillium_wilt", "Verticillium wilt", "Whole Plant or Systemic Symptoms", "Wilts", -4),
        Defect("fusarium_wilt", "Fusarium wilt", "Whole Plant or Systemic Symptoms", "Wilts", -4),
        Defect("wilt", "Wilt", "Whole Plant or Systemic Symptoms", "Wilts", -4),
        // Diebacks or Declines
        Defect("sudden_oak_death", "Sudden oak death", "Whole Plant or Systemic Symptoms", "Diebacks or Declines", -4),
        Defect("pear_decline", "Pear decline", "Whole Plant or Systemic Symptoms", "Diebacks or Declines", -4),
        Defect("palm_diseases", "Palm diseases", "Whole Plant or Systemic Symptoms", "Diebacks or Declines", -4),
        // Bacterial or Phytoplasma Infections
        Defect("citrus_greening", "Citrus greening (Huanglongbing disease)", "Whole Plant or Systemic Symptoms", "Bacterial or Phytoplasma Infections", -4),
        Defect("aster_yellows", "Aster yellows", "Whole Plant or Systemic Symptoms", "Bacterial or Phytoplasma Infections", -4),
        Defect("bltva", "Beet leafhopper transmitted virescence agent (BLTVA)", "Whole Plant or Systemic Symptoms", "Bacterial or Phytoplasma Infections", -4),
        Defect("walnut_blight", "Walnut blight", "Whole Plant or Systemic Symptoms", "Bacterial or Phytoplasma Infections", -4),
        // Viral or Viroid Infections
        Defect("cherry_buckskin", "Cherry buckskin (X-disease)", "Whole Plant or Systemic Symptoms", "Viral or Viroid Infections", -4),
        Defect("exocortis", "Exocortis", "Whole Plant or Systemic Symptoms", "Viral or Viroid Infections", -3),
        Defect("tristeza_disease_complex", "Tristeza disease complex", "Whole Plant or Systemic Symptoms", "Viral or Viroid Infections", -4),
        Defect("psorosis", "Psorosis", "Whole Plant or Systemic Symptoms", "Viral or Viroid Infections", -3),
        Defect("panama_disease", "Panama disease", "Whole Plant or Systemic Symptoms", "Viral or Viroid Infections", -4),
        // OTHER OR NON-PATHOGENIC ISSUES
        // Fungal Growths or Brackets
        Defect("fairy_ring", "Fairy ring", "Other or Non-Pathogenic Issues", "Fungal Growths or Brackets", -1),
        Defect("fungi_nuisance", "Fungi Nuisance", "Other or Non-Pathogenic Issues", "Fungal Growths or Brackets", 0),
        Defect("mushrooms", "Mushrooms", "Other or Non-Pathogenic Issues", "Fungal Growths or Brackets", 0),
        Defect("hairy_turkey_tail", "Hairy turkey tail", "Other or Non-Pathogenic Issues", "Fungal Growths or Brackets", -1),
        Defect("parchment_fungus", "Parchment fungus", "Other or Non-Pathogenic Issues", "Fungal Growths or Brackets", -1),
        Defect("sulfur_fungus", "Sulfur fungus", "Other or Non-Pathogenic Issues", "Fungal Growths or Brackets", -1),
        Defect("artist_conk", "Artist's conk", "Other or Non-Pathogenic Issues", "Fungal Growths or Brackets", -1),
        Defect("common_split_gill", "Common split gill", "Other or Non-Pathogenic Issues", "Fungal Growths or Brackets", -1),
        Defect("oyster_mushroom", "Oyster mushroom", "Other or Non-Pathogenic Issues", "Fungal Growths or Brackets", 0),
        // Fluxes or Oozes
        Defect("foamy_canker", "Foamy canker (alcoholic flux)", "Other or Non-Pathogenic Issues", "Fluxes or Oozes", -1),
        Defect("alcoholic_flux", "Alcoholic flux (Foamy canker)", "Other or Non-Pathogenic Issues", "Fluxes or Oozes", -1),
        Defect("wetwood_or_slime_flux", "Wetwood or slime flux", "Other or Non-Pathogenic Issues", "Fluxes or Oozes", -1),
        Defect("drippy_acorns", "Drippy acorns (Drippy oak)", "Other or Non-Pathogenic Issues", "Fluxes or Oozes", -1),
        Defect("sooty_mold", "Sooty mold", "Other or Non-Pathogenic Issues", "Fluxes or Oozes", -1),
        DEAD
    )

    val categories: List<String>
        get() = ALL.map { it.category }.distinct()

    fun subCategories(category: String): List<String> =
        ALL.filter { it.category == category }
            .map { it.subCategory }
            .distinct()

    fun defects(category: String, subCategory: String): List<Defect> =
        ALL.filter {
            it.category == category && it.subCategory == subCategory
        }

    fun findById(id: String): Defect =
        ALL.firstOrNull { it.id == id } ?: NONE
}