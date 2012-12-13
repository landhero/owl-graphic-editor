package cn.edu.pku.ogeditor.display;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

class ACDialog implements ActionListener
   {
      JDialog jd;
      JLabel Jno, Jvalue, Jat;
      JTextField Fno = new JTextField( 10 ), Fvalue = new JTextField( 5 ), Fat = new JTextField( 5 );
      JButton jb = new JButton( "OK" ), jexit = new JButton( "Exit" );
      int manner, no_;
      int num = -1, atNo = -1;
      ACDialog( JFrame jf, int m, int n_ )
      {
          ViewStatusShell.notifyPause();
    	  manner = m;
          no_ = n_;
          if(manner == 1)
    	    jd = new JDialog( jf, "Set air conditioner's attribute", true );
          else if(manner == 2 || manner == 3)
        	  jd = new JDialog( jf, "Add air conditioners", true ); 
          else if(manner == 4)
        	  jd = new JDialog( jf, "Delete conditioners", true );
          jd.getContentPane().setLayout( new FlowLayout() );
          Jno = new JLabel( "Please input the air conditioner's name:" );
          jd.getContentPane().add( Jno );
          if(manner == 2)   Fno.setText( ViewStatusShell.ACName[0] );
          Fno.setHorizontalAlignment(JTextField.CENTER);
          Fno.addActionListener( this );
          jd.getContentPane().add( Fno );
          Jat = new JLabel( "Please input the attribute's name:" );
          jd.getContentPane().add( Jat );
          Fat.setText( "" );
          Fat.setHorizontalAlignment(JTextField.CENTER);
          Fat.addActionListener( this );
          jd.getContentPane().add( Fat );
          Jvalue = new JLabel( "Please input the air conditioner's value:" );
          jd.getContentPane().add( Jvalue );
          if(manner == 4)
          {
        	  Fat.setEditable(false);   
        	  Fvalue.setEditable(false);
          }
          Fvalue.setHorizontalAlignment(JTextField.CENTER);
          Fvalue.addActionListener( this );
          jd.getContentPane().add( Fvalue );
          jb.addActionListener( this );    
          jd.getContentPane().add( jb );
          jexit.addActionListener( this );
          jd.getContentPane().add( jexit );
          jd.setResizable(false);
          jd.setSize( 300, 170 );
          jd.setLocation( 350, 250 );
          jd.setVisible( true ); 
      }
      
      public void actionPerformed( ActionEvent event )
      {
           if( event.getSource() == jexit )
           {
             ViewStatusShell.isExit = true;
        	 jd.dispose();
             ViewStatusShell.notifyContinue();
             return ;
           }
           ViewStatusShell.isExit = false;
           String t = new String(), name = new String(), atName = new String();
           boolean flag = true;
           int temp = 0;
           try
           {
               if(!ViewStatusShell.toggleA)
            	   temp = ViewStatusShell.num_AC + 1;
               else
            	   temp = ViewStatusShell.num_AC;
        	   name = Fno.getText();
        	   if(manner == 1 || manner == 4)
                  num = searchn(name);
        	   else if(manner == 2)
        	   {
        		   num = no_;
        	   }
        	   else if(manner == 3)
        	   {
        		   num = temp - 1;
        	   }
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
           /*if( num <= 0 || num > Main.num_AC )
             JOptionPane.showMessageDialog( Main.instance, "Invalid input !", 
                            "Warning", JOptionPane.WARNING_MESSAGE );
		   else */
           //System.out.println("num:"+num+"\natNo:"+atNo+"\n");
           if(manner != 4 && atName.length() * t.length() == 0)
				JOptionPane.showMessageDialog(ViewStatusShell.instance, "invalid name !",
						"Warning", JOptionPane.WARNING_MESSAGE);
		   else if(manner == 1 && atNo == -1 && num != -1 )
           {
        	   ViewStatusShell.at[4][num].name[ViewStatusShell.at[4][num].num] = atName;
        	   ViewStatusShell.at[4][num].value[ViewStatusShell.at[4][num].num] = t;
        	   ViewStatusShell.at[4][num].type[ViewStatusShell.at[4][num].num] = t.length();
        	   ViewStatusShell.at[4][num].num++;
        	   ViewStatusShell.getInfo_AC(); 
        	   ViewStatusShell.outstream();
               jb.setEnabled(false);
               jd.dispose();
               ViewStatusShell.notifyContinue();
               return ;
           }
           else if(manner != 2 && manner != 3 &&(num == -1 || atNo == -1) && !(manner == 4 && num != -1))
				JOptionPane.showMessageDialog(ViewStatusShell.instance, "Invalid name !",
						"Warning", JOptionPane.WARNING_MESSAGE);
           else if((manner == 2 || manner == 3 ) && dup(name) > 0)
				JOptionPane.showMessageDialog(ViewStatusShell.instance, "The same name !",// + dup(name),
						"Warning", JOptionPane.WARNING_MESSAGE);       	   
           else if( flag )
           {
        	   if(manner == 4)
        	   {
         		  if(num == 0)
         		  {
         	             JOptionPane.showMessageDialog( ViewStatusShell.instance, "You cannot delete the graph by the menu!", 
                                  "Warning", JOptionPane.WARNING_MESSAGE );       			  
         		  }
         		  else
         		  {
        		     for(int i = num; i + 1 < temp; i++)
        		     {
        		   	      ViewStatusShell.ACName[i] = ViewStatusShell.ACName[i + 1];
        		    	  ViewStatusShell.at[4][i] = ViewStatusShell.at[4][i + 1];
        		     }
       		         ViewStatusShell.num_AC--;
    		         ViewStatusShell.totalNum--;
    		         if(ViewStatusShell.dragNum[4] > 0)
    		         {
    		        	 ViewStatusShell.dragNum[4]--;
    		        	 ViewStatusShell.extraA[ViewStatusShell.dragNum[4]].setVisible(false);
    		         }
         		  }
        	   }
        	   else if(manner == 2)
        	   {
   			       ViewStatusShell.num_AC++;
   			       ViewStatusShell.totalNum++;
				   ViewStatusShell.Bair.setIcon(ViewStatusShell.Iair);
				   ViewStatusShell.toggleA = !ViewStatusShell.toggleA;
        		   ViewStatusShell.at[4][0].num  = 1;
        	       ViewStatusShell.at[4][0].name[0] = atName;
        	       ViewStatusShell.at[4][0].value[0] = t;
        		   ViewStatusShell.at[4][0].dName = name;
        		   ViewStatusShell.ACName[num] = name;
        		   ViewStatusShell.ACName[0] = name;
        		   ViewStatusShell.at[4][0].type[0] = t.length();
        	   }
        	   else if(manner == 1)
        	   {
        		   ViewStatusShell.at[4][num].value[atNo] = t;
        	   }
        	   else if(manner == 3)
        	   {
        		   ViewStatusShell.num_AC++;
   			       ViewStatusShell.totalNum++;
        		   int tmp = ViewStatusShell.num_AC;
        		   if(ViewStatusShell.toggleA)  tmp--;
        		   ViewStatusShell.at[4][tmp].num++;
        		   ViewStatusShell.at[4][tmp].name[0] = atName;
        		   ViewStatusShell.at[4][tmp].value[0] = t;
        		   ViewStatusShell.at[4][tmp].dName = name;
        		   ViewStatusShell.ACName[tmp] = name;
        		   ViewStatusShell.at[4][tmp].type[0] = t.length(); 
        		   //Main.ACName[num] = name;
        	   }
        	   ViewStatusShell.extra_AC.setText("  *" + ViewStatusShell.num_AC + "\n");
        	   ViewStatusShell.getInfo_AC(); 
        	   ViewStatusShell.outstream();
               jb.setEnabled(false);
               jd.dispose();
               ViewStatusShell.notifyContinue();
               return ;
           } 
      } 
		private int dup(String name)
	  {
			int i, temp = 0, s = 0;
            if(!ViewStatusShell.toggleA)
         	   temp = ViewStatusShell.num_AC + 1;
            else
         	   temp = ViewStatusShell.num_AC;
			for(i = 0 ; i < temp; i++)
			{
				if(i == 0 && !ViewStatusShell.toggleA)  continue;
				if(ViewStatusShell.ACName[i].equals(name))
					s++;
			}
			return s;
	  }

		private int searcha(String atName)
		{
			int i;
			for( i = 0; i < ViewStatusShell.at[4][num].num; i++)
			{
				if(ViewStatusShell.at[4][num].name[i].equals(atName))
					break;
			}
			if(i == ViewStatusShell.at[4][num].num)
			  return -1;
			else return i;
		}

		private int searchn(String name)
		{
			int i, temp = 0;
            if(!ViewStatusShell.toggleA)
         	   temp = ViewStatusShell.num_AC + 1;
            else
         	   temp = ViewStatusShell.num_AC;
			for(i = 0 ; i < temp; i++)
			{
				if(ViewStatusShell.ACName[i].equals(name))
					break;
			}
			if(i == temp)
				return -1;
			else
				return i;
		}
   }