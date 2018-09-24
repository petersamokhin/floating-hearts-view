# floating-hearts-view
Instagram-like floating hearts view.

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
            2f,                        // Heart size coefficient 
        )
heartsView.applyConfig(config)
```

Make your model:

```kotlin
val image = "https://cdn.shopify.com/s/files/1/1061/1924/products/Thinking_Face_Emoji_large.png"
val bitmap = URL(image).readBytes().toBitmap()
val model = HeartsView.Model(
            0,                         // Unique ID of this image, used for Rajawali materials caching
            bitmap                     // Bitmap image — easy to use any drawable or image by URL and convert to bitmap
       )
```

Let your image fly!

```kotlin
heartsView.emitHeart(model)
```

# 3rd party
Only one dependency — [Rajawali](https://github.com/Rajawali/Rajawali).
Inspired by Instagram and VK Live broadcasts.
