android-udpsender
=================

[UDP Sender](https://play.google.com/store/apps/details?id=com.hastarin.android.udpsender) is a simple application to allow sending UDP packets.

![Landscape Screenshot](https://raw.github.com/hastarin/android-udpsender/screenshots/screenshots/S4Landscape.png)

It supports the implicit [Intent](http://developer.android.com/reference/android/content/Intent.html) <b>Intent.ACTION_SENDTO</b> with a <b>udp://host:port/data</b> format URI.  

* data is treated as hex if it starts with 0x.  
* If a string starting with 0x is to be sent it can be escaped as \0x

<b>Added in v1.3</b> - Current in Beta see http://goo.gl/reibxw to join
* Added support for Tasker to do variable replacement for the host/port as well as the text.
* Remembers your settings when you quit and re-launch the app.

<b>Added in v1.2</b>
* Now supports multi line text messages.
* Bugfix for Send button not working in Landscape mode.

<b>Added in v1.1</b> 

* Also acts as a plugin for Locale and Tasker.  
* Variable replacement is supported for the Tasker plugin in the text field.
* Changed theme to Holo (Dark)
* Removed unused permissions
* Various tweaks/fixes


Tasker Integration
------------------
Tasker plugin support is now complete and should be straight forward to use.

![Tasker Plugin](https://raw.github.com/hastarin/android-udpsender/screenshots/screenshots/TaskerPlugin.png)


Alternatively UDP packets can be sent via [Tasker](http://tasker.dinglisch.net/) with the Misc/Send Intent action.

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

You can get a copy of my current Tasker project (or at least a portion of it) [Lights.prj.xml](https://raw.github.com/hastarin/android-udpsender/screenshots/screenshots/Lights.prj.xml) that I'm using for controlling my lights.

To give you an idea of how I'm using it I have the following:
* Gesture control via my launcher (Nova) to turn all lights on/off/full bright/night mode and dim/brighten lights.
* Using the Scene shown above I can turn all/individual zones on/off and control bright/dim/warm/cool.
* NOTE: A swipe up/down in the colored square controls a bright/dim ramp and left/right for warm/cool.
* An NFC tag on my bed that when tapped will turn the lights off, or keep some in night mode if I have guests.
* When my phone connects to the Wi-Fi, and it's between sunrise and sunset, my entry hall light is turned on.


<b>NOTE:</b> *It should be obvious but you will need to edit this to use it*

* Insert your own Weather Underground API key instead of &lt;YOURAPIKEYHERE&gt;.

See http://www.wunderground.com/weather/api/d/docs for more info on getting one.

It's used by the "Get Sunrise/set" which is called once a day to update the global variables.  I use this in "Check Night Time" to only turn my entry light on at night when I arrive home and connect to my Wi-Fi.

* You'll want to replace the string for my SSID with your own.  Search and replace C86241.  Or remove the Stop commands that prevent things working when not on the correct Wi-Fi.

* You'll obviously need to change IPs and Names to suit your setup.

* Finally if you're using a device that has a different size screen (my S4 is 1920x1080) you'll need to edit the scene so it fits and adjust the maths for the gestures.

