package com.xiaomi.stat.d;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.gallery3d.exif.ExifInterface;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.TimeZone;

public class m {

    /* renamed from: a  reason: collision with root package name */
    public static final int f514a = 28;

    /* renamed from: b  reason: collision with root package name */
    private static final String f515b = "OSUtil";

    /* renamed from: c  reason: collision with root package name */
    private static final String f516c = "";

    /* renamed from: d  reason: collision with root package name */
    private static Method f517d;

    /* renamed from: e  reason: collision with root package name */
    private static Class f518e;

    /* renamed from: f  reason: collision with root package name */
    private static Method f519f;
    private static Boolean g;

    static {
        try {
            f517d = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
        } catch (Exception unused) {
        }
        try {
            f518e = Class.forName("miui.os.Build");
        } catch (Exception unused2) {
        }
        try {
            f519f = Class.forName("android.provider.MiuiSettings$Secure").getDeclaredMethod("isUserExperienceProgramEnable", new Class[]{ContentResolver.class});
            f519f.setAccessible(true);
        } catch (Exception unused3) {
        }
    }

    public static String a(Context context) {
        String a2 = a("gsm.operator.numeric");
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(a2)) {
            for (String str : a2.split(",")) {
                if (!TextUtils.isEmpty(str) && !"00000".equals(str)) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(str);
                }
            }
        }
        String sb2 = sb.toString();
        if (TextUtils.isEmpty(sb2)) {
            sb2 = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator();
        }
        return sb2 == null ? "" : sb2;
    }

    private static String a(String str) {
        try {
            if (f517d != null) {
                return String.valueOf(f517d.invoke((Object) null, new Object[]{str}));
            }
        } catch (Exception e2) {
            k.b(f515b, "getProp failed ex: " + e2.getMessage());
        }
        return null;
    }

    public static boolean a() {
        Boolean bool = g;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (!TextUtils.isEmpty(a("ro.miui.ui.version.code"))) {
            g = true;
        } else {
            g = false;
        }
        return g.booleanValue();
    }

    public static String b() {
        return "Android";
    }

    public static boolean b(Context context) {
        Method method = f519f;
        if (method == null) {
            return true;
        }
        try {
            return ((Boolean) method.invoke((Object) null, new Object[]{context.getContentResolver()})).booleanValue();
        } catch (Exception e2) {
            Log.e(f515b, "isUserExperiencePlanEnabled failed: " + e2.toString());
            return true;
        }
    }

    public static String c() {
        return Build.VERSION.RELEASE;
    }

    public static String d() {
        return Build.VERSION.INCREMENTAL;
    }

    public static String e() {
        try {
            return TimeZone.getDefault().getDisplayName(false, 0);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String f() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    public static String g() {
        String a2 = a("ro.miui.region");
        if (TextUtils.isEmpty(a2)) {
            a2 = Locale.getDefault().getCountry();
        }
        return a2 == null ? "" : a2;
    }

    public static String h() {
        Class cls = f518e;
        if (cls == null) {
            return "";
        }
        try {
            return ((Boolean) cls.getField("IS_ALPHA_BUILD").get((Object) null)).booleanValue() ? ExifInterface.GpsStatus.IN_PROGRESS : ((Boolean) f518e.getField("IS_DEVELOPMENT_VERSION").get((Object) null)).booleanValue() ? "D" : ((Boolean) f518e.getField("IS_STABLE_VERSION").get((Object) null)).booleanValue() ? ExifInterface.GpsLatitudeRef.SOUTH : "";
        } catch (Exception e2) {
            Log.e(f515b, "getRomBuildCode failed: " + e2.toString());
            return "";
        }
    }

    public static boolean i() {
        Class cls = f518e;
        if (cls == null) {
            return false;
        }
        try {
            return ((Boolean) cls.getField("IS_INTERNATIONAL_BUILD").get((Object) null)).booleanValue();
        } catch (Exception unused) {
            return false;
        }
    }
}
