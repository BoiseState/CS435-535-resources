#!/usr/bin/env python
# coding: utf-8

# The **wordcount** problem for multiple files using Spark in Python. This also shows how to
# interface with Hadoop HDFS.

import findspark
import pyspark
import sys
from pyspark.sql import SparkSession


if (len(sys.argv) != 3):
    print("Usage: wordcount.py <input folder> <output folder>")
    sys.exit(1)

spark = SparkSession.builder.master('local[*]').appName("wordcount").getOrCreate()
sc = spark.sparkContext


# We only need the following five steps if we are running on Google colab. Uncomment these
# lines on Google colab.

#from google.colab import files
#uploaded = files.upload()
#%mkdir input
#%mv *.txt input

#allFiles = sc.textFile('hdfs://cscluster00.boisestate.edu:9000/user/amit/input/*.txt')
allFiles = sc.textFile(sys.argv[1])


counts = allFiles.flatMap(lambda line:line.split(" ")).map(lambda word:(word,1)).reduceByKey(lambda x,y: x+y)

# Save in HDFS
#counts.saveAsTextFile("hdfs://cscluster00.boisestate.edu:9000/user/amit/output")

counts.saveAsTextFile(sys.argv[2]) 


# We only take the first 10 as the whole list is very long.

output = counts.take(10)

for (word, count) in output:
    print('%s %i' % (word, count))

