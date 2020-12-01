<img src="https://raw.githubusercontent.com/iammert/TileProgressView/main/art/art.png"/>

# TileProgressView
Simple Progress View with Tile Animation

# GIF

<img src="https://raw.githubusercontent.com/iammert/TileProgressView/main/art/animatedart.gif"/>

# Usage

```xml
<com.iammert.tileprogressview.TiledProgressView
        android:layout_width="match_parent"
        android:layout_height="32dp"
        android:layout_margin="32dp" />
```

# Modify

```kotlin
tiledProgressView.setProgress(0.3f) //Between 0.0f - 1.0f
tiledProgressView.setLoadingColor(R.color.purple) //Color Resource
```


# Setup
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.iammert:TileProgressView:0.1'
}
```

License
--------


    Copyright 2020 Mert Şimşek

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


