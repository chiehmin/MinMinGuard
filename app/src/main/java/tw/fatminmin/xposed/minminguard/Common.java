package tw.fatminmin.xposed.minminguard;

import tw.fatminmin.xposed.minminguard.ui.fragments.MainFragment;

/**
 * Created by fatminmin on 2015/10/1.
 */
public class Common {
    public final static String MOD_PREFS = "ModSettings";
    public final static String UI_PREFS = "UI_PREF";

    // UI Pref
    public final static String KEY_FIRST_TIME = "first_time_2_0_alpha_7";
    public final static String KEY_SHOW_SYSTEM_APPS = "show_system_apps";

    // Xposed Mod Pref
    public final static String KEY_MODE = "mode";
    public final static String VALUE_MODE_AUTO = "auto";
    public final static String VALUE_MODE_BLACKLIST = "blacklist";
    public final static String VALUE_MODE_WHITELIST = "whitelist";

    // Fragment Name
    public final static String FRG_MAIN = "main_fragment";

    // MinMinProvider
    public final static String KEY_PKG_NAME = "PKG_NAME";
    public final static String KEY_NETWORK = "AD_NETWORKS";
    public final static String KEY_BLOCK_NUM = "BLOCK_NUM";

    private Common() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static String getModeString(MainFragment.FragmentMode mode) {
        switch (mode) {
            case AUTO:
                return Common.VALUE_MODE_AUTO;
            case BLACKLIST:
                return Common.VALUE_MODE_BLACKLIST;
            case WHITELIST:
                return Common.VALUE_MODE_WHITELIST;
            default:
                break;
        }
        return Common.VALUE_MODE_BLACKLIST;
    }
    public static String getWhiteListKey(String pkgName) {
        return pkgName + "_whitelist";
    }
}
