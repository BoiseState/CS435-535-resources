
Run
===

spark-submit --master local[4] SimpleApp.py

where local[4] says to use 4 threads on local machine. You can change that to higher or lower
or replace by * to use as many threads as the number of logical threads on your local system.

There are a lot of info messages. You can redirect them to a file as follows:

spark-submit --master local[4] SimpleApp.py  2> log

You can suppress the messages with a command line opiton as follows:

spark-submit --conf spark.log.level=WARN --master local[4] SimpleApp.py

Or you can control the logging level from your program. See ../simpleapp/SimpleApp2.java for an example. 

You can also configure Spark defaults by creating a spark/conf/log4j.properties file. This is
a bit more complex but would make more sense in a production environment.


We have also provided a Jupyter notebook to run the same example. Just open the notebook and follow
along!
