#!/bin/sh

if [ "$1" == "-h" ] || [ "$1" == "--help" ]; then
	echo "Generate a Google chart apis URL. You need to specify a result file." 
	exit 
fi


if [ -r "$1" ]; then
	F=`cat "$1"`
else
	F=`cat`
fi

RUNTIMES=` echo "$F" | awk '{if (NR==1)print $1}' `

MAX=`echo "$F" | awk 'BEGIN{FS=","; max=0} 
		{if (NR != 1){
			if ($2 > max) max = $2;
		  	if ($3 > max) max = $3;
			if ($4 > max) max = $4;
		 }}
		END { print max} '`
DATAMAX=$MAX
if [ $MAX -gt 4000 ]; then
	DATAMAX=4000
fi

DATA=` echo "$F" | awk -v max="$MAX" -v datamax="$DATAMAX" -F "," '
  BEGIN {scale=max/datamax;}
  { if(NR != 1) { 
      line1=line1","int($2/scale);
	  line2=line2","int($3/scale);
	  line3=line3","int($4/scale)}
  } 
  END{
    printf "%s|%s|%s", substr(line1,2),substr(line2,2),substr(line3,2)
  }'`

echo "http://chart.apis.google.com/chart?cht=lc&chtt=Total+time+for+$RUNTIMES+times&chs=500x440&chxt=y&chxr=0,0,$MAX&chds=0,$DATAMAX&chd=t:$DATA&chdl=V1(volatile)|V2(Synchronized)|V3(AtomicLong)&chco=FF0000,00FF00,0000FF"
