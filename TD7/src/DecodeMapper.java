import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class DecodeMapper extends Mapper<LongWritable,Text,Text,Text> {
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		WikiPage curr = new WikiPage(value.toString());
		String currLink = curr.link();

		while(currLink != null) {
			context.write(new Text(curr.title()), new Text(currLink));
			currLink = curr.link();
		}
	}
}
