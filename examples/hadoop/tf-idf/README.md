
#Building and running Hadoop MapReduce jobs

##To build jar file manually:


We assume that hadoop is in your PATH.

```
hadoop com.sun.tools.javac.Main TfIdfDriver.java *.java
jar cfe tf-idf.jar TfIdfDriver *.class
```

##To build jar file in Eclipse:

Create a normal Java project and add the following external jar files (adjusting paths to match
your paths):

```
~/hadoop-install/hadoop-3.5.0/share/hadoop/common/hadoop-common-3.5.0.jar
~/hadoop-install/hadoop-3.5.0/share/hadoop/mapreduce/hadoop-mapreduce-client-core-3.5.0.jar
~/hadoop-install/hadoop-3.5.0/share/hadoop/hdfs/lib/commons-cli-1.9.jar
~/hadoop-install/hadoop-3.5.0/share/hadoop/common/lib/slf4j-api-1.7.36.jar
```

Then export project as jar file and you should be set.

##To run:

Make sure you have HDFS running either in standalone or pseudo-distributed mode before doing
the following steps. See class notes for more details.

```
hdfs  dfs -put ../word-count/input
hadoop jar tf-idf.jar input output
hdfs dfs -get output
```


