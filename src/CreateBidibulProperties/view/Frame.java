package CreateBidibulProperties.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import CreateBidibulProperties.model.Model;

public class Frame extends JFrame{
	private static final long serialVersionUID = 1L;

	private Model model;

	private final JLabel instruction_label = new JLabel("Veuillez complèter les champs suivants et cliquer sur \"Créer le fichier\".");
	private final JLabel name_label = new JLabel("Nom du module *");
	private JTextField name_field;
	private final JLabel description_label = new JLabel("Description du module");
	private JTextArea description_field;
	private final JLabel author_label = new JLabel("Créateur du module");
	private JTextField author_field;
	private final JLabel website_label = new JLabel("Site Internet");
	private JTextField website_field;
	private JButton create_button = new JButton("Créer le fichier");
	private Dialog result = new Dialog();

	public Frame(){
		super();

		model = new Model();

		this.setSize(500, 300);
		this.setTitle("Bidibul - Création du fichier .properties");
		setLayout(new GridBagLayout());


		// Instructions
		GridBagConstraints instruction_label_g = new GridBagConstraints();
		instruction_label_g.fill = GridBagConstraints.CENTER;
		instruction_label_g.gridx= 0;
		instruction_label_g.gridy= 0;
		instruction_label_g.gridwidth = 2;
		instruction_label_g.ipady = 50;
		add(instruction_label,instruction_label_g);

		// Name
		GridBagConstraints name_label_g = new GridBagConstraints();
		name_label_g.fill = GridBagConstraints.HORIZONTAL;
		name_label_g.gridx= 0;
		name_label_g.gridy= 1;
		name_label_g.weightx = 0.2;
		add(name_label,name_label_g);

		name_field = new JTextField();
		GridBagConstraints name_field_g = new GridBagConstraints();
		name_field_g.fill = GridBagConstraints.HORIZONTAL;
		name_field_g.gridx= 1;
		name_field_g.gridy= 1;
		name_field_g.weightx = 0.8;
		add(name_field,name_field_g);

		// Description
		GridBagConstraints description_label_g = new GridBagConstraints();
		description_label_g.fill = GridBagConstraints.HORIZONTAL;
		description_label_g.gridx= 0;
		description_label_g.gridy= 2;
		description_label_g.weightx = 0.2;
		add(description_label,description_label_g);

		description_field = new JTextArea(4,20);
		description_field.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(description_field,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(new Rectangle(0, 0, 300, 48));
		GridBagConstraints description_field_g = new GridBagConstraints();
		description_field_g.fill = GridBagConstraints.HORIZONTAL;
		description_field_g.gridx= 1;
		description_field_g.gridy= 2;
		description_field_g.weightx = 0.8;
		add(scrollPane,description_field_g);

		// Author
		GridBagConstraints author_label_g = new GridBagConstraints();
		author_label_g.fill = GridBagConstraints.HORIZONTAL;
		author_label_g.gridx= 0;
		author_label_g.gridy= 3;
		author_label_g.weightx = 0.2;
		add(author_label,author_label_g);

		author_field = new JTextField();
		GridBagConstraints author_field_g = new GridBagConstraints();
		author_field_g.fill = GridBagConstraints.HORIZONTAL;
		author_field_g.gridx= 1;
		author_field_g.gridy= 3;
		author_field_g.weightx = 0.8;
		add(author_field,author_field_g);

		// Website
		GridBagConstraints website_label_g = new GridBagConstraints();
		website_label_g.fill = GridBagConstraints.HORIZONTAL;
		website_label_g.gridx= 0;
		website_label_g.gridy= 4;
		website_label_g.weightx = 0.2;
		add(website_label,website_label_g);

		website_field = new JTextField();
		GridBagConstraints website_field_g = new GridBagConstraints();
		website_field_g.fill = GridBagConstraints.HORIZONTAL;
		website_field_g.gridx= 1;
		website_field_g.gridy= 4;
		website_field_g.weightx = 0.8;
		add(website_field,website_field_g);


		// Button
		create_button.setMaximumSize(new Dimension(10, 10));
		GridBagConstraints create_button_g = new GridBagConstraints();
		create_button_g.fill = GridBagConstraints.CENTER;
		create_button_g.gridx= 0;
		create_button_g.gridy= 6;
		create_button_g.gridwidth = 2;
		add(create_button,create_button_g);

		create_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createFile();
			}
		});

	}

	private void createFile() {
		String res = model.createFile(name_field.getText(), description_field.getText(), author_field.getText(), website_field.getText());
		if ( res != null)
			res = "\nLe fichier a été créé avec succès ("+res+").\n\n"+"Veuillez l'intégrer au .jar de votre module.";
		else
			res = "\nLa création du fichier a échoué.\n\nVérifiez que vous avez indiqué le nom du module.";
		result.setText(res);
		result.setVisible(true);
	}
}
