# LocationAlarm-App-Locatozoid-

# **Location Alarm App Using Geo Location API**

# **ABSTRACT**

Many times, we get into situation like we have to turn our mobile silent or we need to be notified about the location through alarm.
Location Alarm App is an android based system that is to do that job. This app triggers an alarm or make mobile device silent in those 
positions where the user wants with highest precision in both offline and online mode.


# **INTRODUCTION**

  1. **Background**

This app is mainly used as a tool to make silent or raise and alarm for its user on his/her designated locations according to user defined range. Sometimes we have to make silent/alarm our mobile device in many places as we are restricted to use mobile device in those places. This app will help its user to do that job. This app does this job very exquisitely. This kind of is rarely been developed and we plan to upload this app on Google Play Store.

  1. **Purpose**

This app is a tool application to notify the user with alarm or make device silent when the user enters his/her predesignated location and specified range. When a user needs to be notified, the app from background notify the user with an alarm or simply make phone silent.

  1. **Applicability**

Suppose the user needs to be notified when he is in the designated region, the app will send and notification and trigger an alarm/silent device. This app will be very useful for different kind of events.


# **SURVEY OF TECHNOLOGIES**

**2.1 Survey**

In today&#39;s technological world everybody is looking for the on-hand smart and perfect technological solution to satisfy their day to day requirements. To design and develop the Mobile Apps, there is necessity to make a survey of the tools and technologies used and which are popular in the world of mobile apps developers.

Possible tools to develop our applications:

**Android Studio** – This is an IDE created by community named &quot;IntelliJIDEA &quot;used as platform for developing Mobile Apps based on Android Technology. The IntelliJIDEA, Android based studio provides massive amount of plugins created by different communities.

**Eclipse** – This IDE is used to develop the programs in Java and it also enables the developer to expand with other languages via plugins.

**SQLite** – SQLite is a C-language library that implements a small, fast, self-contained, high-reliability, full-featured, SQL database engine. SQLite is the most used database engine in the world. SQLite is built into all mobile phones and most computers and comes bundled inside countless other applications that people use every day.

**Geolocation API** – The geolocation api allows the user to provide their location to web applications if they so desire. For privacy reasons, the user is asked for permission to report location information. Application that wish to use the Geolocation object must add the &quot;geolocation&quot; permission to their manifest. The user&#39;s operating system will prompt the user to allow location access the first time it is requested.

Android Studio is official IDE for Android developer which was provided by Google. So, we can have latest support for updating or any official change from Google. Using Android Studio is better than Eclipse for layout. Anyone can code the layout and see the preview.With build.gradle we can easily access share library of others on github. We also can avoid some silly bugs with code analyzer tool of Android Studio (without add any plug-in).

On the other hand, Owned by Google, Geolocation API is a complete package of products that allows to build web and mobile apps, improve the app with real time location services.The Geolocation API returns a location and accuracy radius based on information about cell towers and WiFi nodes that the mobile client can detect. Even it can get location data from satellite. This document describes the protocol used to send this data to the server and to return a response to the client.

We have used SQLite to store the values of a location. SQLite is used so that we can handle our database in offline as SQLite saves all data in the device storage. Through our data model class we have saved our all values that is needed.

Based on the above discussion, we selected Android Studio, SQLite for our application and used GeoLocation API to as our wish.


# **REQUIREMENT AND ANALYSIS**

**3.1 Problem Definition**

The problem is to get the latitude, longitude, location address from the touched location in map and save it with a range in database. In a background thread, there is a process all time running to check if the user is inside any of the saved locations range. If the user is inside the range of any location, the application will alarm/silent the device as the user wanted.
Then that location is disabled and the background thread starts to process again to check if the user is again inside any saved locations.


# **SYSTEM DESIGN**

**4.1 Procedural Design**

The procedure is depicted nicely in fig 4.1. It shows how the app works and stores data.

![](RackMultipart20200730-4-u5w5c7_html_cd19908f64cb246f.png)

Fig 4.1: Flowchart of procedure

**4.2 Algorithms Design**

