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


-keep class com.agamilabs.smartshop.adapter.* { *; }
-keep class com.agamilabs.smartshop.database.* { *; }
-keep class com.agamilabs.smartshop.Fragments.* { *; }
-keep class com.agamilabs.smartshop.model.* { *; }
-keep class com.agamilabs.smartshop.activity.* { *; }
-keep class com.agamilabs.smartshop.async.* { *; }
-keep class com.agamilabs.smartshop.constants.* { *; }
-keep class com.agamilabs.smartshop.controller.* { *; }
-keep class com.agamilabs.smartshop.services.* { *; }
-keep class com.agamilabs.smartshop.ui.* { *; }
-keep class com.agamilabs.smartshop.utilities.* { *; }
-keep class com.agamilabs.smartshop.custom.* { *; }
-keep class com.agamilabs.smartshop.Interfaces.* { *; }



-dontwarn com.google.android.gms.*
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**
-dontwarn com.android.volley.toolbox.**
