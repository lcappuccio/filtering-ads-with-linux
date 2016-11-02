# How to filter (some) advertising with linux and dnsmasq

[![Build Status](https://travis-ci.org/lcappuccio/filtering-ads-with-linux.svg?branch=master)](https://travis-ci.org/lcappuccio/filtering-ads-with-linux)
[![codecov](https://codecov.io/gh/lcappuccio/filtering-ads-with-linux/branch/master/graph/badge.svg)](https://codecov.io/gh/lcappuccio/filtering-ads-with-linux)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b6cf8eb58c5c441eaaa52a79b6f91bf7)](https://www.codacy.com/app/lcappuccio/filtering-ads-with-linux?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lcappuccio/filtering-ads-with-linux&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/420cede9-dd38-4629-ac71-0d377143553b)](https://codebeat.co/projects/github-com-lcappuccio-filtering-ads-with-linux)

A brief recap about how to filter (some) ads with a linux box. This will not remove all ads but can greatly help if used with AdBlock, uBlockOrigin, Ghostery, etc.

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

## Configure local webserver

Will not get much into detail here, simply install lighttpd, apache, pixelserv or whatever other web server of your choice. Personally I'm using lighttpd.

On the `lighttpd.conf` file remember to set:

`server.error-handler-404    = "/1px.gif"`

`index-file.names            = ( "1px.gif" )`

This will forward requests to http://127.0.0.1 directly to our 1px gif.

## Create the lists

Now for the fun part. Actually I'm not creating anything, just putting together pieces that I've found on the web. Inspired by a post about [pihole](https://pi-hole.net) I started to gather information and create a DIY solution to address the same problem:

* A [post](https://www.reddit.com/r/pihole/comments/4p2tp7/adding_easylist_and_other_adblocklike_sources_to/) on reddit
* Look for google *dnsmasq block ads*

The entrypoint is `manual_easylist.sh`. This bash script will download the lists in `lists.lst`, parse them and put them into another file.

If lighttpd is not running on your local machine I replace the `127.0.0.1` on the end of the script with the ip of my ad trap system.

On this same file I'm appending some other hosts found on [URLBlacklist](http://www.urlblacklist.com). After all this parsing and appending the script will sort and remove duplicates.

Finally we will generate `blacklist_dnsmasq.txt`. Copy this file to `/etc/dnsmasq.d` and again restart dnsmasq.

Start browsing and issue
`> sudo tail -f /var/log/dnsmasq.log | grep 127.0.0.1`

And you should see something like this:

`Oct  4 19:40:38 dnsmasq[9669]: config pagead2.googlesyndication.com is 127.0.0.1`

`Oct  4 19:40:38 dnsmasq[9669]: config s7.addthis.com is 127.0.0.1`

So we are redirecting some stuff. :)

## Extras

* If using DHCP I've prepared a php page listing all leases
* Bandwidthd monitor (`sudo apt-get install bandwidthd`)

## ToDo

* Investigate this: `cat /var/log/dnsmasq.log | awk '{print $4 $5 $6 $7 $8}'`
* Statistics Analysis Facility
* Integrate `logtailer` with `statsviewer`, the first will POST the string to the latter.
* Decide database to use on `statsviewer`. Best candidate: embedded H2. I WILL NOT code on java the aggregations to 
be performed if using a key value store. If there is a value in data relation then the database needs to be 
relational. Use a KV only if you do not really care or are interested in what you're saving. **If you expect a 
formatted JSON then your data and the relation in it has a value**
* How to handle HTTPS requests