Here is the proposed algorithm to detect if the user is inside the designated location. The app loads data from the SQLite database and stores it in an ArrayList. An infinite loop in a separate background thread always iterate through the ArrayList to check if the user is inside the location range or not. If the user is inside any location, the app gives him an alarm or silent the phone as the user required

**4.3 Security Issues**

The app has a database of SQLite. The SQLite database is locked and can&#39;t be accessed by anybody. But if the app is uninstalled, the data will be lost. As it is a tool application, there is not much of a security issue.


# **IMPLEMENTATION AND TESTING**

**5.1 Implementation Approach**

After getting the realtime location from google geolocation API, the data is then passed to the location detection iterative process running in background thread to check if the user is inside the location or not. If the user is inside the location the app triggers an alarm or silent the app as the user required.

**5.2 Testing Approach**

The application is tested if it works without data connection or not. It is also tested

When the phone is on airplane mode. In both without data and cellular connection, the alarm system works just fine and perfectly alarms the user or silent the device.


# **RESULTS AND DISCUSSION**

**6.1 User Documentation**

First of all the user needs to install the app. Then the map view with users current location appears. On the map view, the user can add a location by touching in the app. When the user taps or touch a location, a pop up opens to entry data with a range.

![](RackMultipart20200730-4-u5w5c7_html_419b292b16c2fd25.jpg) ![](RackMultipart20200730-4-u5w5c7_html_5f6e30a046355ce9.jpg)

Fig 6.1: Map view with user Fig 6.2: Pop up to entry

location and saved locations encircled. location in database.

When the user entry the data in the app,the data is saved in the SQlite database.

This data are showed in a view where the user can see the location,delete the location and decide if he/she wants to enable the data or keep it disabled.

The user can also choose if he/she wants to get and an alarm or simply wants to silent the device.

![](RackMultipart20200730-4-u5w5c7_html_989413c58d83ee2f.jpg) ![](RackMultipart20200730-4-u5w5c7_html_da6af047d44cffce.jpg)

Fig 6.3: Data from database Fig 6.4 Data from database in

in Silent mode. Alarm mode.

When the user will enter in the desired location that he/she saved on database, the

app will invoke a notification and an alarm when the app is in Alarm mode. If the app is in silent mode, the app will simply turn the device into silent mode.

After the alarm has been triggered, the specified location data will be disabled.

It had to be turned on again to get the alarm or silent service.

![](RackMultipart20200730-4-u5w5c7_html_82afc7e14a88cdee.jpg)

Fig 6.5: Alarm view


# **CONCLUSION**

**7.1 Conclusion**

This application is a tool app like we have an alarm app on our devices. This app will be much more helpful to people who have to be alarmed on locations or have their device to be in silent mode. As the app works just fine in offline mode and runs in background, it is very much of an efficient app.

**7.2 Limitations of the System**

This app has some limitations like it needs data connection enabled to take input of a place as the geolocation API gives us the address of our touched location. Sometimes in offline mode and without data connection, it takes time to get the users current location from GPS service.

**7.3 Future Scope of the Project**

This app can turn into user based system, where user can store there location data on online database as backup. This offline database can be loaded into the phone as a repository or as a recovery mode if the app is deleted.

This applications UI design can be modified to give user much more comfort.

A snooze system can be implemented to the alarm/silent service as the users don&#39;t have to enable the data every time. We think that thus application have a good market value in google app store.

# **REFERENCES**

1. One of the unique features of mobile applications is location awareness. Mobile users take their devices with them everywhere, and adding location awareness to your app offers users a more contextual experience. The location APIs available in Google Play services facilitate adding location awareness to your app with automated location tracking, geofencing, andactivityrecognition.at:[https://developer.android.com/training/location](https://developer.android.com/training/location) [Accessed 12 December 2019].
2. SQLite is an in-process library that implements a self-contained, serverless, zero-configuration, transactional SQL database engine. The code for SQLite is in the public domain and is thus free for use for any purpose, commercial or private. SQLite is the most widely deployed database in the world with more applications than we can count, including several high-profile projects. Available at: [https://www.sqlite.org/index.html](https://www.sqlite.org/index.html) [Accessed 12 December 2018].
