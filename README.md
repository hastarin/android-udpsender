android-udpsender
=================

[UDP Sender](https://play.google.com/store/apps/details?id=com.hastarin.android.udpsender) is a simple application to allow sending UDP packets.

![Landscape Screenshot](https://raw.github.com/hastarin/android-udpsender/screenshots/screenshots/N7Landscape.png)

It supports the implicit [Intent](http://developer.android.com/reference/android/content/Intent.html) <b>Intent.ACTION_SENDTO</b> with a <b>udp://host:port/data</b> format URI.  

* data is treated as hex if it starts with 0x.  
* If a string starting with 0x is to be sent it can be escaped as \0x

Tasker Integration
------------------
UDP packets can be sent via [Tasker](http://tasker.dinglisch.net/) with the Misc/Send Intent action.

Just set it up as follows:
* Action = android.intent.action.SENDTO
* Data = udp://host:port/data
* Target = Activity
 
![Tasker Intent](https://raw.github.com/hastarin/android-udpsender/screenshots/screenshots/TaskerIntent.png)

Example for a www.limitlessled.com Wi-Fi bridge at 192.168.1.100 to turn on all the white LEDs
* Action = android.intent.action.SENDTO
* Data = udp://192.168.1.100:50000/0x350055
* Target = Activity

Tasker Example Project
----------------------

![Lights Scene](https://raw.github.com/hastarin/android-udpsender/screenshots/screenshots/TaskerLightsScene.png)

You can get a copy of my current Tasker project (or at least a portion of it) [Lights.prj.xml](https://raw.github.com/hastarin/android-udpsender/screenshots/screenshots/Lights.prj.xml).

You'll need to edit it and insert your own Weather Underground API key instead of &lt;YOURAPIKEYHERE&gt;.
See http://www.wunderground.com/weather/api/d/docs for more info on getting one.

It's used by the "Get Sunrise/set" which you can call once a day to update the global variables.  I use this in "Check Night Time" to only turn my entry light on at night when I arrive home and connect to my Wi-Fi.

