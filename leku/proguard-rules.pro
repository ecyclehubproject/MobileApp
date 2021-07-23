# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ferranpons/Documents/Android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
keep interface com.schibstedspain.** { *; }
-dontwarn com.schibstedspain.**
-keep class com.schibstedspain.** { *; }

# RxJava
-dontwarn rx.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Retrolambda
-dontwarn java.lang.invoke.*

# Google Maps
-keep class com.google.maps.** { *; }
-dontwarn com.google.maps.**
-keep class org.joda.time.** { *; }
-dontwarn org.joda.time.**
-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.**
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-keep class okio.** { *; }
-dontwarn okio.**
