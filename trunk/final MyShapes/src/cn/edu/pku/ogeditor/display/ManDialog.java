package cn.edu.pku.ogeditor.display;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

class ManDialog implements ActionListener {
	JDialog jd;
	JLabel Jno, Jvalue, Jat;
	JTextField Fno = new JTextField(10), Fvalue = new JTextField(5),
			Fat = new JTextField(5);
	JButton jb = new JButton("OK"), jexit = new JButton("Exit");
	int manner, no_;
	// manner 1:setMan 2:button 3:menu 4:delete
	int num = -1, atNo = -1;
	private OWLOntologyManager ontManager;

	ManDialog(JFrame jf, int m, int n_, OWLOntologyManager ontManager) {
		ViewStatusShell.notifyPause();
		this.ontManager = ontManager;
		manner = m;
		no_ = n_;
		if (manner == 1)
			jd = new JDialog(jf, "Set person's attribute", true);
		else if (manner == 2 || manner == 3)
			jd = new JDialog(jf, "Add persons", true);
		else if (manner == 4)
			jd = new JDialog(jf, "Delete persons", true);
		jd.getContentPane().setLayout(new FlowLayout());
		Jno = new JLabel("Please input the person's name:");
		jd.getContentPane().add(Jno);
		if (manner == 2)
			Fno.setText(ViewStatusShell.manName[no_]);
		Fno.setHorizontalAlignment(JTextField.CENTER);
		Fno.addActionListener(this);
		jd.getContentPane().add(Fno);
		Jat = new JLabel("Please input the attribute's name:");
		jd.getContentPane().add(Jat);
		Fat.setText("");
		Fat.setHorizontalAlignment(JTextField.CENTER);
		Fat.addActionListener(this);
		jd.getContentPane().add(Fat);
		Jvalue = new JLabel("Please input the person's value:");
		jd.getContentPane().add(Jvalue);
		if (manner == 4) {
			Fat.setEditable(false);
			Fvalue.setEditable(false);
		}
		Fvalue.setHorizontalAlignment(JTextField.CENTER);
		Fvalue.addActionListener(this);
		jd.getContentPane().add(Fvalue);
		jb.addActionListener(this);
		jd.getContentPane().add(jb);
		jexit.addActionListener(this);
		jd.getContentPane().add(jexit);
		jd.setResizable(false);
		jd.setSize(300, 170);
		jd.setLocation(450, 250);
		jd.setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == jexit) {
			jd.dispose();
			ViewStatusShell.isExit = true;
			ViewStatusShell.notifyContinue();
			// System.out.println("exit");
			return;
		}

		String e = Fno.getText();
		String v = Fvalue.getText();
		String p = Fat.getText();

		OWLOntology ont = ontManager.getOntology(IRI
				.create("http://www.owl-ontologies.com/Ontology_IOT.owl"));
		if (null == ont)
			return;

		OWLDataFactory dfac = ontManager.getOWLDataFactory();
		IRI docIRI = ontManager.getOntologyDocumentIRI(ont);
		String prefix = ont.getOntologyID().getOntologyIRI() + "#";
		System.out.println("\ndocIRI: " + docIRI + "\nprefix: " + prefix);

		OWLNamedIndividual ind = dfac.getOWLNamedIndividual(IRI.create(prefix
				+ e));
		OWLDataProperty prop = dfac.getOWLDataProperty(IRI.create(prefix + p));

