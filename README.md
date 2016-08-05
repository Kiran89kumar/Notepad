# Notepad

Notepad
==

Simple Notepad application using Material Design, Depencency Injection, Rxjava, MVP with Clean architecture.

## Architecture

MVP:

Clean architecture
------------

The Clean architecture search to make independent our code from data sources, UI, Frameworks, etc.

Make more testable the code

MVP
------------

Model view presenter is one of the most used architectural pattern, in Android this pattern can be used with a few limitations, Activities and Fragments have more responsibility than a simple view, anywhere is a wonderfull pattern to be used in Android applications.

MATERAIL DESIGN
------------

Material Design is a design language developed by Google. Expanding upon the "card" motifs that debuted in Google Now, Material Design makes more liberal use of grid-based layouts, responsive animations and transitions, padding, and depth effects such as lighting and shadows. 

External Libs
------------

This project use this external libs

* [Dagger](http://square.github.io/dagger/)
* [rxJava](http://reactivex.io/tutorials.html)
* [rxAndroid](https://github.com/ReactiveX/RxAndroid)

* [googlePlay]
* [design]
* [recyclerView]
* [appcompatv7]

* [junit]

### Prerequisites

* [Download Android Studio](http://developer.android.com/sdk/installing/studio.html).  

* Under Tools / Android / Android SDK Manager make sure "Extras/Google Repository" and "Extras/Android Support Repository" are installed.

### Clone the git repository

Use Git to clone the Notepad repository to your local disk: 

```
$ git clone https://github.com/Kiran89kumar/Notepad.git
$ cd Notepad
```

### Enable settings.gradle file

* `cp settings.gradle.example settings.gradle`

*Note: settings.gradle cannot be checked in directly due to Android Studio issue #[65915](https://code.google.com/p/android/issues/detail?id=65915)*

### Importing Project into Android Studio

You should be able to import the project directly into Android Studio:

* Start Android Studio
* Choose File / Import Project and choose the settings.gradle file in the Notepad directory you cloned earlier.
* Hit Finish and wait for all tasks to finish (may take a while)

### Running project in Android Studio

If you've checked out this project directly, you might notice there is *nothing to run*.  That is correct, as this project is a library.

If you want to run something (aside from the tests), you should get one of the sample apps listed below.


## Building Notepad on command line via gradle

* Clone the git repository
    * See details above
* Enable settings.gradle file
    * See details above
* If you don't already have a `local.properties` file, configure Android Studio SDK location
    * `cp local.properties.example local.properties`
    * Customize `local.properties` according to your SDK installation directory
* Compile (assemble)
    * `$ ./gradlew assemble`
* Test
    * `$ ./gradlew connectedAndroidTest`
* Build (compile & test)
    * `$ ./gradlew build`

    
## System Requirements

- Android 5.0 Lollipop (API level 21) and above.
