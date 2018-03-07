
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

// source\tXXX\tbut1,but2,but3

public class PageRankMapper extends Mapper<LongWritable,Text,Text,Text> {
	
	private Text word = new Text();
	private Text val = new Text();
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		// Spliter l'entrée courante selon \t
		String[] curr = value.toString().split("	");
		
		if (curr.length > 2) { // Si la page actuelle contient bien des buts
			// Séparer les buts entre eux
			StringTokenizer currValues = new StringTokenizer(curr[2], ",");
			int currValuesCount = currValues.countTokens();
			
			// Clé finale
			String currKey = curr[0] + "	" + curr[1] + "	" + currValuesCount;
			val.set(currKey);
	
			 while (currValues.hasMoreTokens()) {
				 word.set(currValues.nextToken());
			     context.write(word, val);
			 }

			 // Enregistre également pour chaque entrée visitée la source (comme clé) suivie de toutes ses destinations (comme valeur)
			 context.write(new Text(curr[0]), new Text(curr[2]));
		 }
	}
}