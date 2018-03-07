import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class DecodeReducer extends Reducer <Text, Text, Text, Text> {
	@Override
	public void reduce(Text page, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String res = "1.0	";
		for (Text p:values) {
			res += p.toString()+",";
		}
		res = res.substring(0, res.length() - 1);
		context.write(page, new Text(res));
		context.getCounter(MyCounters.NumberOfPages).increment(1);
	}
}
