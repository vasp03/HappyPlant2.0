package se.mau.grupp7.happyplant2.model

enum class SortOption(val displayName: String) {
    CommonNameAZ("Common Name (A-Z)"),
    CommonNameZA("Common Name (Z-A)"),
    DateAddedNewest("Date Added (Newest)"),
    DateAddedOldest("Date Added (Oldest)"),
    NeedOfWaterMost("Need of water (Most)"),
    NeedOfWaterLeast("Need of water (Least)")
}
