# How to filter (some) advertising with linux and dnsmasq
A brief recap about how to filter (some) ads with a linux box.

The idea is very simple and very well documented on the 'net.

Use dnsmasq to point a list of domains to a local webserver that will replace the ads with a 1px transparent gif.

To obtain this we will need:
* a linux machine or a raspberry (I'm using a Pi3)
* dnsmasq installed
* a local webserver (I'm using lighttpd)

Nice to have:
* dhcp on the same box
* some reporting or statistics
* logging