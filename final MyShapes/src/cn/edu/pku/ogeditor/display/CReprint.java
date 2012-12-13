package cn.edu.pku.ogeditor.display;

public class CReprint implements Runnable
{
    final int sleepT = 1000;
	public void run()
    {
	  while(true)
      {
    	ViewStatusShell.instance.repaint();
    	ViewStatusShell.clickTime = 0;
    	try
		{
			Thread.sleep(sleepT);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
      }
    }
}
