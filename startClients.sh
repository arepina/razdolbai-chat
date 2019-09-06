#!/usr/bin/env bash
numOfIterations=$1
numOfIterations=$[ numOfIterations - 1 ]
currentIter=0
file="ports.txt"

for var in $(cat $file)
do
./clientInput.sh $var
./clientOutput.sh $var
sed -i '' "/$var/d" ${file}
if [ $numOfIterations -eq $currentIter ]
then
break
fi
currentIter=$[ $currentIter + 1 ]
done
