package CreateBidibulProperties.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Model {

	private Properties prop;

	public Model(){
		prop = new Properties();
	}

	public String createFile(String name, String description, String author, String website) {
		if (!name.trim().equals("")) {
			prop.put("name", name.trim());
			prop.put("description", description.trim());
			prop.put("author", author.trim());
			prop.put("website", website.trim());

			String dir = null;
			OutputStream out;
			try {
				dir = System.getProperty("user.dir") + "/" + name + ".properties";
				out = new FileOutputStream(dir);
				prop.store(out, "Information sur le module " + name);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace(); return null;
			} catch (IOException e) {
				e.printStackTrace(); return null;
			}
			return dir;
		}
		return null;
	}

}
