import views.LoaderFrame;

public class Main {
	public static void main(String[] args) {
		new LoaderFrame();
/*
		ModuleLoader.getInstance().loadModules();
		List<BidibulModule> modules = ModuleLoader.getInstance().getListModules();
		if(!modules.isEmpty())
		{
			Iterator<BidibulModule> i = modules.listIterator();
			while (i.hasNext()){
				System.out.println(i.next().getName());
			}
		}
*/
	}

}
