# ProGuard & R8 Optimization Rules for Marketplace Application

# Keep attributes required for reflection and serialization
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# Keep kotlinx.serialization models & serializers
-keep @kotlinx.serialization.Serializable class com.example.marketplace.data.models.** { *; }
-keepclassmembers class com.example.marketplace.data.models.** {
    *** Companion;
    *** $serializer;
}
-keep class com.example.marketplace.data.models.** { *; }

-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}

# Keep Retrofit API interfaces and models
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers enum * { *; }
-keep interface com.example.marketplace.data.remote.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Coil Image Loader
-keep class coil.** { *; }
-dontwarn coil.**

# Android Jetpack Architecture Components
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }
-keep class androidx.datastore.** { *; }
