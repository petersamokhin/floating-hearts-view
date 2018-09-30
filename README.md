# Floating Hearts

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Floating%20Hearts%20view-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7155) [![](https://jitpack.io/v/petersamokhin/floating-hearts-view.svg)](https://jitpack.io/#petersamokhin/floating-hearts-view) [![GitHub license](https://img.shields.io/badge/License-MIT-brightgreen.svg)](https://github.com/petersamokhin/floating-hearts-view/blob/master/LICENSE)


Instagram-like floating hearts view for android.

<img src="https://petersamokhin.com/files/projects/fh/demo.gif" width="400" height="651" />

Demo APK: https://petersamokhin.com/files/projects/fh/fh.apk

# Install

1. Add `jitpack` repo to your project-level `build.gradle`:
```groovy
allprojects {
    repositories {
        // other repos
	    maven { url 'https://jitpack.io' }
    }
}
```

2. Add library to your dependencies:
```groovy
dependencies {
    implementation 'com.github.petersamokhin:floating-hearts-view:$ACTUAL_VERSION'
}
```
Latest version: https://github.com/petersamokhin/floating-hearts-view/releases

# Usage

Configure renderer:

```kotlin
val config = HeartsRenderer.Config(
            5f,                        // The max amplitude of the flight along the X axis
            0.15f,                     // Duration of the flying animation will be multiplied by this value (lower — faster)
            2f                         // Heart size coefficient 
)
heartsView.applyConfig(config)
```

Or in your XML layout file:

```xml
<com.petersamokhin.android.floatinghearts.HeartsView
            android:id="@+id/heartsView"
            android:layout_width="match_parent"
            android:layout_height="330dp"
            app:x_max="3"
            app:size_coeff="2.5"
            app:floating_time_coeff="0.8" />
```

Make your model:

```kotlin

// Get bitmap from image by URL
// or simply convert from drawable, etc
val image = "https://cdn.shopify.com/s/files/1/1061/1924/products/Thinking_Face_Emoji_large.png"
val byteArray = URL(image).readBytes()
val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

val model = HeartsView.Model(
            0,                         // Unique ID of this image, used for Rajawali materials caching
            bitmap                     // Bitmap image
)
```

Let your image fly!

```kotlin
heartsView.emitHeart(model)
```

# 3rd party
[Rajawali](https://github.com/Rajawali/Rajawali) is the only one dependency. Inspired by Instagram/Snapchat/etc broadcasts.

# Known issues
On some devices / on some Android versions `NullPointerException` may happen after `new Material()` call in `HeartsRenderer`. This causes because `GLES20.glGetString( GLES20.GL_EXTENSIONS );` is returning null.

To fix this simply do not call `emitHeart` in first 300-500 ms after activity start.
