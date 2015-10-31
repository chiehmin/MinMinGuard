# MinMinGuard

## Description

MinMinGuard is an Ad-remover made with Xposed Framework for Android. MinMinGuard can completely remove both the ads inside apps and the empty space caused by those ads. Conventional ads removing apps are only able to block the ad content, but the space taken by the ad will still remain unused (black). MinMinGuard successfully removes that black space, which extends the app window and makes your user-experience better!

![Comparison](http://fatminmin.com/mmg/compare.png)

You can find out more information [here](http://fatminmin.com/pages/minminguard.html)

## Adding support for a new ad network

You may find some apps' ads are not blocked and removed by MinMinGuard. This may cause by the apps use non-supported ad networks(some local ads providers ex: Vpon in Taiwan). In this case you can help me by doing a reverse engineering on the app and add the non-supported ad network into support. 

```java
public abstract class Blocker {
    /**
     * 
     * @param packageName
     * @param lpparam
     * @param removeAd
     * @return True if currrent handling app using this adnetwork. False otherwise.
     */
    abstract public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd);
    abstract public String getBanner();
    abstract public String getBannerPrefix();
    public String getName() {
        return getClass().getSimpleName();
    }
}
```

You can implement a ad network blocker by extending `Blocker` class. Method `handleLoadPackage` is used for **API based blocking** to intercept api call for loading ads from adview. After you hook and get the adview, you can use ` Main.removeAdView(view, pkgname, true);` helper method to remove the adview from layout. Method `getBanner` and `getBannerPrefix` are used for **Name based blocking**. You can opt to return `null` and name based blocking will not be triggered. 

```java
 */
public class Adbert extends Blocker {

    public final static String banner = "com.adbert.AdbertADView";
    public final static String bannerPrefix = "com.adbert";
    public final static String inter = "com.adbert.AdbertInterstitialAD";

    @Override
    public boolean handleLoadPackage(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {

        try {

            Class<?> bannerClazz = findClass(banner, lpparam.classLoader);
            Class<?> interClazz = findClass(inter, lpparam.classLoader);

            XposedBridge.hookAllMethods(bannerClazz, "start", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect AdbertBanner start in " + packageName);

                    if (removeAd) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }

            });

            XposedBridge.hookAllMethods(interClazz, "loadAd",  new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect AdbertInter loadAd in " + packageName);

                    if(removeAd) {
                        param.setResult(new Object());
                    }
                }
            });

            Util.log(packageName, packageName + " uses AdbertBanner");
        }
        catch(XposedHelpers.ClassNotFoundError e) {
            return false;
        }
        return true;
    }

    @Override
    public String getBanner() {
        return banner;
    }

    @Override
    public String getBannerPrefix() {
        return bannerPrefix;
    }
}
```

```java
public static Blocker[] blockers = {
            /* Popular adnetwork */
            new Ad2iction(), new Adbert(), new Adfurikun(), new AdMarvel(), new Admob(), new AdmobGms(), new Amazon(),
            new Amobee(), new AppBrain(), new Bonzai(), new Chartboost(), new Domob(), new Facebook(), new Flurry(),
            new GmsDoubleClick(), new Hodo(), new ImpAct(), new Inmobi(), new Intowow(), new KuAd(), new mAdserve(),
            new Madvertise(), new MasAd(), new MdotM(), new Millennial(), new Mobclix(), new MoPub(), new Nend(),
            new Og(), new Onelouder(), new OpenX(), new SmartAdserver(), new Smarti(), new Startapp(), new Tapfortap(),
            new TWMads(), new UnityAds(), new Vpadn(), new Vpon(), new Waystorm(), new Yahoo()
    };
```

Above is a classic example. After you complete the new blocker, you have to remember to add it to the `blockers` array at the `Main` class to activiate the new blocker.

After you confirm the new blocker is working, you can send me a pull request. I will double check it and merge it into the MinMinGuard. Finally, I will list you as a contributer :).

## Contributers

* FatMinMin

