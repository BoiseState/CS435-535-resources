import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * This is an example Hadoop Map/Reduce application. It reads the text input
 * files, breaks each line into character and counts the number of capital and
 * lowercase letters. The output is a locally sorted list of letters and the
 * count of how often they occurred.
 * 
 * To run: hadoop jar case-analysis.jar input output
 * 
 */
public class CaseAnalysis
{
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>
	{
		private final static IntWritable one = new IntWritable(1);
		private final static IntWritable zero = new IntWritable(0);
		private Text word = new Text();

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();

			Thread.sleep(15000);
			for (int i = 0; i < line.length(); i++) {
				if (Character.isLowerCase(line.charAt(i))) {
					word.set(String.valueOf(line.charAt(i)).toUpperCase());
					context.write(word, zero);
				} else if (Character.isUpperCase(line.charAt(i))) {
					word.set(String.valueOf(line.charAt(i)));
					context.write(word, one);
				} else {
					word.set("other");
					context.write(word, one);
				}
			}
		}
	}

	public static class Reduce extends Reducer<Text, IntWritable, Text, Text>
	{
		private Text result = new Text();

		public void reduce(Text key, Iterable<IntWritable> values, Context context)
		        throws IOException, InterruptedException {
			long total = 0;
			int upper = 0;

			for (IntWritable val : values) {
				upper += val.get();
				total++;
			}
			result.set(String.format("%16d %16d %16d %16.2f", total, upper, (total - upper), ((double) upper / total)));
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		if (otherArgs.length != 2) {
			System.err.println("Usage: hadoop jar <jar file> <input folder> <output folder>");
			System.exit(1);
		}

		Job job = Job.getInstance(conf, "case analysis");
		job.setJarByClass(CaseAnalysis.class);

		job.setMapperClass(Map.class);
		// we don't want a Combiner here: why?
		job.setReducerClass(Reduce.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
