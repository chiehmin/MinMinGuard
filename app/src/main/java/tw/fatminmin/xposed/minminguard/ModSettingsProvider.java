package tw.fatminmin.xposed.minminguard;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

import static tw.fatminmin.xposed.minminguard.Common.MOD_PREFS;

public class ModSettingsProvider extends RemotePreferenceProvider
{
    /**
     * Initializes the remote preference provider with the specified
     * authority and preference files. The authority must match the
     * {@code android:authorities} property defined in your manifest
     * file. Only the specified preference files will be accessible
     * through the provider.
     *
     * @param authority The authority of the provider.
     * @param prefNames The names of the preference files to expose.
     */
    public ModSettingsProvider()
    {
        super("tw.fatminmin.xposed.minminguard.modesettings", new String[]{MOD_PREFS});
    }
}
