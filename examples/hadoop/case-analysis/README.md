
#Building and running Hadoop MapReduce jobs

##To build jar file manually:

```
hadoop com.sun.tools.javac.Main CaseAnalysis.java
jar cfe case-analysis.jar CaseAnalysis CaseAnalysis*.class
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
the following steps. See Hadoop wiki for more info here:
(http://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html)

```
hdfs  dfs -put input
hadoop jar case-analysis.jar input output
hdfs dfs -get output
```

If you created the jar file with Eclipse (and did not set the main class), then run it as follows:

```
hadoop jar case-analysis.jar CaseAnalysis input output
```

