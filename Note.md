# Development Note

### Removing adview or preventing add adview may cause following problems
```java
protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
    View view = (View) param.args[0];
    ViewGroup thisView = (ViewGroup) param.thisObject;
    if (isAdView(view.getContext(), pkgName, view)) {
        Util.log(pkgName, "NameBasedBlocking before addView: " + view.getClass().getName());
        if (thisView.getChildCount() == 0) {
            Main.removeAdView(thisView, pkgName, true);
        }
        param.setResult(new Object());
    }
}

Attempt to invoke virtual method 'void com.google.android.gms.ads.AdView.setVisibility(int)' on a null object reference
```
