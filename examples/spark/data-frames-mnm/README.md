### How to run the M&M Example
To run the Python code for this chapter:

```
  spark-submit --conf spark.log.level=WARN --master local[*]  mnmcount.py data/mnm_dataset.csv
```

To generate data set using the gen_mnm_dataset.py program as follows:

```
python gen_mnm_dataset.py <#rows>
```

This example is from "Learning Spark, 2nd ed, by Jules S. Damji, Brooke Wening, Tathagata Das, and
Denny Lee. Copyright 2020 Databricks, Inc., 978-1-492-05004-9.  It has been slightly modified.
