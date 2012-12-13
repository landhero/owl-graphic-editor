package cn.edu.pku.ogeditor.display;
//MeetingRoom
//Author: Zhenyu Zhou
//2012.5

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.semanticweb.owlapi.model.OWLOntologyManager;

public class ViewStatusShell extends JFrame implements ActionListener, MouseListener, MouseMotionListener
{
	public static ViewStatusShell instance;

	final static boolean IsOpaque = true;
	// determine whether the JTextField is opaque
	public final static int MAX = 100;
	// largest number of equipments
	static JLabel back, Lman, Llight, Lpro, Lscr, LAC, Lother[] = new JLabel[20];
	static JPanel List;
	static JTextArea info_man, info_AC, info_pro, info_scr, info_light, 
	                 info_other[] = new JTextArea[20];
	static JButton Bexit = new JButton("Exit"), Breset = new JButton("Reset");
	static JButton Blight[], Bprojector, Bman[], Bair, Bscreen, Bother, BextraMan;
	static JButton setMan, setLight, setPro, setScr, setAC, setOther[] = new JButton[20];
	static JButton[] extraL = new JButton[MAX], extraP = new JButton[MAX], extraM = new JButton[MAX],
			 extraS = new JButton[MAX], extraA = new JButton[MAX];
	static Icon Ilight, Iprojector, Iman, Iair, Iscreen, Iblank, Iother;
	static Icon IAH, Iamp, ICP, Icom, IFL, IHWD, IRL;
	static boolean toggleL[], toggleP = false, toggleM[], toggleA = false,
			toggleS = false;
	static boolean optionMan = false, optionAC = false, optionLight = false,
			optionPro = false, optionScr = false;
	static boolean isDrag = false, isExit = true;
	//public static boolean isaddM = false, isaddL = false, isaddP = false, isaddA = false, isaddS = false;
	static int i, j, k;
	static int totalNum = 0, totalKind = 5, num_man = 0, extra_num_man = 0;
	static int temperature, clickTime = 0, Dkind;
	static int num_AC = 0, num_light = 0, num_pro = 0, num_scr = 0;
	// AC:air conditioner
	static int[] dragNum = new int[5]; 
	static JTextField display_man, extra_man, extra_AC, extra_light, extra_pro, extra_scr;
	static JComboBox optionEqu;
	static String names[] =
	/*{ "Default", "Light", "Projector", "Person", "Air Conditioner", "Screen",
			"Air Humidifier", "Amplifier", "Cell Phone", "Computer",
			"Front Light", "Hot Water Dispenser", "Rear Light", "None" };*/
		{ "Default", "Air Humidifier", "Amplifier", "Cell Phone", "Computer",
			 "Hot Water Dispenser", "None" };
	static String[] otherName = new String[20];
	static JMenu fileMenu = new JMenu("File"), actionMenu = new JMenu("Action"),
			helpMenu = new JMenu("Help"), addMenu = new JMenu("Increase"),
			subMenu = new JMenu("Decrease");
	static JMenuItem aboutItem = new JMenuItem("About..."), exitItem = new JMenuItem(
			"Exit"), helpItem = new JMenuItem("Help"), warningItem = new JMenuItem("Warning"),
			add_people = new JMenuItem("Add a person"),
			add_peoples = new JMenuItem("Add a group of people"),
			sub_people = new JMenuItem("Decrease a person"),
			sub_peoples = new JMenuItem("Decrease a group of people"),
			add_ACs = new JMenuItem("Add air conditioners"),
			sub_ACs = new JMenuItem("Decrease air conditioners"),
			add_lights = new JMenuItem("Add lights"), sub_lights = new JMenuItem(
					"Decrease lights"),
			add_pros = new JMenuItem("Add projectors"), sub_pros = new JMenuItem(
					"Decrease projectors"),
			add_scrs = new JMenuItem("Add screens"), sub_scrs = new JMenuItem(
					"Decrease screens"), checkT = new JMenuItem(
					"Check the temperature"),
			add_AC = new JMenuItem("Add an air conditioner"), sub_AC = new JMenuItem(
					"Decrease an air conditioner"),
			add_light = new JMenuItem("Add a light"), sub_light = new JMenuItem("Decrease a light"),
			add_pro = new JMenuItem("Add a projector"), sub_pro = new JMenuItem("Decrease a projector"),
			add_scr = new JMenuItem("Add a screen"), sub_scr = new JMenuItem("Decrease a screen"),
			drag = new JMenuItem("Drag items"), noDrag = new JMenuItem("Cancel drag model"),
			diary = new JMenuItem("Diary");
	static JMenuBar bar = new JMenuBar();
	static JPopupMenu jpm = new JPopupMenu();
	static JMenuItem jbp[] = new JMenuItem[10];
	static String[] manName, lightName, proName, scrName, ACName; 
	public static Attribute at[][]; // at most 20 devices
	// 0:people 1:light 2:projector 3:screen 4:AC else:other
/*	private class point
	{
		int x,y;
		void set(int xx, int yy)
		{x = xx; y = yy;}
	}
	static point Lborder[] = new point[5], Mborder[] = new point[6], Sborder, Pborder, Aborder;*/
	static int dragKind = -1, dragNo = -1;
	
	public static int bytes2i(byte[] buf4)
	{
		return buf4[3] << 24 | buf4[2] << 16 | buf4[1] << 8 | buf4[0];
	}

	private static byte[] mkBytes(byte[] buf4, int i)
	{
        buf4[0] = (byte) (0xff & i);
        buf4[1] = (byte) ((0xff00 & i) >> 8);
        buf4[2] = (byte) ((0xff0000 & i) >> 16);
        buf4[3] = (byte) ((0xff000000 & i) >> 24);
		return buf4;
	}
	static boolean FRL(int i,int j)
	{
		if(i != 1)
			return false;
		if(at[i][j].dName.length()<10)
		    return false;
		String s1 = "Rear_Light", s2 = "Front_Light";
		int ii = 0;
		for(ii = 0; ii < s1.length(); ii++)
			if(at[i][j].dName.charAt(ii) != s1.charAt(ii))
				break;
		if(ii == s1.length())
			return true;
		for(ii = 0; ii < s2.length(); ii++)
			if(at[i][j].dName.charAt(ii) != s2.charAt(ii))
				break;
		if(ii == s2.length())
			return true;
		return false;
	}

