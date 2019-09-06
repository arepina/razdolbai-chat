#!/usr/bin/env bash
file="ports.txt"

var1=48999
while [ $var1 -ge 48654 ]
do
echo $var1 >> $file
var1=$[ $var1 - 1 ]
done
