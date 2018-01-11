# WorkoutJournal
Track your exercises with the Workout Journal.

[Download link for stable alpba build](https://rink.hockeyapp.net/apps/2d9b423d8e47445a8334994e9c935167/app_versions/5)

Note: Removed calendar temp. A new calendar will be in the next few versions. 


Readme coming soon...

MVVM, Room Database, LiveData

### Some images of the app

<p float="top">
<img src="https://github.com/EugeneHoran/WorkoutJournal/blob/master/images/device-2018-01-10-134234.png" width="220" />
<img src="https://github.com/EugeneHoran/WorkoutJournal/blob/master/images/device-2018-01-10-133658.png" width="220" />
<img src="https://github.com/EugeneHoran/WorkoutJournal/blob/master/images/device-2018-01-10-133741.png" width="220"  />
<img src="https://github.com/EugeneHoran/WorkoutJournal/blob/master/images/device-2018-01-11-160702.png" width="220"  />
<img src="https://github.com/EugeneHoran/WorkoutJournal/blob/master/images/device-2018-01-10-133421.png" width="220" />
<img src="https://github.com/EugeneHoran/WorkoutJournal/blob/master/images/device-2018-01-10-133833.png" width="220" />
<img src="https://github.com/EugeneHoran/WorkoutJournal/blob/master/images/device-2018-01-10-133920.png" width="220"  />
</p>


### Libraries

```
    // Support Libraries
    implementation "com.android.support:support-v4:${rootProject.ext.supportLibVersion}"
    implementation "com.android.support:appcompat-v7:${rootProject.ext.supportLibVersion}"
    implementation "com.android.support:support-v4:${rootProject.ext.supportLibVersion}"
    implementation "com.android.support:support-vector-drawable:${rootProject.ext.supportLibVersion}"
    implementation "com.android.support:design:${rootProject.ext.supportLibVersion}"
    implementation "com.android.support:recyclerview-v7:${rootProject.ext.supportLibVersion}"
    implementation "com.android.support:cardview-v7:${rootProject.ext.supportLibVersion}"
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    // Caldroid
    implementation "com.roomorama:caldroid:${rootProject.ext.caldroidVersion}"
    // Architecture Components
    implementation "android.arch.lifecycle:extensions:${rootProject.ext.archLibVersion}"
    implementation "android.arch.persistence.room:runtime:${rootProject.ext.archLibVersion}"
    annotationProcessor "android.arch.lifecycle:compiler:${rootProject.ext.archLibVersion}"
    annotationProcessor "android.arch.persistence.room:compiler:${rootProject.ext.archLibVersion}"

```
