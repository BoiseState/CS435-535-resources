#!/bin/bash

echo "setting up staging folders in DFS"

hdfs dfs -mkdir /tmp
hdfs dfs -chmod 1777 /tmp
hdfs dfs -mkdir /tmp/hadoop-yarn
hdfs dfs -chmod 777 /tmp/hadoop-yarn
hdfs dfs -mkdir /tmp/hadoop-yarn/staging
hdfs dfs -chmod 777 /tmp/hadoop-yarn/staging
hdfs dfs -mkdir /tmp/hadoop-yarn/staging/history
hdfs dfs -chmod 777 /tmp/hadoop-yarn/staging/history
hdfs dfs -mkdir /tmp/logs
hdfs dfs -chmod -R 1777 /tmp/logs

