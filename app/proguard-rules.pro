# Add project specific ProGuard rules here.
# You can control the setString of applied configuration files using the
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

####################################################################################################

#Android Support Library
-keep class android.support.v7.widget.** { *; }
-keep interface android.support.v7.widget.** { *; }

#Kotlin
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# OkHttp 3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

#Picasso
-dontwarn com.squareup.okhttp.**

#Gson
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by trailer_weight_balance, so configure it to keep all of it.
-keepattributes Signature

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Android Billing Library
-keep class com.android.vending.billing.**

# Dagger ProGuard rules.
-keep class com.google.errorprone.annotations.** { *; }
-dontwarn com.google.errorprone.annotations.*
-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}
-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection

# DataBinding
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }

# Timber
-dontwarn org.jetbrains.annotations.**