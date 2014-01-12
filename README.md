MinMinGuard
===========
AD REMOVER! The best alternative!  
 [XDA discussion thread](http://forum.xda-developers.com/showthread.php?p=49112940#post49112940)  
 [XDA News - Selectively Block Ads on Certain Apps with Xposed Module](http://www.xda-developers.com/android/selectively-block-ads-on-certain-apps-with-xposed-module/)   

Index
-----

* [Description](https://github.com/chiehmin/MinMinGuard/#description)
* [Features](https://github.com/chiehmin/MinMinGuard/#features)
* [How does it work?](https://github.com/chiehmin/MinMinGuard/#how-does-it-work)
* [Frequently asked questions](https://github.com/chiehmin/MinMinGuard/#frequently-asked-questions)
* [Thanks to](https://github.com/chiehmin/MinMinGuard/#thanks-to)

Description
-----------

It can completely remove both the ads inside apps and the empty space caused by those ads. Conventional ads removing apps are only able to block the ad content, but the space taken by the ad will still remain unused (black). MinMinGuard successfully removes that black space, which extends the app window and makes it look like the pro version app was completely ad free.

Features
--------

* **Totally remove the advertisement.** You can notice that AdBlock and AdAway only stops showing the ad content, but it can not remove the empty field that was originally taken by the ad. MinMinGuard can totally remove the empty field!
* **Lightweight.** Alternative ad removing apps (AdBlock etc) constantly run a background VPN service, which puts a heavy loading on the system. MinMinGuard does not need to run a VPN service, so it saves system resources and, thus, extends the battery life.
* **Per App Setting.** MinMinGuard lets you choose which apps you want to remove ads from. If you only want to remove ads from several apps, MinMinGuard is your best solution.

How does it work?
-----------------

* **API based blocking:** Block function call to the advertisement network sdk. Then recursively remove the ad space. Now supported advertisement network for this approach are "Admob", "MoPub", "Vpon", and "KuAd".

* **URL based blocking:** Block webviews that contains prohibited urls. ~~For this way I still can not remove the empty space. That's why for some apps the empty space is still there.~~ From v1.2.0, MinMinGuard can also clear empty space for some app by using url filtering approach.

Frequently asked questions
--------------------------

**Q: Why it is not work for some apps?**
A: You can take a look at "how does it work" section. For url based blocking, there is still a few apps contains empty space that can not be removed. I hope in the following updates I can support more advertisement network with API based blocking.

**Q: Can AdAway or Adblock remove the empty space cause by ads?**
A: I tested AdAway and Adblock. They both can not remove the empty space. If there is any other app that can remove the empty space, please tell me and I will mention here.

**Q: Will MinMinGuard has paid version?**
A: No, Never. I hope everyone can use full functioned version with free price.

**Q: How can I donate you?**
A: I am really really appreciate for people who want to donate me. But now I think this is an opensource project, I should not take any money from it. Because maybe someday I will be too busy to update it and somebody will take my place to keep update it. And there may be some translators contribute for this module. So I can not take all the credits.


