import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class SortReducer extends Reducer <FloatWritable, Text, FloatWritable, Text> {
	@Override
	public void reduce(FloatWritable rank, Iterable<Text> pages, Context context) throws IOException, InterruptedException {
		String res = new String();
		for (Text p:pages) {
			res += p.toString()+",";
		}
		res = res.substring(0, res.length() - 1);
		context.write(rank, new Text(res));
	}
}
