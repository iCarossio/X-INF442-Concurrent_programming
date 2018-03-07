import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class SortMapper extends Mapper<LongWritable,Text,FloatWritable,Text> {
	
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] curr = value.toString().split("	");
		context.write(new FloatWritable(Float.parseFloat(curr[1])), new Text(curr[0]));
	}
}
