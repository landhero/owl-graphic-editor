package cn.edu.pku.ogeditor.display;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class DiaryDialog implements ActionListener
{
    JDialog jd;
    static JTextArea display = new JTextArea(10, 20);
    JButton jok = new JButton("OK");
    JPanel jp = new JPanel();
	DiaryDialog(JFrame jf)
    {
    	jd = new JDialog(jf, "Diary");
    	jd.setModal(true);
    	display.setText(DiaryText.getS());
    	display.setCaretPosition(0);
    	display.setEditable(false);
    	jd.getContentPane().setLayout( new FlowLayout() );
    	JScrollPane js = new JScrollPane(display);
    	//js.setBounds(0, 0, 100, 100);
    	jp.add(js);
    	//jd.getContentPane().add(js);
        jok.addActionListener( this );
        jd.getContentPane().add( jp );
        jd.getContentPane().add( jok );
        jd.setResizable(false);
        jd.setSize( 300, 300 );
        jd.setLocation( 350, 250 );
        jd.setVisible( true ); 
    }
	public void actionPerformed(ActionEvent e)
	{
		jd.dispose();
	}

}
