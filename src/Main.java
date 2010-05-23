import views.LoaderFrame;

public class Main {
	public static void main(String[] args) {
		/*
		try {
			UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName()
			);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		*/
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
