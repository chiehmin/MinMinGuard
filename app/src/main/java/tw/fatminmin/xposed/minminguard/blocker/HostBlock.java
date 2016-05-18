package tw.fatminmin.xposed.minminguard.blocker;


import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;

public final class HostBlock {

    private static final String UNABLE_TO_RESOLVE_HOST = "Unable to resolve host";

    private HostBlock() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static void block(XC_LoadPackage.LoadPackageParam lpparam) {

        Class<?> inetAddrClz = XposedHelpers.findClass("java.net.InetAddress", lpparam.classLoader);
        Class<?> inetSockAddrClz = XposedHelpers.findClass(" java.net.InetSocketAddress", lpparam.classLoader);
        Class<?> socketClz = XposedHelpers.findClass("java.net.Socket", lpparam.classLoader);
        Class<?> ioBridgeClz = XposedHelpers.findClass("libcore.io.IoBridge", lpparam.classLoader);


        XposedBridge.hookAllConstructors(socketClz, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object obj = param.args[0];
                String host = "";
                if(obj.getClass().getName().equals("java.lang.String")) {
                    host = (String)obj;
                }
                else if(obj.getClass().getName().equals("java.net.InetAddress")) {
                    host = ((InetAddress) obj).getHostName();
                }
                if(Main.patterns.contains(host)) {
                    param.args[0] = null;
                    param.setResult(new Object());
                }
            }
        });

        XC_MethodHook inetAddrHookSingleResult = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String host = (String) param.args[0];

                if(Main.patterns.contains(host)) {
                    Log.d("inet_before_host", host);
                    param.setResult(new Object());
                    param.setThrowable(new UnknownHostException(UNABLE_TO_RESOLVE_HOST));
                }
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String host = (String) param.args[0];

                if(Main.patterns.contains(host)) {
                    Log.d("inet_after_host", host);
                    param.setResult(new Object());
                    param.setThrowable(new UnknownHostException(UNABLE_TO_RESOLVE_HOST));
                }
            }
        };


        XposedBridge.hookAllMethods(inetAddrClz, "getByName", inetAddrHookSingleResult);
        XposedBridge.hookAllMethods(inetAddrClz, "getByAddress", inetAddrHookSingleResult);
        XposedBridge.hookAllMethods(inetAddrClz, "getAllByName", inetAddrHookSingleResult);

        XposedBridge.hookAllMethods(inetSockAddrClz, "createUnresolved", inetAddrHookSingleResult);
        XposedBridge.hookAllConstructors(inetSockAddrClz, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String host = (String) param.args[0];
                if(Main.patterns.contains(host)) {
                    Log.d("inet_before_host", host);
                    param.args[0] = "localhost";
                    param.setResult(new Object());
                    param.setThrowable(new UnknownHostException(UNABLE_TO_RESOLVE_HOST));
                }
            }
        });


        // public static boolean connect(FileDescriptor fd, InetAddress inetAddress, int port) throws SocketException
        // public static boolean connect(FileDescriptor fd, InetAddress inetAddress, int port, int timeoutMs) throws SocketException, SocketTimeoutException
        // private static boolean connectErrno(FileDescriptor fd, InetAddress inetAddress, int port, int timeoutMs)
        XC_MethodHook ioBridgeHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                InetAddress addr = (InetAddress) param.args[1];
                String host = addr.getHostName();
                String ip = addr.getHostAddress();

                Log.d("fatminmin_iobridge", host + ":" + ip);

                if(Main.patterns.contains(host) || Main.patterns.contains(ip)) {
                    param.setResult(Boolean.valueOf(false));
                    param.setThrowable(new UnknownHostException(UNABLE_TO_RESOLVE_HOST));
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                InetAddress addr = (InetAddress) param.args[1];
                String host = addr.getHostName();
                String ip = addr.getHostAddress();

                if(Main.patterns.contains(host) || Main.patterns.contains(ip)) {
                    param.setResult(Boolean.valueOf(false));
                    param.setThrowable(new UnknownHostException(UNABLE_TO_RESOLVE_HOST));
                }
            }
        };

        XposedBridge.hookAllMethods(ioBridgeClz, "connect", ioBridgeHook);
        XposedBridge.hookAllMethods(ioBridgeClz, "connectErrno", ioBridgeHook);

    }
}
