package alg;

import java.awt.Rectangle;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.java.games.input.ControllerEnvironment;
import javax.swing.JButton;

public class Prototype_2 {

	private Thread cont;  //  @jve:decl-index=0:
	
	private JFrame applicationFrame = null;  //  @jve:decl-index=0:visual-constraint="174,41"

	private JPanel applicationPane = null;

	private JCheckBox toggle4 = null;

	private JCheckBox toggle5 = null;

	private JToggleButton butt4 = null;

	private JToggleButton butt5 = null;

	private JToggleButton butt6 = null;

	private JToggleButton butt1 = null;

	private JToggleButton butt2 = null;

	private JToggleButton butt3 = null;

	private JCheckBox toggle6 = null;

	private JCheckBox toggle1 = null;

	private JCheckBox toggle2 = null;

	private JCheckBox toggle3 = null;
	
	private JToggleButton button[];
	private JCheckBox toggle[];
	private JToggleButton buttonB[];

	private JToggleButton butt6b = null;

	private JToggleButton butt5b = null;

	private JToggleButton butt4b = null;

	private JToggleButton butt3b = null;

	private JToggleButton butt2b = null;

	private JToggleButton butt1b = null;

	private JFrame OptionFrame = null;  //  @jve:decl-index=0:visual-constraint="432,339"

	private JPanel jContentPane = null;

	private JComboBox jComboBox1 = null;

	private JComboBox jComboBox = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JButton jButton = null;

	private JButton Ok = null;

	private JButton jButton1 = null;