		if (null == ind || null == prop) {
			JOptionPane.showMessageDialog(ViewStatusShell.instance,
					"No such device or property", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
		}

		Set<OWLLiteral> origin = ind.getDataPropertyValues(prop, ont);
//		if (origin.isEmpty()) {
//			JOptionPane.showMessageDialog(ViewStatusShell.instance,
//					"No such property of this device", "Invalid Input",
//					JOptionPane.ERROR_MESSAGE);
//		}

		Iterator<OWLLiteral> iter = origin.iterator();

		OWLLiteral ptype = iter.next();
//		OWLDatatype ptype = prop.;

		if (ptype.isBoolean()) {
			Boolean li = Boolean.valueOf(v);
			ontManager.applyChange(new AddAxiom(ont, dfac
					.getOWLDataPropertyAssertionAxiom(prop, ind, li)));
		}
		else if (ptype.isDouble()) {
			double li = Double.valueOf(v);
			ontManager.applyChange(new AddAxiom(ont, dfac
					.getOWLDataPropertyAssertionAxiom(prop, ind, li)));
		}
		else if (ptype.isFloat()) {
			float li = Float.valueOf(v);
			ontManager.applyChange(new AddAxiom(ont, dfac
					.getOWLDataPropertyAssertionAxiom(prop, ind, li)));
		}
		else if (ptype.isInteger()) {
			int li = Integer.valueOf(v);
			ontManager.applyChange(new AddAxiom(ont, dfac
					.getOWLDataPropertyAssertionAxiom(prop, ind, li)));
		}
		else
		{
			JOptionPane.showMessageDialog(ViewStatusShell.instance,
					"No such value", "Invalid Input",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	//
	// public void actionPerformed(ActionEvent event)
	// {
	// if (event.getSource() == jexit)
	// {
	// jd.dispose();
	// ViewStatusShell.isExit = true;
	// ViewStatusShell.notifyContinue();
	// //System.out.println("exit");
	// return;
	// }
	// ViewStatusShell.isExit = false;
	// String t = new String(),name = new String(),atName = new String();
	// boolean flag = true;
	// int temp = 0;
	// try
	// {
	// temp = ViewStatusShell.num_man;
	// for(int i = 0; i < 6; i++)
	// if(!ViewStatusShell.toggleM[i])
	// {
	// temp++;
	// }
	// name = Fno.getText();
	// if(manner == 1 || manner == 4)
	// num = searchn(name);
	// else if(manner == 2)
	// {
	// num = no_;
	// }
	// else if(manner == 3)
	// {
	// num = temp - 1;
	// }
	// //num = Integer.parseInt(Fno.getText());
	// t = Fvalue.getText();
	// atName = Fat.getText();
	// if(num >= 0)
	// atNo = searcha(atName);
	// //atNo = Integer.parseInt(Fat.getText());
	// }
	// catch (NumberFormatException nfe)
	// {
	// JOptionPane.showMessageDialog(ViewStatusShell.instance,
	// "You must enter integers", "Invalid Input",
	// JOptionPane.ERROR_MESSAGE);
	// flag = false;
	// }
	// /*int temp = Main.num_man;
	// for(int i = 0; i < 6; i++)
	// if(!Main.toggleM[i])
	// {
	// temp++;
	// }
	// if (num <= 0 || num > temp)
	// JOptionPane.showMessageDialog(Main.instance, "Invalid input !",
	// "Warning", JOptionPane.WARNING_MESSAGE);
	// else*/
	// //System.out.println(dup(name)+" "+num);
	// if(manner != 4 && atName.length() * t.length() == 0)
	// JOptionPane.showMessageDialog(ViewStatusShell.instance, "invalid name !",
	// "Warning", JOptionPane.WARNING_MESSAGE);
	// else if(manner == 1 && atNo == -1 && num != -1 )
	// {
	// ViewStatusShell.at[0][num].name[ViewStatusShell.at[0][num].num] = atName;
	// ViewStatusShell.at[0][num].value[ViewStatusShell.at[0][num].num] = t;
	// ViewStatusShell.at[0][num].type[ViewStatusShell.at[0][num].num] =
	// t.length();
	// ViewStatusShell.at[0][num].num++;
	// ViewStatusShell.getInfo_man();
	// ViewStatusShell.outstream();
	// jb.setEnabled(false);
	// jd.dispose();
	// ViewStatusShell.notifyContinue();
	// return ;
	// }
	// else if(manner != 2 && manner != 3 &&(num == -1 || atNo == -1) &&
	// !(manner == 4 && num != -1))
	// JOptionPane.showMessageDialog(ViewStatusShell.instance, "invalid name !",
	// "Warning", JOptionPane.WARNING_MESSAGE);
	// else if((manner == 2 || manner == 3 ) && dup(name) > 0)
	// JOptionPane.showMessageDialog(ViewStatusShell.instance,
	// "The same name !",
	// "Warning", JOptionPane.WARNING_MESSAGE);
	// else if (flag)
	// {
	// if(manner == 4)
	// {
	// if(num < 6)
	// {
	// JOptionPane.showMessageDialog( ViewStatusShell.instance,
	// "You cannot delete the graph by the menu!",
	// "Warning", JOptionPane.WARNING_MESSAGE );
	// }
	// else
	// {
	// for(int i = num; i + 1 < temp; i++)
	// {
	// ViewStatusShell.manName[i] = ViewStatusShell.manName[i + 1];
	// ViewStatusShell.at[0][i] = ViewStatusShell.at[0][i + 1];
	// }
	// ViewStatusShell.extra_num_man--;
	// ViewStatusShell.num_man--;
	// ViewStatusShell.totalNum--;
	// if(ViewStatusShell.dragNum[0] > 0)
	// {
	// ViewStatusShell.dragNum[0]--;
	// ViewStatusShell.extraM[ViewStatusShell.dragNum[0]].setVisible(false);
	// }
	// }
	// }
	// else if(manner == 2)
	// {
	// ViewStatusShell.Bman[no_].setIcon(ViewStatusShell.Iman);
	// ViewStatusShell.toggleM[no_] = !ViewStatusShell.toggleM[no_];
	// ViewStatusShell.num_man++;
	// ViewStatusShell.totalNum++;
	// ViewStatusShell.at[0][no_].num = 1;
	// ViewStatusShell.at[0][no_].name[0] = atName;
	// ViewStatusShell.at[0][no_].value[0] = t;
	// ViewStatusShell.at[0][no_].dName = name;
	// ViewStatusShell.manName[no_] = name;
	// ViewStatusShell.at[0][no_].type[0] = t.length();
	// ViewStatusShell.manName[num] = name;
	// }
	// else if(manner == 1)
	// {
	// ViewStatusShell.at[0][num].value[atNo] = t;
	// }
	// else if(manner == 3)
	// {
	// ViewStatusShell.extra_num_man++;
	// ViewStatusShell.num_man++;
	// ViewStatusShell.totalNum++;
	// int tmp = ViewStatusShell.num_man + 5;
	// for(int i = 0; i < 6; i++)
	// if(ViewStatusShell.toggleM[i])
	// tmp--;
	// ViewStatusShell.at[0][tmp].num++;
	// ViewStatusShell.at[0][tmp].name[0] = atName;
	// ViewStatusShell.at[0][tmp].value[0] = t;
	// ViewStatusShell.at[0][tmp].dName = name;
	// ViewStatusShell.manName[tmp] = name;
	// ViewStatusShell.at[0][tmp].type[0] = t.length();
	//
	// //Main.manName[num] = name;
	// }
	// ViewStatusShell.display_man.setText("there are(is) " +
	// ViewStatusShell.num_man + " people\n");
	// ViewStatusShell.extra_man.setText("  *" + ViewStatusShell.extra_num_man +
	// "\n");
	// ViewStatusShell.getInfo_man();
	// ViewStatusShell.outstream();
	// jb.setEnabled(false);
	// jd.dispose();
	// ViewStatusShell.notifyContinue();
	// return ;
	// }
	// //System.out.println("qw");
	// }
	private int dup(String name) {
		int i, temp = 0, s = 0;
		temp = ViewStatusShell.num_man;
		for (i = 0; i < 6; i++)
			if (!ViewStatusShell.toggleM[i]) {
				temp++;
			}
		for (i = 0; i < temp; i++) {
			if (i < 6 && !ViewStatusShell.toggleM[i])
				continue;
			if (ViewStatusShell.manName[i].equals(name))
				s++;
		}
		return s;
	}

	private int searcha(String atName) {
		int i;
		for (i = 0; i < ViewStatusShell.at[0][num].num; i++) {
			if (ViewStatusShell.at[0][num].name[i].equals(atName))
				break;
		}
		if (i == ViewStatusShell.at[0][num].num)
			return -1;
		else
			return i;
	}

	private int searchn(String name) {
		int i, temp = 0;
		temp = ViewStatusShell.num_man;
		for (i = 0; i < 6; i++)
			if (!ViewStatusShell.toggleM[i]) {
				temp++;
			}
		for (i = 0; i < temp; i++) {
			if (ViewStatusShell.manName[i].equals(name))
				break;
		}
		if (i == temp)
			return -1;
		else
			return i;
	}
}