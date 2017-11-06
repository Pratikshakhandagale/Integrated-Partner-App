# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn **
-keep class org.ekstep.genieservices.events.** { *; }

-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class org.ekstep.genie.model.** { *; }
-keep class org.ekstep.genie.telemetry.** { *; }
-keep class org.ekstep.genieservices.** { *; }
-keep class org.ekstep.geniecanvas.** { *; }
-keep class org.apache.cordova.** { *; }
-keep class com.borismus.webintent.** { *; }
-keep class com.ionic.keyboard.** { *; }
-keep class com.xmartlabs.cordova.market.** { *; }

-keep class org.ekstep.summarizer.** { *;}
-keep class org.ekstep.genie.customview.treeview.TreeItemHolder { *;}
-keep class org.ekstep.genie.customview.treeview.TreeItemHolder { *;}
-keep class org.ekstep.genie.BuildConfig { *;}

# Event Bus
-keep class org.greenrobot.eventbus.** { *;}
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

-keep class * implements org.ekstep.genieservices.commons.IProfileConfig { *;}
-keep class * implements org.ekstep.genieservices.commons.IPlayerConfig { *;}
#-keep class org.ekstep.genie.util.geniesdk.PlayerConfig { *;}
#-keep class org.chromium.base.annotations.AccessedByNative { *; }


-keep class org.crosswalk.** { *; }
-keep class io.ionic.** { *; }
#-keep class android.support.v7.** { *; }
#-keep class android.support.v4.** { *; }
#-keep class android.support.design.widget.** { *; }
#-keep class com.google.gson.** { *; }
#-keep class com.squareup.okhttp.** { *; }
#-keep class okio.** { *; }
#-keep class org.joda.time.** { *; }
#-keep class com.github.fge.** { *; }
#-keep class com.fasterxml.jackson.** { *; }
#-keep class com.google.common.base.** { *; }
#-keep class org.ekstep.genie.customview.VelocityViewPager$OnClickListener { *; }
#-keep class org.chromium.ui.** { *; }

# Android common:
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keepclassmembers class * {
    static final %                *;
    static final java.lang.String *;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepattributes InnerClasses
-keep class **.R
-keep class **.R$* {
    <fields>;
}

-adaptresourcefilenames    **.properties,**.gif,**.jpg
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF

# Keep native & callbacks
-keepclasseswithmembernames class *{
    native <methods>;
}

-keepattributes JNINamespace
-keepattributes CalledByNative
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# Too many hard code reflections between xwalk wrapper and bridge,so
# keep all xwalk classes.
-keep class org.xwalk.**{ *; }
-keep interface org.xwalk.**{ *; }
-keep class com.example.extension.**{ *; }
-keep class org.crosswalkproject.**{ *; }

# Rules for org.chromium classes:
# Keep annotations used by chromium to keep members referenced by native code
-keep class org.chromium.base.*Native*
-keep class org.chromium.base.annotations.JNINamespace
-keepclasseswithmembers class org.chromium.** {
    @org.chromium.base.AccessedByNative <fields>;
}
-keepclasseswithmembers class org.chromium.** {
    @org.chromium.base.*Native* <methods>;
}

-keep class org.chromium.** {
    native <methods>;
}

# Keep methods used by reflection and native code
-keep class org.chromium.base.UsedBy*
-keep @org.chromium.base.UsedBy* class *
-keepclassmembers class * {
    @org.chromium.base.UsedBy* *;
}

-keep @org.chromium.base.annotations.JNINamespace* class *
-keepclassmembers class * {
    @org.chromium.base.annotations.CalledByNative* *;
}

# Suppress unnecessary warnings.
-dontnote org.chromium.net.AndroidKeyStore
# Objects of this type are passed around by native code, but the class
# is never used directly by native code. Since the class is not loaded, it does
# not need to be preserved as an entry point.
-dontnote org.chromium.net.UrlRequest$ResponseHeadersMap

# Generate by aapt. may only need for testing, just add them here.
-keep class org.chromium.ui.ColorPickerAdvanced { <init>(...); }
-keep class org.chromium.ui.ColorPickerMoreButton { <init>(...); }
-keep class org.chromium.ui.ColorPickerSimple { <init>(...); }

# Preserve annotations, line numbers, and source file names
-keepattributes *Annotation*,SourceFile,LineNumberTable

#-printconfiguration config.txt

-keep class com.facebook.stetho.**{ *; }

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** w(...);
    public static *** v(...);
    public static *** i(...);
}