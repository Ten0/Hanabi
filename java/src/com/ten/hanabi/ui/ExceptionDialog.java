package com.ten.hanabi.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * 
 * ExceptionDialog.
 * 
 * Displays an exception stack trace in a panel south of the main dialog area.
 * 
 * 
 * @author Oliver Watkins (c)
 */
public class ExceptionDialog extends JDialog {

	private int dialogWidth = 500;
	private int dialogHeight = 140;

	private JLabel iconLabel = new JLabel();

	// is error panel opened up
	private boolean open = false;

	private JLabel errorLabel = new JLabel();
	private JTextArea errorTextArea = new JTextArea("");

	private JTextArea exceptionTextArea = new JTextArea("");
	private JScrollPane exceptionTextAreaSP = new JScrollPane();

	private JButton okButton = new JButton("OK");
	private JButton viewButton = new JButton("View Error");

	private JPanel topPanel = new JPanel(new BorderLayout());

	/**
	 * @wbp.parser.constructor
	 */
	public ExceptionDialog(Window parent, String errorLabelText, String errorDescription, Throwable e) {
		super(parent, JDialog.ModalityType.DOCUMENT_MODAL);
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));

		setSize(dialogWidth, dialogHeight);

		setResizable(false);

		errorTextArea.setText(errorDescription);

		errorLabel.setText(errorLabelText);

		exceptionTextArea.setText(errors.toString());

		exceptionTextAreaSP = new JScrollPane(exceptionTextArea);

		iconLabel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

		iconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
		setupUI();
		setLocationRelativeTo(parent);

		setUpListeners();
	}

	public ExceptionDialog(Window parent, String errorLabelText, Throwable e) {
		this(parent, errorLabelText, null, e);
	}

	public void setupUI() {

		this.setTitle("Error");

		errorTextArea.setLineWrap(true);
		errorTextArea.setWrapStyleWord(true);
		errorTextArea.setEditable(false);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(okButton);
		buttonPanel.add(viewButton);

		errorTextArea.setBackground(iconLabel.getBackground());

		JScrollPane textAreaSP = new JScrollPane(errorTextArea);

		textAreaSP.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
		errorLabel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
		exceptionTextArea.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		exceptionTextAreaSP.setPreferredSize(new Dimension(100, 100));

		topPanel.add(iconLabel, BorderLayout.WEST);

		JPanel p = new JPanel(new BorderLayout());
		p.add(errorLabel, BorderLayout.NORTH);
		p.add(textAreaSP);

		topPanel.add(p);

		getContentPane().add(topPanel, BorderLayout.NORTH);

		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	private void setUpListeners() {

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ExceptionDialog.this.setVisible(false);
			}
		});

		viewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(open) {
					viewButton.setText("View details");

					getContentPane().remove(exceptionTextAreaSP);

					ExceptionDialog.this.setSize(dialogWidth, dialogHeight);

					topPanel.revalidate();

					open = false;

				} else {

					viewButton.setText("Hide details");

					getContentPane().add(exceptionTextAreaSP, BorderLayout.CENTER);

					ExceptionDialog.this.setSize(dialogWidth, dialogHeight + 100);

					topPanel.revalidate();

					open = true;
				}
			}
		});

	}
}