	/**
	 * This method initializes applicationFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getApplicationFrame() {
		if (applicationFrame == null) {
			applicationFrame = new JFrame();
			applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			applicationFrame.setSize(380, 241);
			applicationFrame.setContentPane(getApplicationPane());
			applicationFrame.setTitle("Application");
			applicationFrame.setResizable(false);

			button = new JToggleButton[] {butt1,butt2,butt3,butt4,butt5,butt6};
			toggle = new JCheckBox[] {toggle1,toggle2,toggle3,toggle4,toggle5,toggle6};
			buttonB = new JToggleButton[] {butt1b,butt2b,butt3b,butt4b,butt5b,butt6b};

			for(int i=0;i<button.length;i++) {
				button[i].setFocusable(false);
				toggle[i].setFocusable(false);
				buttonB[i].setFocusable(false);
			}
			
			cont = new Cont(button,toggle);
			cont.start();
			
			getOptionFrame().setVisible(true);
		}
		return applicationFrame;
	}


	/**
	 * This method initializes applicationPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getApplicationPane() {
		if (applicationPane == null) {
			applicationPane = new JPanel();
			applicationPane.setLayout(null);
			applicationPane.add(getToggle4(), null);
			applicationPane.add(getToggle5(), null);
			applicationPane.add(getButt4(), null);
			applicationPane.add(getButt5(), null);
			applicationPane.add(getButt6(), null);
			applicationPane.add(getButt1(), null);
			applicationPane.add(getButt2(), null);
			applicationPane.add(getButt3(), null);
			applicationPane.add(getToggle6(), null);
			applicationPane.add(getToggle1(), null);
			applicationPane.add(getToggle2(), null);
			applicationPane.add(getToggle3(), null);
			applicationPane.add(getButt6b(), null);
			applicationPane.add(getButt5b(), null);
			applicationPane.add(getButt4b(), null);
			applicationPane.add(getButt3b(), null);
			applicationPane.add(getButt2b(), null);
			applicationPane.add(getButt1b(), null);
			applicationPane.add(getJButton1(), null);
		}
		return applicationPane;
	}

	/**
	 * This method initializes toggle4	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getToggle4() {
		if (toggle4 == null) {
			toggle4 = new JCheckBox();
			toggle4.setBounds(new Rectangle(15, 75, 76, 16));
			toggle4.setText("Toggle");
		}
		return toggle4;
	}

	/**
	 * This method initializes toggle5	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getToggle5() {
		if (toggle5 == null) {
			toggle5 = new JCheckBox();
			toggle5.setBounds(new Rectangle(150, 75, 76, 16));
			toggle5.setText("Toggle");
		}
		return toggle5;
	}

	/**
	 * This method initializes butt4	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt4() {
		if (butt4 == null) {
			butt4 = new JToggleButton();
			butt4.setBounds(new Rectangle(30, 15, 46, 46));
			butt4.setText("4");
		}
		return butt4;
	}

	/**
	 * This method initializes butt5	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt5() {
		if (butt5 == null) {
			butt5 = new JToggleButton();
			butt5.setBounds(new Rectangle(165, 15, 46, 46));
			butt5.setText("5");
		}
		return butt5;
	}

	/**
	 * This method initializes butt6	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt6() {
		if (butt6 == null) {
			butt6 = new JToggleButton();
			butt6.setBounds(new Rectangle(300, 15, 46, 46));
			butt6.setText("6");
		}
		return butt6;
	}

	/**
	 * This method initializes butt1	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt1() {
		if (butt1 == null) {
			butt1 = new JToggleButton();
			butt1.setBounds(new Rectangle(30, 120, 46, 46));
			butt1.setText("1");
		}
		return butt1;
	}

	/**
	 * This method initializes butt2	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt2() {
		if (butt2 == null) {
			butt2 = new JToggleButton();
			butt2.setBounds(new Rectangle(165, 120, 46, 46));
			butt2.setText("2");
		}
		return butt2;
	}

	/**
	 * This method initializes butt3	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt3() {
		if (butt3 == null) {
			butt3 = new JToggleButton();
			butt3.setBounds(new Rectangle(300, 120, 46, 46));
			butt3.setText("3");
		}
		return butt3;
	}

	/**
	 * This method initializes toggle6	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getToggle6() {
		if (toggle6 == null) {
			toggle6 = new JCheckBox();
			toggle6.setBounds(new Rectangle(285, 75, 76, 16));
			toggle6.setText("Toggle");
		}
		return toggle6;
	}

	/**
	 * This method initializes toggle1	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getToggle1() {
		if (toggle1 == null) {
			toggle1 = new JCheckBox();
			toggle1.setBounds(new Rectangle(15, 180, 76, 16));
			toggle1.setText("Toggle");
		}
		return toggle1;
	}

	/**
	 * This method initializes toggle2	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getToggle2() {
		if (toggle2 == null) {
			toggle2 = new JCheckBox();
			toggle2.setBounds(new Rectangle(150, 180, 76, 16));
			toggle2.setText("Toggle");
		}
		return toggle2;
	}

	/**
	 * This method initializes toggle3	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getToggle3() {
		if (toggle3 == null) {
			toggle3 = new JCheckBox();
			toggle3.setBounds(new Rectangle(285, 180, 76, 16));
			toggle3.setText("Toggle");
		}
		return toggle3;
	}

	/**
	 * This method initializes butt6b	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt6b() {
		if (butt6b == null) {
			butt6b = new JToggleButton();
			butt6b.setBounds(new Rectangle(295, 10, 56, 56));
		}
		return butt6b;
	}


	/**
	 * This method initializes butt5b	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt5b() {
		if (butt5b == null) {
			butt5b = new JToggleButton();
			butt5b.setBounds(new Rectangle(160, 10, 56, 56));
		}
		return butt5b;
	}


	/**
	 * This method initializes butt4b	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt4b() {
		if (butt4b == null) {
			butt4b = new JToggleButton();
			butt4b.setBounds(new Rectangle(25, 10, 56, 56));
		}
		return butt4b;
	}


	/**
	 * This method initializes butt3b	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt3b() {
		if (butt3b == null) {
			butt3b = new JToggleButton();
			butt3b.setBounds(new Rectangle(295, 115, 56, 56));
		}
		return butt3b;
	}


	/**
	 * This method initializes butt2b	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt2b() {
		if (butt2b == null) {
			butt2b = new JToggleButton();
			butt2b.setBounds(new Rectangle(160, 115, 56, 56));
		}
		return butt2b;
	}


	/**
	 * This method initializes butt1b	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	private JToggleButton getButt1b() {
		if (butt1b == null) {
			butt1b = new JToggleButton();
			butt1b.setBounds(new Rectangle(25, 115, 56, 56));
		}
		return butt1b;
	}


	/**
	 * This method initializes OptionFrame	
	 * 	
	 * @return javax.swing.JFrame	
	 */
	private JFrame getOptionFrame() {
		if (OptionFrame == null) {
			OptionFrame = new JFrame();
			OptionFrame.setSize(new Dimension(380, 240));
			OptionFrame.setTitle("Options");
			OptionFrame.setContentPane(getJContentPane());
		}
		return OptionFrame;
	}


	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(75, 90, 181, 16));
			jLabel1.setText("Midi device");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(75, 15, 181, 16));
			jLabel.setText("Controller");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJComboBox1(), null);
			jContentPane.add(getJComboBox(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getOk(), null);
		}
		return jContentPane;
	}


	/**
	 * This method initializes jComboBox1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBox1() {
		if (jComboBox1 == null) {
			jComboBox1 = new JComboBox();
			jComboBox1.setBounds(new Rectangle(75, 105, 226, 22));
			net.java.games.input.Controller[] cs = 
				ControllerEnvironment.getDefaultEnvironment().getControllers();
			
			jComboBox1.addItem(""+cs.length);
			
////			for(int i=0;i<cs.length;i++)
////				jComboBox1.addItem(cs[i].getName());
		}
		return jComboBox1;
	}


	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.setBounds(new Rectangle(75, 30, 226, 22));
			jComboBox.addItem("USB Joystick, stick");
		}
		return jComboBox;
	}


	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(90, 165, 91, 31));
			jButton.setText("Cancel");
		}
		return jButton;
	}


	/**
	 * This method initializes Ok	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOk() {
		if (Ok == null) {
			Ok = new JButton();
			Ok.setBounds(new Rectangle(195, 165, 91, 31));
			Ok.setText("Ok");
		}
		return Ok;
	}


	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(345, 180, 30, 30));
		}
		return jButton1;
	}


	/**
	 * Launches this application
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Prototype_2 application = new Prototype_2();
				application.getApplicationFrame().setVisible(true);
			}
		});
		

		
		
	}

}