	public static void main(String[] args)
	{
//		ViewStatusShell shell = new ViewStatusShell("D:\\Program Files (x86)\\eclipse\\tmp/output.txt");
//		shell.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void outstream()
	{
		int i, j, k;
//		Socket s;
		try
		{
//			s = new Socket("127.0.0.1", 63392);
//			OutputStream os = s.getOutputStream();
			OutputStream os = new FileOutputStream("D:\\Program Files (x86)\\eclipse\\tmp/outStream.txt");
			byte[] buf4 = new byte[4];
			os.write(mkBytes(buf4, 2));//opcode
			os.write(mkBytes(buf4, totalNum));//total device number
			//System.out.println("totalNum:"+totalNum);
			at[0][0].dNum = num_man;
			at[1][0].dNum = num_light;
			at[2][0].dNum = num_pro;
			at[3][0].dNum = num_scr;
			at[4][0].dNum = num_AC;
			at[0][0].className = "Person";
			at[1][0].className = "Light";
			at[2][0].className = "Projector";
			at[3][0].className = "Screen";
			at[4][0].className = "Air_Condition";
			//System.out.println(manName[6]+"qwqweq");
			for(i=0;i<num_man + 6;i++)
				at[0][i].dName = manName[i];
			for(i=0;i<num_light + 5;i++)
				at[1][i].dName = lightName[i];
			for(i=0;i<num_pro + 1;i++)
				at[2][i].dName = proName[i];
			for(i=0;i<num_scr + 1;i++)
				at[3][i].dName = scrName[i];
			for(i=0;i<num_AC + 1;i++)
				at[4][i].dName = ACName[i];
			for (i = 0; i < totalKind; i++)//total device kind
			{
				int tmp = at[i][0].dNum;
				for (j = 0; j < tmp; j++)
				{
					if(i == 0)
					{
						if(j < 6 && !toggleM[j])
						{
							tmp++;
							continue;
						}
					}
					if(i == 1)
					{
						if(j < 5 && !toggleL[j])
						{
							tmp++;
							continue;
						}
					}
					if(i == 2)
					{
						if(j == 0 && !toggleP)
						{
							tmp++;
							continue;
						}
					}
					if(i == 3)
					{
						if(j == 0 && !toggleS)
						{
							tmp++;
							continue;
						}
					}
					if(i == 4)
					{
						if(j == 0 && !toggleA)
						{
							tmp++;
							continue;
						}
					}
					int Num = at[i][j].num + 2;
					os.write(mkBytes(buf4, Num));// number of attribute
					//System.out.println("atNum:"+at[i][j].num);
					String s1 = "class", s2 = "name",sc,sd;
					os.write(mkBytes(buf4, 5));
					if(FRL(i,j))//rear or front light
					{
						if(at[i][j].dName.charAt(0) == 'F')
							sc = "Front_Light";
						else 
							sc = "Rear_Light";
						sd = at[i][j].dName;
					}
					else
					{
						sc = at[i][0].className;
						sd = at[i][j].dName;
					}
					for(int qqq=0;qqq<s1.length();qqq++)
						os.write(s1.charAt(qqq));
					os.write(mkBytes(buf4, sc.length()));
					//System.out.println(at[i][0].className);
					for(int qqq=0;qqq<sc.length();qqq++)
						os.write(sc.charAt(qqq));
					//System.out.println(j);
					os.write(mkBytes(buf4, 4));
					for(int qqq=0;qqq<s2.length();qqq++)
						os.write(s2.charAt(qqq));
					//System.out.println()
					os.write(mkBytes(buf4, sd.length()));
					for(int qqq=0;qqq<sd.length();qqq++)
						os.write(sd.charAt(qqq));
					for (k = 0; k < at[i][j].num; k++)
					{		
						os.write(mkBytes(buf4, at[i][j].name[k].length()));
						//System.out.println("length:"+at[i][j].name[k].length());
						for(int qqq=0;qqq<at[i][j].name[k].length();qqq++)
							os.write(at[i][j].name[k].charAt(qqq));
						//System.out.println(at[i][j].name[k]);
						os.write(mkBytes(buf4, at[i][j].type[k]));
						//System.out.println(at[i][j].type[k]);
						String v = at[i][j].value[k];
						//System.out.println(v);
						switch(at[i][j].type[k])
						{
						   case -1:os.write( mkBytes(buf4,Integer.valueOf(v)) );break;
						   case -2:
							   long l = Double.doubleToLongBits(Double.valueOf(v));
						       os.write(mkBytes(buf4, (int) (l>>>32)));
						       os.write(mkBytes(buf4, (int) (l&0xff)));break;
						   case -3:
							   if(at[i][j].value[k].equals("true"))
								   os.write(mkBytes(buf4, 1));
							   else
								   os.write(mkBytes(buf4, 0));break;
						   case -4:
							   Float f = Float.valueOf(v);
							   //System.out.println(f+"zzz");
							   int temp = Float.floatToIntBits(f);
							   //System.out.println(temp+"zzw");
							   os.write(mkBytes(buf4, temp));break;
						   default:
							   String str = at[i][j].value[k];
							   //System.out.println(str);
							   for (int qqq=0;qqq<str.length();qqq++){
								   os.write(str.charAt(qqq));
							   }
							   break;
						}
					}
				}
			}
			//s.close();
			os.close();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return;
	}

	public static void init()
	{
		instance.repaint();
		for (i = 0; i < 5; i++)
			toggleL[i] = false;
		for (i = 0; i < 6; i++)
			toggleM[i] = false;
		toggleP = false;
		toggleA = false;
		toggleS = false;
		optionMan = false;
		optionAC = false;
		optionLight = false;
		optionPro = false;
		optionScr = false;
		num_man = 0;
		extra_num_man = 0;
		num_AC = 0;
		num_light = 0;
		num_pro = 0;
		num_scr = 0;
		totalNum = 0;
		totalKind = 5;
		for (i = 0; i < 5; i++)
		{
			at[i][0].reset();
		}
		for (i = 0; i < 20; i++)
		{
			Lother[i].setText("Default:");
			info_other[i].setText("");
			setOther[i].setText("Default");
			setOther[i].setEnabled(false);
		}
		List.setPreferredSize(new Dimension(320, 1700));
		for (i = 0; i < 5; i++)
//			Blight[i].setIcon(Iblank);
			Blight[i].setIcon(Ilight);
		
		Bprojector.setIcon(Iprojector);
		for (i = 0; i < 5; i++)
//			Bman[i].setIcon(Iblank);
			Bman[i].setIcon(Iman);

		Bair.setIcon(Iair);
		Bscreen.setIcon(Iscreen);
		Bother.setIcon(Iother);
		display_man.setText("there are(is) " + num_man + " people\n");
		extra_man.setText("  *" + extra_num_man + "\n");
		extra_AC.setText("  *" + num_AC + "\n");
		extra_light.setText("  *" + num_light + "\n");
		extra_pro.setText("  *" + num_pro + "\n");
		extra_scr.setText("  *" + num_scr + "\n");
		optionEqu.setSelectedIndex(0);
		getInfo_man();
		getInfo_light();
		getInfo_pro();
		getInfo_scr();
		getInfo_AC();
		dragKind = -1;
		dragNo = -1;
		for(i = 0; i < 6; i++)
			Bman[i].setBounds(220 + 125 * (i % 3), 150 + i / 3 * 300, 50, 50);
		for(i = 0; i < 5; i++)
			Blight[i].setBounds(210 + 70 * i, 10, 50, 50);
		Bprojector.setBounds(345, 300, 50, 50);
		Bair.setBounds(10, 10, 50, 50);
		Bscreen.setBounds(600, 300, 50, 50);
		for(i = 0; i < MAX; i++)
		{
			extraL[i].setVisible(false);
			extraM[i].setVisible(false);
			extraS[i].setVisible(false);
			extraP[i].setVisible(false);
			extraA[i].setVisible(false);
		}
		for(i = 0; i < dragNum.length; i++)
			dragNum[i] = 0;
		isDrag = false;
		clickTime = 0;
		isExit = true;
	}

	static void getInfo_man()
	{
		String output = "";
		int t = num_man;
		for (i = 0; i < t; i++)
		{
			if( i < 6 && !toggleM[i] )
			{
				t++;
				continue;
			}
			//System.out.println(i+"  "+manName[i]);
			output += manName[i] + ":\n";
			int tj = 0;
			//if(at[0][i].num == 0)  tj = at[0][0].num;
			//else
			tj = at[0][i].num;
			for (j = 0; j < tj; j++)
			{
				output += at[0][i].name[j] + ":" + at[0][i].value[j] + "\n";
			}
			output += "\n";
		}
		info_man.setText(output);
		info_man.setCaretPosition(0);
	}

	static void getInfo_light()
	{
		String output = "";
		int t = num_light;
		for (i = 0; i < t; i++)
		{
			if( i < 5 && !toggleL[i] )
			{
				t++;
				continue;
			}
			output += lightName[i] + ":\n";
			int tj = 0;
			//if(at[1][i].num == 0)  tj = at[1][0].num;
			//else
			tj = at[1][i].num;
			for (j = 0; j < tj; j++)
			{
				output += at[1][i].name[j] + ":" + at[1][i].value[j] + "\n";
			}
			output += "\n";
		}
		info_light.setText(output);
		info_light.setCaretPosition(0);
	}

	static void getInfo_pro()
	{
		String output = "";
		int t = num_pro;
		if(!toggleP)  t++;
		for (i = 0; i < t; i++)
		{
			if(i == 0 && !toggleP)  continue;
			output += proName[i] + ":\n";
			int tj = 0;
			//if(at[2][i].num == 0)  tj = at[2][0].num;
			//else
			tj = at[2][i].num;
			for (j = 0; j < tj; j++)
			{
				output += at[2][i].name[j] + ":" + at[2][i].value[j] + "\n";
			}
			output += "\n";
		}
		info_pro.setText(output);
		info_pro.setCaretPosition(0);
	}

	static void getInfo_scr()
	{
		String output = "";
		int t = num_scr;
		if(!toggleS)  t++;
		for (i = 0; i < t; i++)
		{
			if(i == 0 && !toggleS)  continue;
			output += scrName[i] + ":\n";
			int tj = 0;
			//if(at[3][i].num == 0)  tj = at[3][0].num;
			//else
			tj = at[3][i].num;
			for (j = 0; j < tj; j++)
			{
				output += at[3][i].name[j] + ":" + at[3][i].value[j] + "\n";
			}
			output += "\n";
		}
		info_scr.setText(output);
		info_scr.setCaretPosition(0);
	}

	static void getInfo_AC()
	{
		String output = "";
		int t = num_AC;
		if(!toggleA)  t++;
		for (i = 0; i < t; i++)
		{
			if(i == 0 && !toggleA)  continue;
			output += ACName[i] + ":\n";
			int tj = 0;
			//if(at[4][i].num == 0)  tj = at[4][0].num;
			//else
			tj = at[4][i].num;
			for (j = 0; j < tj; j++)
			{
				output += at[4][i].name[j] + ":" + at[4][i].value[j] + "\n";
			}
			output += "\n";
		}
		info_AC.setText(output);
		info_AC.setCaretPosition(0);
	}

	static void getInfo(int n)
	{
		String output = "";
		for (i = 0; i < at[n][0].dNum; i++)
		{
			output += at[n][i].dName + ":\n";
			int tj = 0;
			//if(at[n][i].num == 0)  tj = at[n][0].num;
			//else
			tj = at[n][i].num;
			for (j = 0; j < tj; j++)
			{
				output += at[n][i].name[j] + ":" + at[n][i].value[j] + "\n";
			}
			output += "\n";
		}
		info_other[n - 5].setText(output);
		info_other[n - 5].setCaretPosition(0);
	}

	private String path;

	private OWLOntologyManager ontManager;

	// make GUI
	void makeMenu()
	{
		fileMenu.setMnemonic('F');
		fileMenu.add(aboutItem);
		aboutItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				JOptionPane.showMessageDialog(ViewStatusShell.instance,
						"This is a meeting room\nAuthor: Zhenyu Zhou", "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		fileMenu.add(exitItem);
		exitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		actionMenu.setMnemonic('A');
		addMenu.add(add_people);
		add_people.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if (num_man + 1 > MAX)// at most 100 people
				{
					JOptionPane.showMessageDialog(ViewStatusShell.instance,
							"Too many people !", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					ManDialog md = new ManDialog(instance, 3, 0, ontManager);
					display_man.setText("there are(is) " + num_man
							+ " people\n");
				    //extra_man.setText("  *" + extra_num_man + "\n");
					getInfo_man();
					outstream();
				}
				instance.repaint();
			}
		});
		addMenu.add(add_peoples);
		add_peoples.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String content = JOptionPane.showInputDialog(ViewStatusShell.instance,
						"Please input number of people",
						"Add a group of people");
				int num = 0;
				try
				{
				   num = Integer.parseInt(content);
				}
				catch (NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(instance,
							"You must enter integers", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				if (num >= 0)
				{
					extra_num_man += num;
					num_man += num;
					totalNum += num;
					if (num_man > MAX)
					{
						extra_num_man -= num;
						num_man -= num;
						totalNum -= num;
						JOptionPane.showMessageDialog(ViewStatusShell.instance,
								"Too many people !", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						display_man.setText("there are(is) " + num_man
								+ " people\n");
						extra_man.setText("  *" + extra_num_man + "\n");
						getInfo_man();
						outstream();
					}
				}
				else
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid input !",
							"Warning", JOptionPane.WARNING_MESSAGE);
				instance.repaint();
			}
		});
		addMenu.add(add_AC);
		add_AC.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
		   {
			    if(num_AC + 1> MAX)
			    {
					JOptionPane.showMessageDialog(ViewStatusShell.instance,
							"Too many air conditioners !", "Warning",
							JOptionPane.WARNING_MESSAGE);
			    }
				else
				{
					ACDialog ad = new ACDialog(instance, 3, 0);
					//extra_AC.setText("  *" + num_AC + "\n");
					getInfo_AC();
					outstream();
				}
			    instance.repaint();
		   }
		});
		addMenu.add(add_ACs);
		add_ACs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String content = JOptionPane.showInputDialog(ViewStatusShell.instance,
						"Please input number of air conditioners",
						"Add air conditioner");
				int num = 0;
				try
				{
				  num = Integer.parseInt(content);
				}
				catch (NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(instance,
							"You must enter integers", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				if (num >= 0)
				{
					num_AC += num;
					totalNum += num;
					if (num_AC > MAX)
					{
						num_AC -= num;
						totalNum -= num;
						JOptionPane.showMessageDialog(ViewStatusShell.instance,
								"Too many air conditioners !", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						extra_AC.setText("  *" + num_AC + "\n");
						getInfo_AC();
						outstream();
					}
				}
				else
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid input !",
							"Warning", JOptionPane.WARNING_MESSAGE);
				instance.repaint();
			}
		});
		addMenu.add(add_light);
		add_light.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
		   {
			    
			    if(num_light + 1> MAX)
			    {
					JOptionPane.showMessageDialog(ViewStatusShell.instance,
							"Too many lights !", "Warning",
							JOptionPane.WARNING_MESSAGE);			    	
			    }
			    else
			    {
			    	LightDialog ld = new LightDialog(instance, 3, 0);
					//extra_light.setText("  *" + num_light + "\n");
					getInfo_light();
					outstream();			    	
			    }
			    instance.repaint();
		   }
		});
		addMenu.add(add_lights);
		add_lights.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String content = JOptionPane.showInputDialog(ViewStatusShell.instance,
						"Please input number of lights", "Add light");
				int num = 0;
				try
				{
				   num = Integer.parseInt(content);
				}
				catch (NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(instance,
							"You must enter integers", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				if (num >= 0)
				{
					num_light += num;
					totalNum += num;
					if (num_light > MAX)
					{
						JOptionPane.showMessageDialog(ViewStatusShell.instance,
								"Too many lights !", "Warning",
								JOptionPane.WARNING_MESSAGE);
						num_light -= num;
						totalNum -= num;
					}
					else
					{
						extra_light.setText("  *" + num_light + "\n");
						getInfo_light();
						outstream();
					}
				}
				else
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid input !",
							"Warning", JOptionPane.WARNING_MESSAGE);
				instance.repaint();
			}
		});
		addMenu.add(add_pro);
		add_pro.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if(num_pro + 1 > MAX)
				{
					JOptionPane.showMessageDialog(ViewStatusShell.instance,
							"Too many projectors !", "Warning",
							JOptionPane.WARNING_MESSAGE);					
				}
				else
				{
					ProDialog pd = new ProDialog(instance, 3, 0);
					//extra_pro.setText("  *" + num_pro + "\n");
					getInfo_pro();
					outstream();					
				}
				instance.repaint();
			}
		});
		addMenu.add(add_pros);
		add_pros.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String content = JOptionPane.showInputDialog(ViewStatusShell.instance,
						"Please input number of projectors", "Add projector");
				int num = 0;
				try
				{
				  num = Integer.parseInt(content);
				}
				catch (NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(instance,
							"You must enter integers", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				if (num >= 0)
				{
					num_pro += num;
					totalNum += num;
					if (num_pro > MAX)
					{
						num_pro -= num;
						totalNum -= num;
						JOptionPane.showMessageDialog(ViewStatusShell.instance,
								"Too many projectors !", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						extra_pro.setText("  *" + num_pro + "\n");
						getInfo_pro();
						outstream();
					}
				}
				else
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid input !",
							"Warning", JOptionPane.WARNING_MESSAGE);
				instance.repaint();
			}
		});
		addMenu.add(add_scr);
		add_scr.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if(num_scr + 1 > MAX)
				{
					JOptionPane.showMessageDialog(ViewStatusShell.instance,
							"Too many screens !", "Warning",
							JOptionPane.WARNING_MESSAGE);					
				}
				else
				{
					ScrDialog sd = new ScrDialog(instance, 3, 0);
					//extra_scr.setText("  *" + num_scr + "\n");
					getInfo_scr();
					outstream();					
				}
				instance.repaint();
			}
		});
		addMenu.add(add_scrs);
		add_scrs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String content = JOptionPane.showInputDialog(ViewStatusShell.instance,
						"Please input number of screens", "Add screen");
				int num = 0;
				try
				{
				  num = Integer.parseInt(content);
				}
				catch (NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(instance,
							"You must enter integers", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				if (num >= 0)
				{
					num_scr += num;
					totalNum += num;
					if (num_scr > MAX)
					{
						num_scr -= num;
						totalNum -= num;
						JOptionPane.showMessageDialog(ViewStatusShell.instance,
								"Too many screens !", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						extra_scr.setText("  *" + num_scr + "\n");
						getInfo_scr();
						outstream();
					}
				}
				else
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid input !",
							"Warning", JOptionPane.WARNING_MESSAGE);
				instance.repaint();
			}
		});
		subMenu.add(sub_people);
		sub_people.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if (extra_num_man != 0)
				{
					ManDialog md = new ManDialog(instance, 4, 0, ontManager);
					//display_man.setText("there are(is) " + num_man
							//+ " people\n");
					//extra_man.setText("  *" + extra_num_man + "\n");
					getInfo_man();
					outstream();
				}
				else
				{
					JOptionPane.showMessageDialog(ViewStatusShell.instance,
							"There is no extra people already !", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}
				instance.repaint();
			}
		});
		subMenu.add(sub_peoples);
		sub_peoples.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String content = JOptionPane.showInputDialog(ViewStatusShell.instance,
						"Please input number of people",
						"Decrease a group of people");
				int num = 0;
				try
				{
				  num = Integer.parseInt(content);
				}
				catch (NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(instance,
							"You must enter integers", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				if (num >= 0 && extra_num_man >= num)
				{
					extra_num_man -= num;
					num_man -= num;
					totalNum -= num;
					display_man.setText("there are(is) " + num_man
							+ " people\n");
					extra_man.setText("  *" + extra_num_man + "\n");
					getInfo_man();
					outstream();
				}
				else
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid input !",
							"Warning", JOptionPane.WARNING_MESSAGE);
				instance.repaint();
			}
		});
		subMenu.add(sub_AC);
		sub_AC.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if ( num_AC != 0)
				{
					ACDialog md = new ACDialog(instance, 4, 0);
					//extra_AC.setText("  *" + num_AC + "\n");
					getInfo_AC();
					outstream();
				}
				else
				{
					JOptionPane.showMessageDialog(ViewStatusShell.instance,
							"Invalid input !", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}
				instance.repaint();
			}
	    });
		subMenu.add(sub_ACs);
		sub_ACs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String content = JOptionPane.showInputDialog(ViewStatusShell.instance,
						"Please input number of air conditioners",
						"Decrease air conditioner");
				int num = 0, temp;
				try
				{
				  num = Integer.parseInt(content);
				}
				catch (NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(instance,
							"You must enter integers", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				if ((optionAC && !toggleA) || (!optionAC && toggleA))
					temp = 1;
				else if (optionAC && toggleA)
					temp = 2;
				else
					temp = 0;
				if (num >= 0 && num_AC - num >= temp)
				{
					num_AC -= num;
					totalNum -= num;
					extra_AC.setText("  *" + num_AC + "\n");
					getInfo_AC();
					outstream();
				}
				else
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid input !",
							"Warning", JOptionPane.WARNING_MESSAGE);
				instance.repaint();
			}
		});
		subMenu.add(sub_light);
		sub_light.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if ( num_light != 0)
				{
					LightDialog ld = new LightDialog(instance, 4, 0);
					extra_light.setText("  *" + num_light + "\n");
					getInfo_light();
					outstream();
				}
				else
				{
					JOptionPane.showMessageDialog(ViewStatusShell.instance,
							"Invalid input !", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}	
				instance.repaint();
			}				
	    });
		subMenu.add(sub_lights);
		sub_lights.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String content = JOptionPane.showInputDialog(ViewStatusShell.instance,
						"Please input number of lights", "Decrease light");
				int num = 0;
				try
				{
				  num = Integer.parseInt(content);
				}
				catch (NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(instance,
							"You must enter integers", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				int temp = 0, j;
				if (optionLight) temp++;
				for (j = 0; j < 5; j++)
					if (toggleL[j]) temp++;
				if (num >= 0 && num_light - num >= temp)
				{
					num_light -= num;
					totalNum -= num;
					extra_light.setText("  *" + num_light + "\n");
					getInfo_light();
					outstream();
				}
				else
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid input !",
							"Warning", JOptionPane.WARNING_MESSAGE);
				instance.repaint();
			}
		});
		subMenu.add(sub_pro);
		sub_pro.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if ( num_pro != 0)
				{
					ProDialog pd = new ProDialog(instance, 4, 0);
					extra_pro.setText("  *" + num_pro + "\n");
					getInfo_pro();
					outstream();
				}
				else
				{
					JOptionPane.showMessageDialog(ViewStatusShell.instance,
							"Invalid input !", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}		
				instance.repaint();
			}				
	    });
		subMenu.add(sub_pros);
		sub_pros.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String content = JOptionPane.showInputDialog(ViewStatusShell.instance,
						"Please input number of projectors",
						"Decrease projector");
				int num = 0, temp;
				try
				{
				  num = Integer.parseInt(content);
				}
				catch (NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(instance,
							"You must enter integers", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				if ((optionPro && !toggleP) || (!optionPro && toggleP))
					temp = 1;
				else if (optionPro && toggleP)
					temp = 2;
				else
					temp = 0;
				if (num >= 0 && num_pro - num >= temp)
				{
					num_pro -= num;
					totalNum -= num;
					extra_pro.setText("  *" + num_pro + "\n");
					getInfo_pro();
					outstream();
				}
				else
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid input !",
							"Warning", JOptionPane.WARNING_MESSAGE);
				instance.repaint();
			}
		});
		subMenu.add(sub_scr);
		sub_scr.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if ( num_scr != 0)
				{
					ScrDialog sd = new ScrDialog(instance, 4, 0);
					extra_scr.setText("  *" + num_scr + "\n");
					getInfo_scr();
					outstream();
				}
				else
				{
					JOptionPane.showMessageDialog(ViewStatusShell.instance,
							"Invalid input !", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}	
				instance.repaint();
			}				
	    });
		subMenu.add(sub_scrs);
		sub_scrs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String content = JOptionPane.showInputDialog(ViewStatusShell.instance,
						"Please input number of screens", "Decrease screen");
				int num = 0, temp;
				try
				{
				  num = Integer.parseInt(content);
				}
				catch (NumberFormatException nfe)
				{
					JOptionPane.showMessageDialog(instance,
							"You must enter integers", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
				}
				if ((optionScr && !toggleS) || (!optionScr && toggleS))
					temp = 1;
				else if (optionScr && toggleS)
					temp = 2;
				else
					temp = 0;
				if (num >= 0 && num_scr - num >= temp)
				{
					num_scr -= num;
					totalNum -= num;
					extra_scr.setText("  *" + num_scr + "\n");
					getInfo_scr();
					outstream();
				}
				else
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid input !",
							"Warning", JOptionPane.WARNING_MESSAGE);
				instance.repaint();
			}
		});
		actionMenu.add(checkT);
		checkT.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				temperature = 24 + num_man;
				temperature -= 10 * num_AC;
				String output = "The temperature is " + temperature
						+ " degrees Celsius\n";
				if (temperature >= 30)
					output += "It's too hot !\n";
				else if (temperature <= 10)
					output += "It's too cold !\n";
				else
					output += "The temperature is proper !\n";
				JOptionPane.showMessageDialog(ViewStatusShell.instance, output, "Temperature",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		actionMenu.addSeparator();
		actionMenu.add(addMenu);
		actionMenu.add(subMenu);
		actionMenu.addSeparator();
		actionMenu.add(drag);
		drag.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				dragDialog dd = new dragDialog(instance);
				instance.repaint();
			}
		});
		actionMenu.add(noDrag);
		noDrag.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				dragNo = -1;
				dragKind = -1;
				instance.repaint();
			}
		});		
		helpMenu.add(helpItem);
		helpItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String output = "This is a meeting room.\n";
				output += "You can increase or decrease the device easily.\n";
				output += "It's used to displayed the explainer's controlling.\n";
				JOptionPane.showMessageDialog(ViewStatusShell.instance, output, "Help",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(warningItem);
		warningItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				String output = "You can increase or decrease the devices in numbers.\n";
				output += "But then you have to define their attributes one by one.\n";
				output += "Deleting a group of devices always deletes from the last one.\n";
				JOptionPane.showMessageDialog(ViewStatusShell.instance, output, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			}				
	    });	
		helpMenu.add(diary);
		diary.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				DiaryDialog did = new DiaryDialog(instance);
			}
	    });
		helpMenu.setMnemonic('H');
		setJMenuBar(bar);
		bar.add(fileMenu);
		bar.add(actionMenu);
		bar.add(helpMenu);
	}
	
	void makeDragable()
	{
		JButton Bl, Bm, Ba, Bs, Bp;
		Bm = new JButton("", Iman);
		Bm.setBounds(925, 50, 50, 50);
		back.add(Bm);
		Bl = new JButton("", Ilight);
		Bl.setBounds(925, 120, 50, 50);
		back.add(Bl);
		Bp = new JButton("", Iprojector);
		Bp.setBounds(925, 190, 50, 50);
		back.add(Bp);
		Bs = new JButton("", Iscreen);
		Bs.setBounds(925, 260, 50, 50);
		back.add(Bs);
		Ba = new JButton("", Iair);
		Ba.setBounds(925, 330, 50, 50);
		back.add(Ba);
	}

	void makeButton()
	{
		Ilight = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Light.jpg");
		Blight = new JButton[5];
		for (i = 0; i < 5; i++)
		{
			Blight[i] = new JButton("", Iblank);
//			Blight[i].setVisible(true);
			Blight[i].setToolTipText("This is a light");
			Blight[i].setBounds(210 + 70 * i, 10, 50, 50);
			Blight[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					JButton temp = (JButton) (event.getSource());
					if (temp == Blight[0])
						i = 0;
					else if (temp == Blight[1])
						i = 1;
					else if (temp == Blight[2])
						i = 2;
					else if (temp == Blight[3])
						i = 3;
					else if (temp == Blight[4])
						i = 4;
					else
						i = 5;
					if (toggleL[i])
					{
						Blight[i].setIcon(Iblank);
						toggleL[i] = !toggleL[i];
						num_light--;
						totalNum--;
						extra_light.setText("  *" + num_light + "\n");
						getInfo_light();
						outstream();
					}
					else
					{
						if (num_light + 1 > MAX)
						{
							JOptionPane.showMessageDialog(ViewStatusShell.instance,
									"Too many lights !", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							LightDialog ld = new LightDialog(instance, 2, i);
							extra_light.setText("  *" + num_light + "\n");
							getInfo_light();
							outstream();
						}
					}
				}
			});
			back.add(Blight[i]);
		}
		//Blight[2].setVisible(false);
		for( i = 0; i < MAX; i++)
		{
            extraL[i] = new JButton( "", Ilight);
            extraL[i].setVisible(false);
		}
		Iprojector = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/projector.jpg");
		Bprojector = new JButton("", Iblank);
		Bprojector.setBounds(345, 300, 50, 50);
		Bprojector.setToolTipText("This is a projector");
		Bprojector.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if (toggleP)
				{
					Bprojector.setIcon(Iblank);
					toggleP = !toggleP;
					num_pro--;
					totalNum--;
					extra_pro.setText("  *" + num_pro + "\n");
					getInfo_pro();
					outstream();
				}
				else
				{
					if (num_pro + 1 > MAX)
					{
						JOptionPane.showMessageDialog(ViewStatusShell.instance,
								"Too many projectors !", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						ProDialog pd = new ProDialog(instance, 2, 0);						
						extra_pro.setText("  *" + num_pro + "\n");
						getInfo_pro();
						outstream();
					}
				}
			}
		});
		back.add(Bprojector);
		for( i = 0; i < MAX; i++)
		{
            extraP[i] = new JButton( "", Iprojector);
            extraP[i].setVisible(false);
		}
		Iman = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Person.jpg");
		Bman = new JButton[6];
		for (i = 0; i < 6; i++)
		{
			Bman[i] = new JButton("", Iblank);
			Bman[i].setBounds(220 + 125 * (i % 3), 150 + i / 3 * 300, 50, 50);
			// 6 people around a table
			Bman[i].setToolTipText("This is a person");
			Bman[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					JButton temp = (JButton) (event.getSource());
					if (temp == Bman[0])
						i = 0;
					else if (temp == Bman[1])
						i = 1;
					else if (temp == Bman[2])
						i = 2;
					else if (temp == Bman[3])
						i = 3;
					else if (temp == Bman[4])
						i = 4;
					else
						i = 5;
					if (toggleM[i])
					{
						Bman[i].setIcon(Iblank);
						toggleM[i] = !toggleM[i];
						num_man--;
						totalNum--;
						display_man.setText("There are(is) " + num_man
								+ " people\n");
						getInfo_man();
						outstream();
					}
					else
					{
						if (num_man + 1 > MAX)
						{
							JOptionPane.showMessageDialog(ViewStatusShell.instance,
									"Too many people !", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							ManDialog md = new ManDialog(instance,2,i, ontManager);
							display_man.setText("There are(is) " + num_man
									+ " people\n");
							getInfo_man();
							outstream();
						}
					}
				}
			});
			back.add(Bman[i]);
		}
		for( i = 0; i < MAX; i++)
		{
            extraM[i] = new JButton( "", Iman);
            extraM[i].setVisible(false);
		}
		Iair = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Air_Condition.jpg");
		Bair = new JButton("", Iblank);
		Bair.setBounds(10, 10, 50, 50);
		Bair.setToolTipText("This is an air conditioner");
		Bair.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if (toggleA)
				{
					Bair.setIcon(Iblank);
					toggleA = !toggleA;
					num_AC--;
					totalNum--;
					extra_AC.setText("  *" + num_AC + "\n");
					getInfo_AC();
					outstream();
				}
				else
				{
					if (num_AC + 1 > MAX)
					{
						JOptionPane.showMessageDialog(ViewStatusShell.instance,
								"Too many air conditioners !", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						ACDialog ad = new ACDialog(instance, 2, 0);
						extra_AC.setText("  *" + num_AC + "\n");
						getInfo_AC();
						outstream();
					}
				}
			}
		});
		back.add(Bair);
		for( i = 0; i < MAX; i++)
		{
            extraA[i] = new JButton( "", Iair);
            extraA[i].setVisible(false);
		}
		Iscreen = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Screen.jpg");
		Bscreen = new JButton("", Iblank);
		Bscreen.setBounds(600, 300, 50, 50);
		Bscreen.setToolTipText("This is a screen");
		Bscreen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if (toggleS)
				{
					Bscreen.setIcon(Iblank);
					toggleS = !toggleS;
					num_scr--;
					totalNum--;
					extra_scr.setText("  *" + num_scr + "\n");
					getInfo_scr();
					outstream();
				}
				else
				{
					if (num_scr + 1 > MAX)
					{
						JOptionPane.showMessageDialog(ViewStatusShell.instance,
								"Too many screens !", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						ScrDialog sd = new ScrDialog(instance, 2, 0);
						extra_scr.setText("  *" + num_scr + "\n");
						getInfo_scr();
						outstream();
					}
				}
			}
		});
		back.add(Bscreen);
		for( i = 0; i < MAX; i++)
		{
            extraS[i] = new JButton( "", Iscreen);
            extraS[i].setVisible(false);
		}
		for (i = 0; i < 5; i++)
			Blight[i].setRolloverIcon(Ilight);
		for (i = 0; i < 6; i++)
			Bman[i].setRolloverIcon(Iman);
		Bprojector.setRolloverIcon(Iprojector);
		Bair.setRolloverIcon(Iair);
		Bscreen.setRolloverIcon(Iscreen);
	}

	void makeOptionalEquip()
	{
		Iother = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/default.jpg");
		IAH = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Air_Humidifier.jpg");
		Iamp = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Amplifier.jpg");
		ICP = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Cell_Phone.jpg");
		Icom = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Computer.jpg");
		IFL = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Front_Light.jpg");
		IHWD = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Hot_Water_Dispenser.jpg");
		IRL = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Rear_Light.jpg");
		Bother = new JButton("", Iother);
		Bother.setBounds(10, 100, 50, 50);
		Bother.setToolTipText("Optional equipment");
		back.add(Bother);
		optionEqu = new JComboBox(names);
		optionEqu.setMaximumRowCount(3);
		optionEqu.setBounds(70, 100, 140, 50);
		optionEqu.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent event)
			{
				if (event.getStateChange() == ItemEvent.SELECTED)
				{
					/*if (optionMan && optionEqu.getSelectedIndex() != 3)
					{
						num_man--;
						totalNum--;
						optionMan = false;
						display_man.setText("there are(is) " + num_man
								+ " people\n");
						getInfo_man();
						outstream();
					}
					if (optionAC && optionEqu.getSelectedIndex() != 4)
					{
						num_AC--;
						totalNum--;
						extra_AC.setText("  *" + num_AC + "\n");
						optionAC = false;
						getInfo_AC();
						outstream();
					}
					if (optionLight && optionEqu.getSelectedIndex() != 1)
					{
						num_light--;
						totalNum--;
						extra_light.setText("  *" + num_light + "\n");
						optionLight = false;
						getInfo_light();
						outstream();
					}
					if (optionPro && optionEqu.getSelectedIndex() != 2)
					{
						num_pro--;
						totalNum--;
						extra_pro.setText("  *" + num_pro + "\n");
						optionPro = false;
						getInfo_pro();
						outstream();
					}
					if (optionScr && optionEqu.getSelectedIndex() != 5)
					{
						num_scr--;
						totalNum--;
						extra_scr.setText("  *" + num_scr + "\n");
						optionScr = false;
						getInfo_scr();
						outstream();
					}*/
					switch (optionEqu.getSelectedIndex())
					{
					case 0:
						Bother.setIcon(Iother);
						break;
					/*case 1:
					{
						if (num_light + 1 > MAX)
						{
							Bother.setIcon(Iother);
							optionEqu.setSelectedIndex(0);
							JOptionPane.showMessageDialog(Main.instance,
									"Too many lights !", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							Bother.setIcon(Ilights);
							num_light++;
							totalNum++;
							extra_light.setText("  *" + num_light + "\n");
							optionLight = true;
							getInfo_light();
							outstream();
						}
						break;
					}
					case 2:
					{
						if (num_pro + 1 > MAX)
						{
							Bother.setIcon(Iother);
							optionEqu.setSelectedIndex(0);
							JOptionPane.showMessageDialog(Main.instance,
									"Too many projectors !", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							Bother.setIcon(Iprojector);
							num_pro++;
							totalNum++;
							extra_pro.setText("  *" + num_pro + "\n");
							optionPro = true;
							getInfo_pro();
							outstream();
						}
						break;
					}
					case 3:
					{
						if (num_man + 1 > MAX)
						{
							Bother.setIcon(Iother);
							optionEqu.setSelectedIndex(0);
							JOptionPane.showMessageDialog(Main.instance,
									"Too many people !", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							Bother.setIcon(Iman);
							num_man++;
							totalNum++;
							display_man.setText("there are(is) " + num_man
									+ " people\n");
							optionMan = true;
							getInfo_man();
							outstream();
						}
						break;
					}
					case 4:
					{
						if (num_AC + 1 > MAX)
						{
							Bother.setIcon(Iother);
							optionEqu.setSelectedIndex(0);
							JOptionPane.showMessageDialog(Main.instance,
									"Too many air conditioners !", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							Bother.setIcon(Iair);
							num_AC++;
							totalNum++;
							extra_AC.setText("  *" + num_AC + "\n");
							optionAC = true;
							getInfo_AC();
							outstream();
						}
						break;
					}
					case 5:
					{
						if (num_scr + 1 > MAX)
						{
							Bother.setIcon(Iother);
							optionEqu.setSelectedIndex(0);
							JOptionPane.showMessageDialog(Main.instance,
									"Too many screens !", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
						else
						{
							Bother.setIcon(Iscreen);
							num_scr++;
							totalNum++;
							extra_scr.setText("  *" + num_scr + "\n");
							optionScr = true;
							getInfo_scr();
							outstream();
						}
						break;
					}*/
					case 1:
						Bother.setIcon(IAH);
						break;
					case 2:
						Bother.setIcon(Iamp);
						break;
					case 3:
						Bother.setIcon(ICP);
						break;
					case 4:
						Bother.setIcon(Icom);
						break;
					/*case 5:
						Bother.setIcon(IFL);
						break;*/
					case 5:
						Bother.setIcon(IHWD);
						break;
					/*case 7:
						Bother.setIcon(IRL);
						break;*/
					case 6:
						Bother.setIcon(Iblank);
						break;
					}
				}
			}
		});
		back.add(optionEqu);
	}

	void makeTotalNum()
	{
		BextraMan = new JButton("", Iman);
		BextraMan.setBounds(450, 70, 50, 50);
		BextraMan.setToolTipText("Extra people");
		extra_man = new JTextField(5);
		extra_man.setEditable(false);
		extra_man.setHorizontalAlignment(JTextField.CENTER);
		extra_man.setOpaque(IsOpaque);
		extra_man.setBounds(510, 70, 50, 50);
		Font bold = new Font("Serif", Font.BOLD, 14);
		extra_man.setFont(bold);
		extra_man.setText("  *0\n");
		extra_man.setToolTipText("Number of extra person (except the person around the table)");
		back.add(BextraMan);
		back.add(extra_man);

		extra_AC = new JTextField(5);
		extra_AC.setHorizontalAlignment(JTextField.CENTER);
		extra_AC.setOpaque(IsOpaque);
		extra_AC.setBounds(70, 10, 50, 50);
		extra_AC.setEditable(false);
		extra_AC.setFont(bold);
		extra_AC.setText("  *0\n");
		extra_AC.setToolTipText("Number of air conditioners");
		back.add(extra_AC);
		extra_light = new JTextField(5);
		extra_light.setHorizontalAlignment(JTextField.CENTER);
		extra_light.setOpaque(IsOpaque);
		extra_light.setBounds(550, 10, 50, 50);
		extra_light.setEditable(false);
		extra_light.setFont(bold);
		extra_light.setText("  *0\n");
		extra_light.setToolTipText("Number of lights");
		back.add(extra_light);
		extra_pro = new JTextField(5);
		extra_pro.setHorizontalAlignment(JTextField.CENTER);
		extra_pro.setOpaque(IsOpaque);
		extra_pro.setBounds(410, 300, 50, 50);
		extra_pro.setEditable(false);
		extra_pro.setFont(bold);
		extra_pro.setText("  *0\n");
		extra_pro.setToolTipText("Number of projectors");
		back.add(extra_pro);
		extra_scr = new JTextField(5);
		extra_scr.setHorizontalAlignment(JTextField.CENTER);
		extra_scr.setOpaque(IsOpaque);
		extra_scr.setBounds(600, 240, 50, 50);
		extra_scr.setEditable(false);
		extra_scr.setFont(bold);
		extra_scr.setText("  *0\n");
		extra_scr.setToolTipText("Number of screens");
		back.add(extra_scr);

		display_man = new JTextField(20);
		display_man.setEditable(false);
		display_man.setOpaque(IsOpaque);
		display_man.setHorizontalAlignment(JTextField.CENTER);
		display_man.setBounds(250, 70, 170, 50);
		display_man.setFont(bold);
		display_man.setText("there are(is) " + num_man + " people\n");
		back.add(display_man);
	}
	
	void makeList()
	{
		List = new JPanel();
		List.setLayout(null);
		List.setPreferredSize(new Dimension(320, 890)); // JPanel should use
															// setPreferredSized,
															// not setSize or
															// setBounds(former:8700)
		Lman = new JLabel("Current States of People:");
		Lman.setHorizontalAlignment(JLabel.CENTER);
		Lman.setBounds(40, 10, 200, 50);
		List.add(Lman);
		String output = "";
		info_man = new JTextArea(output, 10, 15);
		info_man.setEditable(false);
		JScrollPane jsm = new JScrollPane(info_man);
		jsm.setBounds(40, 50, 200, 100);
		List.add(jsm);
		setMan = new JButton("Set New States");
		setMan.addActionListener(this);
		setMan.setBounds(70, 160, 150, 30);
		List.add(setMan);
		Llight = new JLabel("Current States of Light:");
		Llight.setHorizontalAlignment(JLabel.CENTER);
		Llight.setBounds(40, 190, 200, 50);
		List.add(Llight);
		info_light = new JTextArea(output, 10, 15);
		info_light.setEditable(false);
		JScrollPane jsl = new JScrollPane(info_light);
		jsl.setBounds(40, 230, 200, 100);
		List.add(jsl);
		setLight = new JButton("Set New States");
		setLight.addActionListener(this);
		setLight.setBounds(70, 340, 150, 30);
		List.add(setLight);
		Lpro = new JLabel("Current States of Projector:");
		Lpro.setHorizontalAlignment(JLabel.CENTER);
		Lpro.setBounds(40, 370, 200, 50);
		List.add(Lpro);
		info_pro = new JTextArea(output, 10, 15);
		info_pro.setEditable(false);
		JScrollPane jsp = new JScrollPane(info_pro);
		jsp.setBounds(40, 410, 200, 100);
		List.add(jsp);
		setPro = new JButton("Set New States");
		setPro.addActionListener(this);
		setPro.setBounds(70, 520, 150, 30);
		List.add(setPro);
		Lscr = new JLabel("Current States of Screen:");
		Lscr.setHorizontalAlignment(JLabel.CENTER);
		Lscr.setBounds(40, 550, 200, 50);
		List.add(Lscr);
		info_scr = new JTextArea(output, 10, 15);
		info_scr.setEditable(false);
		JScrollPane jss = new JScrollPane(info_scr);
		jss.setBounds(40, 590, 200, 100);
		List.add(jss);
		setScr = new JButton("Set New States");
		setScr.addActionListener(this);
		setScr.setBounds(70, 700, 150, 30);
		List.add(setScr);
		LAC = new JLabel("Current States of Air conditioner:");
		LAC.setHorizontalAlignment(JLabel.CENTER);
		LAC.setBounds(40, 730, 200, 50);
		List.add(LAC);
		info_AC = new JTextArea(output, 10, 15);
		info_AC.setEditable(false);
		JScrollPane jsa = new JScrollPane(info_AC);
		jsa.setBounds(40, 770, 200, 100);
		List.add(jsa);
		setAC = new JButton("Set New States");
		setAC.addActionListener(this);
		setAC.setBounds(70, 880, 150, 30);
		List.add(setAC);
		for (i = 0; i < 20; i++)
		{
			Lother[i] = new JLabel("Default:");
			Lother[i].setHorizontalAlignment(JLabel.CENTER);
			Lother[i].setBounds(40, 730 + 180 * i + 180, 200, 50);
			List.add(Lother[i]);
			info_other[i] = new JTextArea("", 10, 15);
			info_other[i].setEditable(false);
			JScrollPane jstemp = new JScrollPane(info_other[i]);
			jstemp.setBounds(40, 770 + 180 * i + 180, 200, 100);
			List.add(jstemp);
			setOther[i] = new JButton("Default");
			setOther[i].setEnabled(false);
			setOther[i].addActionListener(this);
			setOther[i].setBounds(70, 880 + 180 * i + 180, 150, 30);
			List.add(setOther[i]);
		}

		JScrollPane SList = new JScrollPane(List);
		SList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		SList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		SList.setBounds(1015, 0, 280, 655);
		SList.setWheelScrollingEnabled(true); 
		SList.getVerticalScrollBar().setUnitIncrement(20);
		back.add(SList);
	}

	public ViewStatusShell(String path, OWLOntologyManager ontManager)
	{
		super("Meeting Room");
		instance = this;
		
		this.path = path;
		this.ontManager = ontManager;

		int i;
		manName = new String[MAX];
		lightName = new String[MAX];
		proName = new String[MAX];
		scrName = new String[MAX];
		ACName = new String[MAX];
		for (i = 0; i < MAX; i++)
			manName[i] = "people_" + (i + 1);
		for (i = 0; i < MAX; i++)
			proName[i] = "projector_" + (i + 1);
		for (i = 0; i < MAX; i++)
			lightName[i] = "light_" + (i + 1);
		for (i = 0; i < MAX; i++)
			scrName[i] = "screen_" + (i + 1);
		for (i = 0; i < MAX; i++)
			ACName[i] = "Air conditioner_" + (i + 1);
		at = new Attribute[20][]; // at most 20 devices
		for (i = 0; i < 20; i++)
			at[i] = new Attribute[MAX];
		for( i = 0; i < 20; i++)
			for( j = 0; j < MAX; j++)
				at[i][j] = new Attribute();
		for(i = 0; i < dragNum.length; i++)
			dragNum[i] = 0;

		back = new JLabel(new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/Background2.jpg"));
		back.setBounds(0, 0, this.getWidth(), this.getHeight());
		ButtonHandler handler = new ButtonHandler();
		Iblank = new ImageIcon("D:\\Program Files (x86)\\eclipse\\images/blank.jpg");
		File file = new File("test.txt");
		System.out.println("file path: "+ file.getAbsolutePath());

		toggleL = new boolean[5];
		toggleM = new boolean[6];

		this.setResizable(false);
		this.setLocationRelativeTo(this);

		makeMenu();
		makeButton();
		makeOptionalEquip();
		makeTotalNum();
		makeDragable();
		makeList();

		Bexit.setBounds(300, 600, 100, 50);
		Bexit.setToolTipText("Exit");
		back.add(Bexit);
		Bexit.addActionListener(handler);

		Breset.setBounds(300, 550, 100, 50);
		Breset.setToolTipText("Reset");
		back.add(Breset);
		Breset.addActionListener(handler);

		setContentPane(back);
		setSize(1300, 710);
		setVisible(true);
		// pack();
		setLocation(5, 5);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addWindowListener(new MyWListener());
		
		for( i = 0; i < jbp.length; i++)
			jbp[i] = new JMenuItem();
		jbp[0].setText("Diary");
		jbp[0].addActionListener(handler);
		jpm.add(jbp[0]);
		jpm.addSeparator();
		jbp[1].setText("Reset");
		jbp[1].addActionListener(handler);
		jpm.add(jbp[1]);
		jbp[2].setText("Exit");
		jbp[2].addActionListener(handler);
		jpm.add(jbp[2]);
		//jpm.setVisible(true); 
		//back.add(jpm);
		
		ListenerThread t = new ListenerThread(this.path); 
		Thread tPaint = new Thread(new CReprint());
		t.start();
		tPaint.start();
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.RED);
		g2d.setStroke(new BasicStroke(10.0f));
		g2d.draw(new Rectangle2D.Double(120, 270, 500, 200));
		// draw a table
	}

