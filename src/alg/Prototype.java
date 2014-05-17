package alg;

import java.awt.Rectangle;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Prototype {

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
			
//			cont = new Cont(new JToggleButton[] {butt1,butt2,butt3,butt4,butt5,butt6},
//							new JCheckBox[] {toggle1,toggle2,toggle3,toggle4,toggle5,toggle6});

			cont = new Cont(button,toggle);
			cont.start();
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
				Prototype application = new Prototype();
				application.getApplicationFrame().setVisible(true);
			}
		});
		

		
		
	}

}
