#!/usr/bin/env python3
"""SimpleApp.py"""
from pyspark.sql import SparkSession
import logging


spark = SparkSession.builder.appName("SimpleApp").getOrCreate()

logFile = "../simpleapp/README.md"  # Should be some file on your system
logData = spark.read.text(logFile).cache()

numAs = logData.filter(logData.value.contains('a')).count()
numBs = logData.filter(logData.value.contains('b')).count()

print("Lines with a: %i, lines with b: %i" % (numAs, numBs))

spark.stop()
