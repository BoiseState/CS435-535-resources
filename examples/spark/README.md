#Extra python packages

To use pyspark from Jupyter notebook or lab, you need to install the following packages:

 pip install pandas
 pip install matplotlib
 pip install seaborn
 pip install pyspark
 pip install findspark

#Using Google colabs

!pip install findspark
!pip install pyspark

##Reading local/drive files in Gooogle colab

from google.colab import files
uploaded = files.upload()
--> will display file chooser
then read file as usual in spark


##Reading files from GitHub

url = 'copied_raw_github_link'
df = pd.read_csv(url)


#Reading files from Google drive

from google.colab import drive
drive.mount('content/drive')
path_to_data = '/content/drive/My Drive/Data'