//	public static void display()
//	{
//		ViewStatusShell application = new ViewStatusShell();
//		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}

	private class ButtonHandler implements ActionListener
	{

		public void actionPerformed(ActionEvent event)
		{
			if (event.getSource() == Bexit || event.getSource() == jbp[2])
				System.exit(0);
			else if (event.getSource() == Breset || event.getSource() == jbp[1])
			{
				init();
				outstream();
			}
			else if (event.getSource() == jbp[0])
				ViewStatusShell.Cdiary();//DiaryDialog did = new DiaryDialog(instance);
		}
	}
	static void Cdiary()
	{
		DiaryDialog did = new DiaryDialog(instance);
	}

	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == setMan)
		{
			ManDialog md = new ManDialog(this, 1, 0, ontManager);
		}
		else if (event.getSource() == setLight)
		{
			LightDialog ld = new LightDialog(this, 1, 0);
		}
		else if (event.getSource() == setPro)
		{
			ProDialog pd = new ProDialog(this, 1, 0);
		}
		else if (event.getSource() == setScr)
		{
			ScrDialog sd = new ScrDialog(this, 1, 0);
		}
		else if (event.getSource() == setAC)
		{
			ACDialog ad = new ACDialog(this, 1, 0);
		}
		else
		{
			int i;
			for (i = 0; i < totalKind; i++)
			{
				if (event.getSource() == setOther[i]) break;
			}
			if (i < totalKind)// find
			{
				OthDialog od = new OthDialog(this, i);
			}
		}
	}

	public static void notifyContinue()
	{
//		Socket s;
//		try
//		{
//		  s = new Socket("127.0.0.1", 63392);
//		  OutputStream os = s.getOutputStream();
//		  byte[] buf4 = new byte[4];
//		  os.write(mkBytes(buf4, 4));
//		  os.write(mkBytes(buf4, 0)); 
//		  s.close();
//		}
//		catch (UnknownHostException e)
//		{
//			e.printStackTrace();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
	}

	public static void notifyPause()
	{
//		Socket s;
//		try
//		{
//		  s = new Socket("127.0.0.1", 63392);
//		  OutputStream os = s.getOutputStream();
//		  byte[] buf4 = new byte[4];
//		  os.write(mkBytes(buf4, 4));
//		  os.write(mkBytes(buf4, 1)); 
//		  s.close();
//		}
//		catch (UnknownHostException e)
//		{
//			e.printStackTrace();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
	}
	@Override
	public void mouseDragged(MouseEvent e)
	{
		int x, y;
		if(!(dragNo == -1 || dragKind == -1))
		{
		  x = e.getX() - 50; y = e.getY() - 50;
		  int i;
		  if(dragKind == 0)
		     Bman[dragNo].setBounds( x, y, 50, 50 );
		  else if(dragKind == 1)
			  Blight[dragNo].setBounds( x, y, 50, 50 );
		  else if(dragKind == 2)
		      Bprojector.setBounds( x, y, 50, 50 );
		  else if(dragKind == 3)
		      Bscreen.setBounds( x, y, 50, 50 );
		  else if(dragKind == 4)
		      Bair.setBounds( x, y, 50, 50 );
		  return ;
		}
		x = e.getX(); y = e.getY();
		if(!isDrag)
		{
		  if(x < 882 || x > 1017)  return ;
		  Dkind = 0;
		  for(Dkind = 0; Dkind < 5; Dkind++)
			  if(y >= 100 + Dkind * 70 && y <= 150 + Dkind * 70)
			    	break;
		  //System.out.println("kind:"+Dkind+"num:"+dragNum[0]);
		  if(Dkind == 5)  return ;
		  isDrag = true;
		}
		switch(Dkind)
		{
		  case 0:
			  extraM[dragNum[0]].setVisible(true);
			  extraM[dragNum[0]].setBounds(e.getX(), e.getY(), 50, 50);
			  back.add(extraM[dragNum[0]]);break;
		  case 1:
			  extraL[dragNum[1]].setVisible(true);
			  extraL[dragNum[1]].setBounds(e.getX(), e.getY(), 50, 50);
			  back.add(extraL[dragNum[1]]);break;
		  case 2:
			  extraP[dragNum[2]].setVisible(true);
			  extraP[dragNum[2]].setBounds(e.getX(), e.getY(), 50, 50);
			  back.add(extraP[dragNum[2]]);break;
		  case 3:
			  extraS[dragNum[3]].setVisible(true);
			  extraS[dragNum[3]].setBounds(e.getX(), e.getY(), 50, 50);
			  back.add(extraS[dragNum[3]]);break;
		  case 4:
			  extraA[dragNum[4]].setVisible(true);
			  extraA[dragNum[4]].setBounds(e.getX(), e.getY(), 50, 50);
			  back.add(extraA[dragNum[4]]);break;
		}
	}
	@Override
	public void mouseMoved(MouseEvent e)
	{
		instance.setTitle("Meeting Room"+" ["+e.getX()+","+e.getY()+"]");
	}
	@Override
	public void mouseClicked(MouseEvent e)
	{
		instance.setTitle("Meeting Room (Mouse Clicked)");
		clickTime++;
		if(clickTime % 2 == 0)
			ViewStatusShell.Cdiary();
	}
	@Override
	public void mousePressed(MouseEvent e)
	{
		check(e);
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(check(e)) return ;
		if(isDrag)
		{
		  isDrag = false;
		  switch(Dkind)
		  {
		     case 0:
			   ManDialog md = new ManDialog(instance, 3, 0,ontManager);
			   if(isExit)
				  {
					  //isExit = false;
					  extraM[dragNum[0]].setVisible(false);
					  return ;
				  }
			   //extraM[dragNum[0]].setLocation(100,100);
			   //System.out.println(num_man);
			   extraM[dragNum[0]].setBounds(460 + 10 * dragNum[0], 80 + 10 * dragNum[0], 50, 50);
			   //extraM[dragNum[0]].setVisible(true);
			   //back.add(extraM[dragNum[0]]);
			   break;
		     case 1:
			   LightDialog ld = new LightDialog(instance, 3, 0);
			   if(isExit)
				  {
					  //isExit = false;
					  extraL[dragNum[1]].setVisible(false);
					  return ;
				  }
			   extraL[dragNum[1]].setBounds(140 + 10 * dragNum[1], 10 + 10 * dragNum[1], 50, 50);
			   break;
		     case 2:
			   ProDialog pd = new ProDialog(instance, 3, 0);
			   if(isExit)
				  {
					  //isExit = false;
					  extraP[dragNum[2]].setVisible(false);
					  return ;
				  }
			   extraP[dragNum[2]].setBounds(355 + 10 * dragNum[2], 310 + 10 * dragNum[2], 50, 50);
			   break;
		     case 3:
			   ScrDialog sd = new ScrDialog(instance, 3, 0);
			   if(isExit)
				  {
					  //isExit = false;
					  extraS[dragNum[3]].setVisible(false);
					  return ;
				  }
			   extraS[dragNum[3]].setBounds(610 + 10 * dragNum[3], 310 + 10 * dragNum[3], 50, 50);
			   break;
		     case 4:
			   ACDialog ad = new ACDialog(instance, 3, 0);
			   if(isExit)
				  {
					  //isExit = false;
					  extraA[dragNum[4]].setVisible(false);
					  return ;
				  }
			   extraA[dragNum[4]].setBounds(20 + 10 * dragNum[4], 20 + 10 * dragNum[4], 50, 50);
			   break;
		  }
		  dragNum[Dkind]++;
		  isExit = true;
		}
	}
	@Override
	public void mouseEntered(MouseEvent e)
	{
		instance.setTitle("Meeting Room (Mouse Entered)");
	}
	@Override
	public void mouseExited(MouseEvent e)
	{
		instance.setTitle("Meeting Room");
	}
    private boolean check(MouseEvent e)
    {
    	if(e.isPopupTrigger())
    	{
    		jpm.show(e.getComponent(), e.getX(), e.getY());
    	    return true;
    	}
    	return false;
    }
    
    class MyWListener extends WindowAdapter
    {
    	@Override
    	public void windowDeactivated(WindowEvent e)
    	{
    		System.out.println("no active");
    	}
    	@Override
    	public void windowActivated(WindowEvent e)
    	{
    		System.out.println("active");
    	}
    	@Override
		public void windowOpened(WindowEvent e)
		{
			// TODO Auto-generated method stub
			
		}
    	@Override
		public void windowClosing(WindowEvent e)
		{
			// TODO Auto-generated method stub
			
		}
    	@Override
		public void windowClosed(WindowEvent e)
		{
			// TODO Auto-generated method stub
			
		}
    	@Override
		public void windowIconified(WindowEvent e)
		{
			// TODO Auto-generated method stub
			
		}
    	@Override
		public void windowDeiconified(WindowEvent e)
		{
			// TODO Auto-generated method stub
			
		}
    }
}
