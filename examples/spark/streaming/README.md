
Build
=====

Build the jar file directly in Eclipse (just include all Spark jar files from
~/spark-install/spark/jars as External jars for the project).

From the command line:

javac -proc:none -cp ".:/home/amit/spark-install/spark/jars/*" -d out JavaStructuredNetworkWordCount.java
jar cvf spark-streaming-wordcount.jar  -C out/ .

Run
===
Startup a streaming server with the following command (in a separate terminal):

nc -l 9999

Then startup the streaming listener in a separate terminal as follows:

spark-submit --master local[4] spark-streaming-wordcount.jar localhost 9999 2> log

or


spark-submit --master local[4] spark-streaming-wordcount.py 2> log

where local[4] says to use 4 threads on local machine. You can change that to higher or lower
or replace by * to use as many threads as the number of logical threads on your local system.

Now type some words in the terminal running nc. Make sure to hit enter after each line. Every 5
seconds, the listener will trigger and collect the data and run a streaming query.


