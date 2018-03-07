import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class PageRankReducer extends Reducer <Text, Text, Text, Text> {
	
	private Text result = new Text();
	private final double d = 0.85;
	private double N;
	private String goals = new String();

	@Override
	public void reduce(Text page, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		double sum = 0;

		Configuration conf = context.getConfiguration();
		N = Double.parseDouble(conf.get("N"));

		// page   = key
		// values = valeurs associées à page

		for (Text val : values) {

			String valString = val.toString();
			
			if(valString.indexOf("	") >= 0) {
				// Sépare chaque valeur selon \t
				String[] line = valString.split("	");
				// Calcul de la somme qui dépend des voisins entrants vers page et des voisins sortant de chacun de ces voisins entrants
				sum += Double.parseDouble(line[1])/Double.parseDouble(line[2]);
			}
			else {
				goals = valString;
			}
		}
		// Calcul de la popularité de la page en cours
		double pi = (1-d)/N + d*sum;
		result.set(pi + "	" + goals);
		context.write(page, result);
	}
}
