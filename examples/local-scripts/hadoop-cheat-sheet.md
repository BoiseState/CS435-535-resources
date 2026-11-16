## Restart master services (cscluster00):

Run as s_ajain

sudo systemctl restart hadoop-namenode hadoop-secondarynamenode hadoop-resourcemanager hadoop-historyserver

## Restart worker services (cscluster01–cscluster08):

Run as s_ajain

for node in cscluster0{1..8}; do
  ssh -t s_ajain@$node "sudo systemctl restart hadoop-datanode hadoop-nodemanager"
done

## View live service logs (journalctl):

sudo journalctl -u hadoop-namenode -f

## View cluster health anytime:

hdfs dfsadmin -report
yarn node -list
