#!/bin/sh

RUNTIMES=10000000

if [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
	echo "Run a benchmark. You can specify run times by giving a numberic parameter, the default value is $RUNTIMES." 
	exit 
fi

if [ "$#" -ge 1 ]; then
	RUNTIMES="$1"
fi

echo "$RUNTIMES"

for idx in `seq 10`;
do
	java -cp ./bin me.xiping.volitileverify.SystemTimerUserThreads "$RUNTIMES" | awk -F ":" '{r=r","$2} END{print r}'
	sleep 1
done
