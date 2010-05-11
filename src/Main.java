import java.util.Iterator;
import java.util.List;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ModuleLoader.getInstance().loadModules();
		List<iModule> modules = ModuleLoader.getInstance().getListModules();
		if(!modules.isEmpty())
		{
			Iterator<iModule> i = modules.listIterator();
			while (i.hasNext()){
				i.next().run();
			}
		}
	}
}
