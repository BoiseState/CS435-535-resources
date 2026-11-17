#!/usr/bin/env python
# coding: utf-8

# Structured streaming wordcount example


import findspark
import pyspark
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder.master('local[*]').appName("Tutorial-1").getOrCreate()
sc = spark.sparkContext

lines = (spark
        .readStream.format("socket")
        .option("host", "localhost")
        .option("port", 9999)
        .load())

words = lines.select(split(col("value"), "\\s").alias("word"))
counts = words.groupBy("word").count()
checkpointDir = "checkpoint"

streamingQuery = (counts
                .writeStream
                .format("console")
                .outputMode("complete")
                .trigger(processingTime="5 second")
                .option("checkpointLocation", checkpointDir)
                .start())

streamingQuery.awaitTermination()

sc.stop();
