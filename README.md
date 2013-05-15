android-udpsender
=================

Simple application to allow sending UDP packets.  Supports Intent.ACTION_SENDTO with a udp://host:port/data format URI.  data is treated as hex if it starts with 0x.  If a string starting with 0x is to be sent it can be escaped as \0x
