package vn.com.omart.backend;

public class CacheName {

    // 30 days - Evil when category updated/created
    public static final String CATEGORIES = "omart:client:home";
    public static final Long CATEGORIES_TTL = 60L * 60L * 24L * 30L;

    // 30 days - Evil when poi updated/created
    public static final String POI = "omart:poi";
    public static final Long POI_TTL = 60L * 60L * 24L * 30L;

    public static final String PROVINCE = "omart:province";
    public static final Long PROVINCE_TTL = 60L * 60L * 24L * 30L;

    public static final String DISTRICT = "omart:district";
    public static final Long DISTRICT_TTL = 60L * 60L * 24L * 30L;

    public static final String WARD = "omart:ward";
    public static final Long WARD_TTL = 60L * 60L * 24L * 30L;

    public static final String CATEGORY_VERIFY_CODE = "omart:category:verify:code";
    public static final Long CATEGORY_VERIFY_CODE_TTL = 60L * 5L;

    // 30 days
//    public static final String APP_CONFIG_ID = "key";
//    public static final Long APP_CONFIG_ID_TTL = 60L * 60L * 24L * 30L;
}
