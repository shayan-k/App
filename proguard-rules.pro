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
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.

-keepclassmembers class com.example.myapp_sherkat.classs.Rest** { <fields>; }
-keepclassmembers class com.example.myapp_sherkat.classs.Present { <fields>; }
-keepclassmembers class com.example.myapp_sherkat.classs.User { <fields>; }
-keepclassmembers class com.example.myapp_sherkat.classs.Slider { <fields>; }
-keepclassmembers class com.example.myapp_sherkat.classs.ResponcePresent { <fields>; }
-keepclassmembers class com.example.myapp_sherkat.classs.ResponceRest { <fields>; }

-keepclassmembers class com.example.myapp_sherkat.notification.Notification { <fields>; }
-keepclassmembers class com.example.myapp_sherkat.notification.PushNotification { <fields>; }
