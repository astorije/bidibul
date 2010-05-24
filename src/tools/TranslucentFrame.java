package tools;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.sun.jna.Platform;
import com.sun.jna.platform.WindowUtils;



/*
 *  Classe permettant de créer une fenêtre transparente
 *  // Pour désactiver les effets (mode dev) passer undecorated et windowTransparent à false
 *  ATTENTION : ne pas utiliser getContentPane() (par exemple, faire un this.update() au lieu de this.getContentPane().update)
 */


public class TranslucentFrame extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	private float alpha = 1.0f;
	private Boolean undecorated = true;
	private Boolean windowTransparent = true;



	public TranslucentFrame(GraphicsConfiguration gc) {
		super(gc);
		this.init();


	}

	public TranslucentFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		this.init();
	}

	public TranslucentFrame(String title) throws HeadlessException {
		super(title);
		this.init();
	}

	public TranslucentFrame() {
		super();
		this.init();
	}


	private void init() {
		this.setUndecorated(undecorated);
		System.setProperty("sun.java2d.noddraw", "true");
		System.setProperty("sun.java2d.opengl", "true");
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b && !Platform.isX11())
			WindowUtils.setWindowTransparent(this, windowTransparent);
	}

	@Override
	protected void addImpl(Component comp, Object constraints, int index) {
		super.addImpl(comp, constraints, index);
		if (comp instanceof JComponent) {
			JComponent jcomp = (JComponent) comp;
			jcomp.setOpaque(false);
		}
	}

	public void setAlpha(float alpha) {
		WindowUtils.setWindowAlpha(this, alpha);
	}

	public float getAlpha() {
		return alpha;
	}

}