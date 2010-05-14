package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import models.Flash;

public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;

	public MainView() {
		this.setUndecorated(true);
		output();
		initialize();
	}

	public void initialize() {
		this.setLayout(null);

		JButton fermer = new JButton("Fermer");
		fermer.setBounds(20, 60, 70, 20);
		fermer.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
		this.add(fermer);

		// NotificationPanel
		NotificationPanel panNotification = new NotificationPanel(Flash.getInstance());
		panNotification.setPreferredSize(getMaximumSize());
		panNotification.setBounds(200, 100, 311, 133);
		this.add(panNotification);

		this.setSize(640, 480);
	}

	public void output() {
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
}
