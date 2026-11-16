import pyspark
from pyspark.storagelevel import StorageLevel
from pyspark.sql import (
    functions as F, 
    types as T,
    SparkSession,
)

spark = (
    SparkSession.builder
    # Spark Connect on EMR
    .remote('sc://localhost:15002')
    # Local Spark session
    # .master("local")
    .appName("Spark Demo")
    .getOrCreate()
)

(
    spark.read
    # Refers to the local file system
    .text("file:///home/arjunshukla/teaching/shakespeare-dataset/text")
    .createOrReplaceTempView("willy")
)

# Word count for the William Shakespeare dataset
(
    spark.table("will")
    .withColumn("words", F.split(F.col("value"), r'\s+'))
    .select(F.explode("words").alias("word"))
    .withColumn(
        "new_word",
        # we don't want things like capitalization or punctuation to 
        # affect the word count.
        F.lower(F.regexp_replace("word", r"[^A-Za-z]", "")),
    )
    .filter(F.col("new_word") != "")
    .groupby("new_word")
    .agg(F.count("*").alias("frequency"))
    .show(truncate=False)
)

# Question: I want the total length of all the words from each
# line that start with the letter "A"

# Method 1: Explode and reaggregate. This one is the least efficient
# since it has to use an aggregation.
(
    spark.table("willy")
    .withColumn("words", F.split(F.col("value"), r'\s+'))
    # We need a key to aggregate back on, so we can create one with 
    # monotonically_increasing_id
    .withColumn("row_id", F.monotonically_increasing_id())
    .select(
        "row_id",
        F.explode("words").alias("word"),
    )
    .withColumn("word", F.lower(F.regexp_replace("word", r"[^A-Za-z]", "")))
    .filter(F.startswith("word", F.lit("a")))
    .groupby("row_id")
    .agg(F.sum(F.length("word")).alias("total_a_length"))
    .show(truncate=False)
)

# Method 2: UDF. 
import re

def a_lengths(line):
    words = [re.sub(r"[^A-Za-z]", "", w) for w in line.lower().split()]
    return sum(len(w) for w in words if w.startswith('a'))

spark.udf.register("a_lengths", a_lengths, T.IntegerType())
a_lengths_udf = F.udf(a_lengths, T.IntegerType())

(
    spark.table("willy")
    # You can either use a registered or unregistered UDF
    .withColumn("total_a_length_registered", F.expr("a_lengths(value)"))
    .withColumn("total_a_length_unregistered", a_lengths_udf("value")) 
    .show(truncate=False)
)

# Method 3: Functional programming. This one is the most efficient, since it
# runs with pure Spark and does not require an aggregation.
(
    spark.table("willy")
    .withColumn("words", F.split(F.lower("value"), r'\s+'))
    .drop("value")
    # Strip each word without having to explode. "transform" is the Spark 
    # equivalent for "map" on an array
    .withColumn("stripped", F.expr("transform(words, x -> regexp_replace(x, '[^A-Za-z]', ''))"))
    .withColumn("a_words", F.expr("filter(stripped, x -> startswith(x, 'a'))"))
    # Aggregate the length of each word starting with "A". "aggregate" is the 
    # Spark equivalent of "fold" in other languages
    .withColumn("a_word_length", F.expr("aggregate(a_words, 0, (x, y) -> x + length(y))"))
    .show(10, truncate=False, vertical=True)
)