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

class OthDialog implements ActionListener
   {
      JDialog jd;
      JLabel Jno, Jvalue, Jat;
      JTextField Fno = new JTextField( 10 ), Fvalue = new JTextField( 5 ), Fat = new JTextField( 5 );
      JButton jb = new JButton( "OK" ), jexit = new JButton( "Exit" );
      int NO;
      int num = -1, atNo = -1;
      OthDialog( JFrame jf, int n )
      {
    	  ViewStatusShell.notifyPause();
    	  NO = n + 5;
          jd = new JDialog( jf, "Set " + ViewStatusShell.at[NO][0].className + "'s attribute", true );
          jd.getContentPane().setLayout( new FlowLayout() );
          Jno = new JLabel( "Please input the " + ViewStatusShell.at[NO][0].className + "'s name:" );
          jd.getContentPane().add( Jno );
          Fno.setText( "" );
          Fno.setHorizontalAlignment(JTextField.CENTER);
          Fno.addActionListener( this );
          jd.getContentPane().add( Fno );
          Jat = new JLabel( "Please input the attribute's name:" );
          jd.getContentPane().add( Jat );
          Fat.setText( "" );
          Fat.setHorizontalAlignment(JTextField.CENTER);
          Fat.addActionListener( this );
          jd.getContentPane().add( Fat );
          Jvalue = new JLabel( "Please input the " + ViewStatusShell.at[NO][0].className + "'s value:" );
          jd.getContentPane().add( Jvalue );
          Fvalue.setHorizontalAlignment(JTextField.CENTER);
          Fvalue.addActionListener( this );
          jd.getContentPane().add( Fvalue );
          jb.addActionListener( this );    
          jd.getContentPane().add( jb );
          jexit.addActionListener( this );
          jd.getContentPane().add( jexit );
          jd.setResizable(false);
          jd.setSize( 300, 170 );
          jd.setLocation( 450, 250 );
          jd.setVisible( true );   
      }
      public void actionPerformed( ActionEvent event )
      {
           if( event.getSource() == jexit )
           {
             jd.dispose();
             ViewStatusShell.notifyContinue();
             return ;
           }
           String t = new String(), name = new String(), atName = new String();
           boolean flag = true;
           try
           {
               name = Fno.getText();
               num = searchn(name);
        	   //num = Integer.parseInt( Fno.getText() );
              t = Fvalue.getText();
              atName = Fat.getText();
              if(num >= 0)
                 atNo = searcha(atName);
              
              //atNo = Integer.parseInt( Fat.getText() );
           }
           catch( NumberFormatException nfe )
           {
               JOptionPane.showMessageDialog( ViewStatusShell.instance, "You must enter integers", "Invalid Input", JOptionPane.ERROR_MESSAGE ); 
               flag = false; 
           }
           /*if( num < 0 || num > Main.at[NO][0].dNum )
             JOptionPane.showMessageDialog( Main.instance, "Invalid input !", 
                            "Warning", JOptionPane.WARNING_MESSAGE );
		   else*/
		   if(atName.length() * t.length() == 0)
				JOptionPane.showMessageDialog(ViewStatusShell.instance, "invalid name !",
						"Warning", JOptionPane.WARNING_MESSAGE);
		   else if(atNo == -1 && num != -1 )
	           {
	        	   ViewStatusShell.at[NO][num].name[ViewStatusShell.at[NO][num].num] = atName;
	        	   ViewStatusShell.at[NO][num].value[ViewStatusShell.at[NO][num].num] = t;
	        	   ViewStatusShell.at[NO][num].type[ViewStatusShell.at[NO][num].num] = t.length();
	        	   ViewStatusShell.at[NO][num].num++;
	        	   ViewStatusShell.getInfo(NO); 
	        	   ViewStatusShell.outstream();
	               jb.setEnabled(false);
	               jd.dispose();
	               ViewStatusShell.notifyContinue();
	               return ;
	           }
	       else if(num == -1 || atNo == -1)
				JOptionPane.showMessageDialog(ViewStatusShell.instance, "invalid name !",
						"Warning", JOptionPane.WARNING_MESSAGE);
           else if( flag )
           {
              ViewStatusShell.at[NO][num].value[atNo] = t;
              ViewStatusShell.getInfo( NO );  
              jd.dispose();
              ViewStatusShell.outstream();
              ViewStatusShell.notifyContinue();
              return ;
           } 
 
      }
		private int searcha(String atName)
		{
			int i;
			for( i = 0; i < ViewStatusShell.at[NO][num].num; i++)
			{
				if(ViewStatusShell.at[NO][num].name[i].equals(atName))
					break;
			}
			if(i == ViewStatusShell.at[NO][num].num)
			  return -1;
			else return i;
		}

		private int searchn(String name)
		{
			int i;
			for(i = 0 ; i < ViewStatusShell.at[NO][0].dNum; i++)
			{
				if(ViewStatusShell.at[NO][i].dName.equals(name))
					break;
			}
			if(i == ViewStatusShell.at[NO][0].dNum)
				return -1;
			else
				return i;
		}
   }