# How to filter (some) advertising with linux and dnsmasq
A brief recap about how to filter (some) ads with a linux box.

The idea is very simple and very well documented on the 'net:

*Use dnsmasq to point a list of domains to a local webserver that will replace the ads with a 1px transparent gif.*

To obtain this we will need:
* a linux machine or a raspberry (I'm using a Pi3)
* dnsmasq installed
* a local webserver (I'm using lighttpd)

Nice to have:
* dhcp on the same box
* some reporting or statistics
* logging

## Installing dnsmasq

This is basically a matter of typing:

`sudo apt-get install dnsmasq`

### Configuring dnsmasq

See `dnsmasq.conf` and `hosts`. On my example I'm also using this machine as DHCP server and resolving some local hostnames. I'm also using google's DNS.

Everything is on domain `home`. After installing and configuring:

`sudo service dnsmasq restart`

## Configure router

On my home modem/router I had to set the new dnsmasq machine as DNS. Remember to disable also the DHCP server, otherwise disable the DHCP on the dnsmasq machine.