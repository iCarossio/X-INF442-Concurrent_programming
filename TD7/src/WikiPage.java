public class WikiPage {
	final String s;
	final String text;
		
	WikiPage(String contents)
	{
		s = contents.replace(' ','_');
		text = text();
	}
		
	String title()
	{
		int start = s.indexOf("<title>")+7;
		int end = s.indexOf("</title>",start);
		
		if (start == -1 || end == -1)
			return "";
		else
			return s.substring(start,end); 
	}
	
	private String text()
	{
		int start = s.indexOf("<text")+5;
		start = s.indexOf(">",start)+1;
		int end = s.indexOf("</text>",start);
		
		if (start == -1 || end == -1)
			return "";
		else
			return s.substring(start, end);
	}
		
	private int pos = 0;
	String link()
	{
		int start = text.indexOf("[[", pos);
		if (start == -1) return null; else start += 2;
		int end = text.indexOf("]", start);
		int pipe = text.indexOf("|", start);
		if (0 <= pipe && pipe < end) end = pipe;
		int sharp = text.indexOf("#", start);
		if (0 <= sharp && sharp < end) end = sharp;
		if (end == -1) return null;
		String page = text.substring(start,end);
		pos = end;
		if (page.equals("")) page = title(); else {
		if (page.indexOf(':') != -1 || page.indexOf('{') != -1 || page.indexOf('\n') != -1) return link();}
		return page;
	}
}
