
#Building and running Hadoop MapReduce jobs

This folder has the wc.jar file pre-generated for you but see below on how to
generate it if you need to (like for your own programs)

##To build jar file manually:

We assume that hadoop is in your PATH.
```
hadoop com.sun.tools.javac.Main WordCount.java
jar cfe ../wc.jaa WordCount WordCount*.class
rm -f *.class
```

##To build jar file in Eclipse:

Create a normal Java project and add the following external jar files (adjusting paths to match your
paths):

```
~/hadoop-install/hadoop-3.5.0/share/hadoop/common/hadoop-common-3.5.0.jar
~/hadoop-install/hadoop-3.5.0/share/hadoop/mapreduce/hadoop-mapreduce-client-core-3.5.0.jar
~/hadoop-install/hadoop-3.5.0/share/hadoop/hdfs/lib/commons-cli-1.9.jar
```

Then export project as jar file and you should be set.

##To run:

Make sure you have HDFS running either in standalone or pseudo-distributed mode before doing
the following steps. See class notes for more details.

```
hdfs  dfs -put input
hadoop jar wc.jar input output
hdfs dfs -get output
```


