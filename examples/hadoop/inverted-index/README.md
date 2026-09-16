
#Building and running Hadoop MapReduce jobs

##To build jar file manually:


We assume that hadoop is in your PATH.

```
hadoop com.sun.tools.javac.Main InvertedIndex.java
jar cfe inverted-index.jar InvertedIndex InvertedIndex*.class
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
hadoop jar inverted-index.jar input output
hdfs dfs -get output
```


Sample output
=============

See below for a snippet from the output file (that is very large so we are showing a few sample
lines).

...
adversary	Les-Miserables.txt, Les-Miserables.txt, Les-Miserables.txt, Complete-Shakespeare.txt, Complete-Shakespeare.txt, Complete-Shakespeare.txt, Complete-Shakespeare.txt, Complete-Shakespeare.txt, Complete-Shakespeare.txt, Complete-Shakespeare.txt, Complete-Shakespeare.txt, Complete-Shakespeare.txt, Patrick-Henry.txt, Encyclopaedia.txt, Encyclopaedia.txt, Encyclopaedia.txt, Encyclopaedia.txt, Encyclopaedia.txt, Encyclopaedia.txt
...
critically	Gift-of-the-Magi.txt, Encyclopaedia.txt
...
wonderland	Through-the-Looking-Glass.txt, Alice-in-Wonderland.txt, Alice-in-Wonderland.txt, Alice-in-Wonderland.txt, Alice-in-Wonderland.txt
...
yell	Complete-Shakespeare.txt, Complete-Shakespeare.txt, Complete-Shakespeare.txt, Tom-Sawyer-Abroad.txt, Tom-Sawyer-Abroad.txt, Tom-Sawyer-Abroad.txt, Tom-Sawyer-Abroad.txt, Les-Miserables.txt
...

