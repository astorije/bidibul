import java.util.Iterator;
import java.util.List;

import utils.BidibulModule;
import views.MainFrame;

public class Main {
	public static void main(String[] args) {
		new MainFrame();
		
		ModuleLoader.getInstance().loadModules();
		List<BidibulModule> modules = ModuleLoader.getInstance().getListModules();
		if(!modules.isEmpty())
		{
			Iterator<BidibulModule> i = modules.listIterator();
			while (i.hasNext()){
				i.next().run();
			}
		}
	}
}
