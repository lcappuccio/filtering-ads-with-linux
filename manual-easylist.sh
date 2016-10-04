#!/bin/bash
blacklist=blacklist.txt

rm -rf blacklist*.txt

for source in `cat lists.lst`; do
    echo $source;
    curl --silent $source >> ads.txt
    echo -e "\t`wc -l ads.txt | cut -d " " -f 1` lines downloaded"
done

echo -e "\nFiltering non-url content..."
perl easylist.pl ads.txt > ads_parsed.txt
rm ads.txt
echo -e "\t`wc -l ads_parsed.txt | cut -d " " -f 1` lines after parsing"

# the args to add to the request to the yoyo server, to tell it that we want
# a hosts file and that we want to redirect to the addcatcher
temp_ads=tmp_ads.txt
listurlargs="hostformat=nohtml&showintro=0&mimetype=plaintext"
# URL of the ad server list to download
listurl="http://pgl.yoyo.org/adservers/serverlist.php?${listurlargs}"
# command to fetch the list (alternatives commented out)
fetchcmd="/usr/bin/wget -q -O $temp_ads $listurl"
# download and process
$fetchcmd
cat $temp_ads >> $blacklist
echo -e "\t`wc -l $temp_ads | cut -d " " -f 1` lines after parsing"
rm $temp_ads

cat domains_*.txt >> $blacklist

echo -e "\nRemoving duplicates..."
sort -u ads_parsed.txt > ads_unique.txt
rm ads_parsed.txt
echo -e "\t`wc -l ads_unique.txt | cut -d " " -f 1` lines after deduping"

cat ads_unique.txt >> $blacklist
sort -u $blacklist > blacklist.txt2
rm $blacklist
mv blacklist.txt2 $blacklist
rm ads_unique.txt

# read blacklist and convert to dnsmasq format
while IFS='' read -r line || [[ -n "$line" ]]; do
    echo "address=/$line/127.0.0.1" >> blacklist_dnsmasq.txt
done < $blacklist

echo "`wc -l blacklist_dnsmasq.txt` ad servers blacklisted"
