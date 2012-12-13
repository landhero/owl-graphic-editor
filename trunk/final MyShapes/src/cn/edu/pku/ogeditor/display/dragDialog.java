package cn.edu.pku.ogeditor.display;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class dragDialog implements ActionListener
{
	JDialog jd;
	JLabel Jkind, Jno;
	JTextField Fkind = new JTextField(10), Fno = new JTextField(5);
	JButton jb = new JButton("OK"), jexit = new JButton("Exit");
	dragDialog(JFrame jf)
	{
		jd = new JDialog(jf, "Set the item to drag");
		jd.getContentPane().setLayout(new FlowLayout());
		Jkind = new JLabel("Please input the kind of item:");
		jd.getContentPane().add(Jkind);
		Fkind.addActionListener( this );
		jd.getContentPane().add(Fkind);
		Jno = new JLabel("Please input the number of item:");
		jd.getContentPane().add(Jno);
		Fno.addActionListener( this );
		jd.getContentPane().add(Fno);
		jb.addActionListener(this);
		jd.getContentPane().add(jb);
		jexit.addActionListener(this);
		jd.getContentPane().add(jexit);
		jd.setResizable(false);
		jd.setSize(300, 150);
		jd.setLocation(450, 250);
		jd.setVisible(true);
	}
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == jexit)
		{
			jd.dispose();
			return;
		}
		String sk = new String(), sn = new String();
		int dragn = 0;
		try
		{
			sk = Fkind.getText();
			if(sk.equals("Light"))
				ViewStatusShell.dragKind = 1;
			else if(sk.equals("Person"))
				ViewStatusShell.dragKind = 0;
			else if(sk.equals("Projector"))
				ViewStatusShell.dragKind = 2;
			else if(sk.equals("Screen"))
				ViewStatusShell.dragKind = 3;
			else if(sk.equals("Air_Condition"))
				ViewStatusShell.dragKind = 4;
			else
			{
				JOptionPane.showMessageDialog(ViewStatusShell.instance, "invalid name !",
						"Warning", JOptionPane.WARNING_MESSAGE);
				ViewStatusShell.dragKind = -1;
				ViewStatusShell.dragNo = -1;				
				return ;
			}
			sn = Fno.getText();
			dragn = Integer.parseInt(sn);
			ViewStatusShell.dragNo = dragn;
		}
		catch (NumberFormatException nfe)
		{
			JOptionPane.showMessageDialog(ViewStatusShell.instance,
					"You must enter integers", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
			ViewStatusShell.dragKind = -1;
			ViewStatusShell.dragNo = -1;
			return ;
		}
		ViewStatusShell.dragNo--;
		if( ViewStatusShell.dragKind > 4 || ViewStatusShell.dragKind < 0 || ViewStatusShell.dragNo < 0 || (ViewStatusShell.dragKind == 0 && ViewStatusShell.dragNo >= 6)
				|| (ViewStatusShell.dragKind == 1 && ViewStatusShell.dragNo >= 5) || (ViewStatusShell.dragKind > 1 && ViewStatusShell.dragNo > 0))
		{
			JOptionPane.showMessageDialog(ViewStatusShell.instance, "invalid name !",
					"Warning", JOptionPane.WARNING_MESSAGE);
			ViewStatusShell.dragKind = -1;
			ViewStatusShell.dragNo = -1;				
			return ;			
		}
        jb.setEnabled(false);
        jd.dispose();
        return ;
	}

}
