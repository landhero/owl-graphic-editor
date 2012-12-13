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

class ScrDialog implements ActionListener
   {
      JDialog jd;
      JLabel Jno, Jvalue, Jat;
      JTextField Fno = new JTextField( 10 ), Fvalue = new JTextField( 5 ), Fat = new JTextField( 5 );
      JButton jb = new JButton( "OK" ), jexit = new JButton( "Exit" );
      int manner, no_;
      int num = -1, atNo = -1;
      ScrDialog( JFrame jf, int m, int n_ )
      {
          ViewStatusShell.notifyPause();
    	  manner = m;
          no_ = n_;
          if(manner == 1)
    	    jd = new JDialog( jf, "Set screen's attribute", true );
          else if(manner == 2 || manner == 3)
        	  jd = new JDialog( jf, "Add screens", true ); 
          else if(manner == 4)
        	  jd = new JDialog( jf, "Delete screens", true );
          jd.getContentPane().setLayout( new FlowLayout() );
          Jno = new JLabel( "Please input the screen's name:" );
          jd.getContentPane().add( Jno );
          if(manner == 2) Fno.setText( ViewStatusShell.scrName[0] );
          Fno.setHorizontalAlignment(JTextField.CENTER);
          Fno.addActionListener( this );
          jd.getContentPane().add( Fno );
          Jat = new JLabel( "Please input the attribute's name:" );
          jd.getContentPane().add( Jat );
          Fat.setText( "" );
          Fat.setHorizontalAlignment(JTextField.CENTER);
          Fat.addActionListener( this );
          jd.getContentPane().add( Fat );
          Jvalue = new JLabel( "Please input the screen's value:" );
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
          jd.setLocation( 450, 250 );
          jd.setVisible( true );   
      }
      public void actionPerformed( ActionEvent event )
      {
           if( event.getSource() == jexit )
           {
             jd.dispose();
             ViewStatusShell.isExit = true;
             ViewStatusShell.notifyContinue();
             return ;
           }
           ViewStatusShell.isExit = false;
           String t = new String(), name = new String(), atName = new String();
           boolean flag = true;
           int temp = 0;
           try
           {
	          if(!ViewStatusShell.toggleS)
	              temp = ViewStatusShell.num_scr + 1;
	          else
	              temp = ViewStatusShell.num_scr;              
        	  name = Fno.getText();
        	  if(manner == 1 || manner == 4)
        	  {
                 num = searchn(name);
        	  }
        	  else if(manner == 2)
        	  {
        		 num = no_;
        		 //Main.scrName[num] = name;
        	  }
        	  else if(manner == 3)
        	  {
        		  num = temp - 1;
        		  //Main.scrName[num] = name;
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
           //System.out.println("num:"+num+"\natNo:"+atNo+"\n");
           if(manner != 4 && atName.length() * t.length() == 0)
				JOptionPane.showMessageDialog(ViewStatusShell.instance, "invalid name !",
						"Warning", JOptionPane.WARNING_MESSAGE);
		   else if(manner == 1 && atNo == -1 && num != -1 )
	           {
	        	   ViewStatusShell.at[3][num].name[ViewStatusShell.at[3][num].num] = atName;
	        	   ViewStatusShell.at[3][num].value[ViewStatusShell.at[3][num].num] = t;
	        	   ViewStatusShell.at[3][num].type[ViewStatusShell.at[3][num].num] = t.length();
	        	   ViewStatusShell.at[3][num].num++;
	        	   ViewStatusShell.getInfo_scr(); 
	        	   ViewStatusShell.outstream();
	               jb.setEnabled(false);
	               jd.dispose();
	               ViewStatusShell.notifyContinue();
	               return ;
	           }
	       else if(manner != 2 && manner != 3 &&(num == -1 || atNo == -1) && !(manner == 4 && num != -1))
             JOptionPane.showMessageDialog( ViewStatusShell.instance, "Invalid input !", 
                            "Warning", JOptionPane.WARNING_MESSAGE );
		   /*else if(num == -1 || atNo == -1)
				JOptionPane.showMessageDialog(Main.instance, "invalid name !",
						"Warning", JOptionPane.WARNING_MESSAGE);*/
           else if((manner == 2 || manner == 3 ) && dup(name) > 0)
					JOptionPane.showMessageDialog(ViewStatusShell.instance, "The same name !",
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
        		        ViewStatusShell.scrName[i] = ViewStatusShell.scrName[i + 1];
        		        ViewStatusShell.at[3][i] = ViewStatusShell.at[3][i + 1];
        		     }
       		         ViewStatusShell.num_scr--;
    		         ViewStatusShell.totalNum--;
    		         if(ViewStatusShell.dragNum[3] > 0)
    		         {
    		        	 ViewStatusShell.dragNum[3]--;
    		        	 ViewStatusShell.extraS[ViewStatusShell.dragNum[3]].setVisible(false);
    		         }
        		  }
        	   }
        	   else if(manner == 2)
        	   {
				   ViewStatusShell.Bscreen.setIcon(ViewStatusShell.Iscreen);
				   ViewStatusShell.toggleS = !ViewStatusShell.toggleS;
				   ViewStatusShell.num_scr++;
				   ViewStatusShell.totalNum++;
        		   ViewStatusShell.at[3][no_].num = 1;
        	       ViewStatusShell.at[3][no_].name[0] = atName;
        	       ViewStatusShell.at[3][no_].value[0] = t;
        		   ViewStatusShell.at[3][no_].dName = name;
        		   ViewStatusShell.scrName[no_] = name;
        		   ViewStatusShell.at[3][no_].type[0] = t.length();
        	   }
        	   else if(manner == 1)
        	   {
        		   ViewStatusShell.at[3][num].value[atNo] = t;
        	   }
        	   else if(manner == 3)
        	   {
   			       ViewStatusShell.num_scr++;
   				   ViewStatusShell.totalNum++;
        		   int tmp = ViewStatusShell.num_scr;
        		   if(ViewStatusShell.toggleS)  tmp--;
        		   ViewStatusShell.at[3][tmp].num++;
        		   ViewStatusShell.at[3][tmp].name[0] = atName;
        		   ViewStatusShell.at[3][tmp].value[0] = t;
        		   ViewStatusShell.at[3][tmp].dName = name;
        		   ViewStatusShell.scrName[tmp] = name;
        		   ViewStatusShell.at[3][tmp].type[0] = t.length(); 
        	   }   
        	   ViewStatusShell.extra_scr.setText("  *" + ViewStatusShell.num_scr + "\n");
        	   ViewStatusShell.getInfo_scr(); 
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
	            if(!ViewStatusShell.toggleS)
	         	   temp = ViewStatusShell.num_scr + 1;
	            else
	         	   temp = ViewStatusShell.num_scr;
				for(i = 0 ; i < temp; i++)
				{
					if(i == 0 && !ViewStatusShell.toggleS)  continue;
					if(ViewStatusShell.scrName[i].equals(name))
						s++;
				}
				return s;
		  }
		private int searcha(String atName)
		{
			int i;
			for( i = 0; i < ViewStatusShell.at[3][num].num; i++)
			{
				if(ViewStatusShell.at[3][num].name[i].equals(atName))
					break;
			}
			if(i == ViewStatusShell.at[3][num].num)
			  return -1;
			else return i;
		}

		private int searchn(String name)
		{
			int i, temp = 0;
            if(!ViewStatusShell.toggleS)
         	   temp = ViewStatusShell.num_scr + 1;
            else
         	   temp = ViewStatusShell.num_scr;
            System.out.println("temp:"+temp);
			for(i = 0 ; i < temp; i++)
			{
				System.out.println("name:"+ViewStatusShell.scrName[i]);
				if(ViewStatusShell.scrName[i].equals(name))
					break;
			}
			if(i == temp)
				return -1;
			else
				return i;
		}
   }