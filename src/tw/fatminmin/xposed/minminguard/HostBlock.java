package tw.fatminmin.xposed.minminguard;


import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HostBlock {

    public static void block(XC_LoadPackage.LoadPackageParam lpparam) {

        Class<?> inetAddrClz = XposedHelpers.findClass("java.net.InetAddress", lpparam.classLoader);
        Class<?> ioBridgeClz = XposedHelpers.findClass("libcore.io.IoBridge", lpparam.classLoader);

        XC_MethodHook inetAddrHookSingleResult = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                InetAddress result = (InetAddress) param.getResult();
                String host = result.getHostName();
                String ip = result.getHostAddress();

                if(Main.urls.contains(host) || Main.urls.contains(ip) || host.contains("yahoo")) {
                    param.setResult(Boolean.valueOf(false));
                    param.setThrowable(new UnknownHostException("Unable to resolve host"));
                }
            }
        };
        XC_MethodHook inetAddrHookMultipleResult = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                InetAddress[] result = (InetAddress[]) param.getResult();
                for (InetAddress addr : result) {
                    String host = addr.getHostName();
                    String ip = addr.getHostAddress();

                    if(Main.urls.contains(host) || Main.urls.contains(ip) || host.contains("yahoo")) {
                        param.setResult(Boolean.valueOf(false));
                        param.setThrowable(new UnknownHostException("Unable to resolve host"));
                        break;
                    }
                }
            }
        };


        XposedBridge.hookAllMethods(inetAddrClz, "getByName", inetAddrHookSingleResult);
        XposedBridge.hookAllMethods(inetAddrClz, "getByAddress", inetAddrHookSingleResult);
        XposedBridge.hookAllMethods(inetAddrClz, "getAllByName", inetAddrHookMultipleResult);


        // public static boolean connect(FileDescriptor fd, InetAddress inetAddress, int port) throws SocketException
        // public static boolean connect(FileDescriptor fd, InetAddress inetAddress, int port, int timeoutMs) throws SocketException, SocketTimeoutException
        // private static boolean connectErrno(FileDescriptor fd, InetAddress inetAddress, int port, int timeoutMs)
        XC_MethodHook ioBridgeHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                InetAddress addr = (InetAddress) param.args[1];
                String host = addr.getHostName();
                String ip = addr.getHostAddress();

                if(Main.urls.contains(host) || Main.urls.contains(ip) || host.contains("yahoo")) {
                    param.setResult(Boolean.valueOf(false));
                    //param.setThrowable(new UnknownHostException("Unable to resolve host"));
                }
            }
        };

        XposedBridge.hookAllMethods(ioBridgeClz, "connect", inetAddrHookMultipleResult);
        XposedBridge.hookAllMethods(ioBridgeClz, "connectErrno", inetAddrHookMultipleResult);

    }
}
