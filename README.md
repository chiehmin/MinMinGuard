# ![icon](app/src/main/res/drawable-hdpi/icon.png) MinMinGuard [![Build Status](https://travis-ci.org/chiehmin/MinMinGuard.svg?branch=master)](https://travis-ci.org/chiehmin/MinMinGuard)

MinMinGuard is an Ad-remover made with Xposed Framework for Android. MinMinGuard can completely remove both the ads inside apps and the empty space caused by those ads. Conventional ads removing apps are only able to block the ad content, but the space taken by the ad will still remain unused (black). MinMinGuard successfully removes that black space, which extends the app window and makes your user-experience better!

![Comparison](http://fatminmin.com/mmg/compare.png)

## Discussion and Supports
If you have any idea or need some supports, you are welcome to post on the Xda Developers forum or chat in the Gitter Room.

[![](https://fatminmin.com/images/xda_link.svg)](http://forum.xda-developers.com/xposed/modules/xposed-minminguard-v1-7-0-cancelled-t2597332)
[![Join the chat at https://gitter.im/MinMinGuard/MinMinGuard](https://badges.gitter.im/MinMinGuard/MinMinGuard.svg)](https://gitter.im/MinMinGuard/MinMinGuard?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Developing and Contributing

### Adding a new ad network into support

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

You can implement a ad network blocker by extending `Blocker` class. Method `handleLoadPackage` is used for **API based blocking** to intercept api call for loading ads from adview. You can leverage `ApiBlocking.removeBanner` and `ApiBlocking.blockAdFunction` to remove banner view and block ad functions. Method `getBanner` and `getBannerPrefix` are used for **Name based blocking**. You can opt to return `null` and name based blocking will not be triggered.

```java
public class Flurry extends Blocker {

  public static final String BANNER = "com.flurry.android.FlurryAds";
  public static final String BANNER_PREFIX = "com.flurry.android";

  public static final String NATIVE_AD = "com.flurry.android.ads.FlurryAdNative";

	@Override
	public String getBannerPrefix() {
		return BANNER_PREFIX;
	}

	@Override
	public String getBanner() {
		return BANNER;
	}
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
    boolean result = false;
    result |= ApiBlocking.removeBanner(packageName, BANNER, "displayAd", lpparam, removeAd);
    result |= ApiBlocking.blockAdFunction(packageName, BANNER, "fetchAd", lpparam, removeAd);
    result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "fetchAd", lpparam, removeAd);
		return result;
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

## Legal and Licensing
MinMinGuard is licensed under the [GPL-3.0 license](LICENSE).