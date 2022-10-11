package at.srfg.iasset.repository.model.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Path implements Iterable<String> {
//	private final String DELIMITER = "/";
	private static final Pattern pattern = Pattern.compile("(([a-zA-Z_0-9])+)");
	
	private final List<String> pathToken;
	public Path(String path) {
		pathToken = pathTokens(path);
	}
	public String getFirst() {
		if (! pathToken.isEmpty()) {
			return pathToken.get(0);
		}
		throw new IllegalStateException("No valid path provided!");
	}
	public String getLast() {
		if (! pathToken.isEmpty()) {
			return pathToken.get(pathToken.size()-1);
		}
		throw new IllegalStateException("No valid path provided!");
	}
	public boolean isLast(String token) {
		return token.equals(getLast());
	}
	public Iterator<String> iterator() {
		return pathToken.iterator();
	}
	private List<String> pathTokens(String path) {
		List<String> token = new ArrayList<>();
		Matcher m = pattern.matcher(path);
		while (m.find()) {
			token.add(m.group());
			
		}
		return token;
	}
	public static void main(String[]args) {
		Path p = new Path("first.second[2].fourth");
		
		p.forEach(new Consumer<String>() {

			@Override
			public void accept(String t) {
				System.out.println(t);
				
			}
		});
	}
}