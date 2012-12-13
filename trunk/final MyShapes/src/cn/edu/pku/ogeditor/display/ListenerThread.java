package cn.edu.pku.ogeditor.display;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;

public class ListenerThread extends Thread
{
	private String path;
	public ListenerThread(String path) {
		this.path = path;
	}
	public void run()
	{
		try
		{

			byte[] buf4 = new byte[4], buf;
//			ServerSocket ss = new ServerSocket();
//			ss.bind(new InetSocketAddress(63391));
			while (true)
			{
//				Socket s = ss.accept();
				ViewStatusShell.init();
				File oldfile = new File(path);
				File file = new File(path+".copy");
				copyFile(oldfile, file);
				
				FileInputStream fi = new FileInputStream(file);
//				InputStream fi = s.getInputStream();
				//FileInputStream is = new FileInputStream("NetStream");
				fi.read(buf4);
				int opCode = ViewStatusShell.bytes2i(buf4);
				//System.out.println("opCode:"+opCode);
				if (opCode == 2)
				{
					fi.read(buf4);
					int deviceNum = ViewStatusShell.bytes2i(buf4), attrNum = 0, len;
					//System.out.println("deviceNum:"+deviceNum);
					int i, j, k, l;// i,j,k,l of the function run own
					for (i = 0; i < deviceNum; i++)
					{
						fi.read(buf4);
						attrNum = ViewStatusShell.bytes2i(buf4);
						//System.out.println("attrNum:"+attrNum);
						String value = new String(),//属性值
						atName = new String(), //属性名
						name = new String(), //设备名
						Cname = new String();//设备类型
						String[] tempn, tempv;
						int tempType[], t = 0;
						t = 0;
						tempn = new String[20];
						tempv = new String[20];
						tempType = new int[20];
						for (j = 0; j < attrNum; j++)
						{
							int temp;
							fi.read(buf4);
							len = ViewStatusShell.bytes2i(buf4);//属性名长度
							buf = new byte[len];
							fi.read(buf);
							atName = "";
							for (k = 0; k < len; k++)
								atName += (char) buf[k];
							//System.out.println("atName:"+atName);
							fi.read(buf4);
							len = ViewStatusShell.bytes2i(buf4);
							//System.out.println("len:"+len);
							value = "";
							if (len > 0)
							{
								buf = new byte[len];
								fi.read(buf);
								for (k = 0; k < len; k++)
									value += (char) buf[k];
							}
							else if (len == -1 || len == -3)
							{
								fi.read(buf4);
								int tempi = ViewStatusShell.bytes2i(buf4);
								if(len == -1)
								  value = Integer.toString(tempi);
								else
								{
									if(tempi == 1)
									{
										value = "true";
									}
									else
										value = "false";
								}
							}
							else if (len == -2)
							{
								fi.read(buf4);
								temp = ViewStatusShell.bytes2i(buf4);
								long r = temp << 32;
								fi.read(buf4);
								temp = ViewStatusShell.bytes2i(buf4);
								r |= temp;
								double d = Double.longBitsToDouble(r);
								value = Double.toString(d);
							}
							else if(len == -4)
							{
								fi.read(buf4);
								int ib = ViewStatusShell.bytes2i(buf4);
								//System.out.println("ib:"+ib);
								float f = Float.intBitsToFloat(ib);
								value = Float.toString(f);
							}
							//System.out.println("value:"+value);
							/*for(int ll=0;ll<4;ll++)
								System.out.print(buf4[ll]+ " ");
							System.out.println();*/
							if (atName.equals("class"))
							{
								Cname = value;
							}
							else if (atName.equals("name"))
							{
								name = value;
							}
							else
							{
								tempn[t] = atName;
								tempv[t] = value;
								tempType[t] = len;
								t++;
							}
						}
						//System.out.println("Cname:"+Cname);
						//System.out.println("name:"+name);
						if (Cname.equals("Person"))
						{
							ViewStatusShell.at[0][0].dNum++;
							//Main.at[0][0].num = t;
							ViewStatusShell.at[0][ViewStatusShell.num_man + 6].num = t;
							ViewStatusShell.manName[ViewStatusShell.num_man + 6] = name;
							for (k = 0; k < t; k++)
							{
								ViewStatusShell.at[0][ViewStatusShell.num_man + 6].name[k] = tempn[k];
								ViewStatusShell.at[0][ViewStatusShell.num_man + 6].value[k] = tempv[k];
								ViewStatusShell.at[0][ViewStatusShell.num_man + 6].type[k] = tempType[k];
							}
							ViewStatusShell.num_man++;
							ViewStatusShell.extra_num_man++;
							ViewStatusShell.totalNum++;
							ViewStatusShell.display_man.setText("There are(is) " + ViewStatusShell.num_man
									+ " people\n");
							ViewStatusShell.extra_man.setText("  *" + ViewStatusShell.extra_num_man + "\n");
							ViewStatusShell.getInfo_man();
						}
						else if (Cname.equals("Light")||Cname.equals("Front_Light")||Cname.equals("Rear_Light"))
						{

							ViewStatusShell.at[1][0].dNum++;
							//Main.at[1][0].num = t;
							ViewStatusShell.at[1][ViewStatusShell.num_light + 5].num = t;
							ViewStatusShell.lightName[ViewStatusShell.num_light + 5] = name;
							for (k = 0; k < t; k++)
							{
								ViewStatusShell.at[1][ViewStatusShell.num_light + 5].name[k] = tempn[k];
								ViewStatusShell.at[1][ViewStatusShell.num_light + 5].value[k] = tempv[k];
								ViewStatusShell.at[1][ViewStatusShell.num_light + 5].type[k] = tempType[k];
							}
							ViewStatusShell.num_light++;
							ViewStatusShell.totalNum++;
							ViewStatusShell.extra_light.setText("  *" + ViewStatusShell.num_light + "\n");
							ViewStatusShell.getInfo_light();
						}
						else if (Cname.equals("Projector"))
						{
							ViewStatusShell.at[2][0].dNum++;
							//Main.at[2][0].num = t;
							ViewStatusShell.at[2][ViewStatusShell.num_pro + 1].num = t;
							ViewStatusShell.proName[ViewStatusShell.num_pro + 1] = name;
							for (k = 0; k < t; k++)
							{
								ViewStatusShell.at[2][ViewStatusShell.num_pro + 1].name[k] = tempn[k];
								ViewStatusShell.at[2][ViewStatusShell.num_pro + 1].value[k] = tempv[k];
								ViewStatusShell.at[2][ViewStatusShell.num_pro + 1].type[k] = tempType[k];
							}
							ViewStatusShell.num_pro++;
							ViewStatusShell.totalNum++;
							ViewStatusShell.extra_pro.setText("  *" + ViewStatusShell.num_pro + "\n");
							ViewStatusShell.getInfo_pro();
						}
						else if (Cname.equals("Screen"))
						{
							ViewStatusShell.at[3][0].dNum++;
							//Main.at[3][0].num = t;
							ViewStatusShell.at[3][ViewStatusShell.num_scr + 1].num = t;
							//System.out.println(Main.num_scr+"asd"+t);
							ViewStatusShell.scrName[ViewStatusShell.num_scr + 1] = name;
							for (k = 0; k < t; k++)
							{
								ViewStatusShell.at[3][ViewStatusShell.num_scr + 1].name[k] = tempn[k];
								ViewStatusShell.at[3][ViewStatusShell.num_scr + 1].value[k] = tempv[k];
								ViewStatusShell.at[3][ViewStatusShell.num_scr + 1].type[k] = tempType[k];
							}
							ViewStatusShell.num_scr++;
							ViewStatusShell.totalNum++;
							ViewStatusShell.extra_scr.setText("  *" + ViewStatusShell.num_scr + "\n");
							ViewStatusShell.getInfo_scr();
						}
						else if (Cname.equals("Air_Condition"))
						{
							//Main.at[4][Main.at[4][0].dNum].num = t;
							ViewStatusShell.at[4][0].dNum++;
							ViewStatusShell.at[4][ViewStatusShell.num_AC + 1].num = t;
							ViewStatusShell.ACName[ViewStatusShell.num_AC + 1] = name;
							for (k = 0; k < t; k++)
							{
								ViewStatusShell.at[4][ViewStatusShell.num_AC + 1].name[k] = tempn[k];
								ViewStatusShell.at[4][ViewStatusShell.num_AC + 1].value[k] = tempv[k];
								ViewStatusShell.at[4][ViewStatusShell.num_AC + 1].type[k] = tempType[k];
							}
							ViewStatusShell.num_AC++;
							ViewStatusShell.totalNum++;
							ViewStatusShell.extra_AC.setText("  *" + ViewStatusShell.num_AC + "\n");
							ViewStatusShell.getInfo_AC();
						}
						else
						{
							//System.out.println("totalKind:"+Main.totalKind);
							boolean haveFind = false;
							for (k = 0; k < ViewStatusShell.totalKind - 5; k++)
							{
								if (ViewStatusShell.otherName[k].equals(Cname))// search for
																// such kind of device
								{
									haveFind = true;
									break;
								}
							}
							//System.out.println("k:"+k+"\nhaveFind:"+haveFind);
							//System.out.println("dNum:"+Main.at[k+5][0].dNum);
							if (haveFind)
							{
								for (l = 0; l < t; l++)
								{
									ViewStatusShell.at[k + 5][ViewStatusShell.at[k + 5][0].dNum].name[l] = tempn[l];
									ViewStatusShell.at[k + 5][ViewStatusShell.at[k + 5][0].dNum].value[l] = tempv[l];
									ViewStatusShell.at[k + 5][ViewStatusShell.at[k + 5][0].dNum].type[l] = tempType[l];
								}
								ViewStatusShell.at[k + 5][ViewStatusShell.at[k + 5][0].dNum].dName = name;
								ViewStatusShell.at[k + 5][ViewStatusShell.at[k + 5][0].dNum].num = t;
								ViewStatusShell.at[k + 5][0].dNum++;
								ViewStatusShell.totalNum++;
								ViewStatusShell.getInfo(k + 5);
								/*if(Main.at[k+5][0].dNum>1)
									for(i=0;i<Main.at[k+5][0].dNum;i++)
								                System.out.println("Name:"+Main.at[k+5][i].dName);*/
							}
							else
							// not found
							{
								for (k = 0; k < t; k++)
								{
									ViewStatusShell.at[ViewStatusShell.totalKind][0].name[k] = tempn[k];
									ViewStatusShell.at[ViewStatusShell.totalKind][0].value[k] = tempv[k];
									ViewStatusShell.at[ViewStatusShell.totalKind][0].type[k] = tempType[k];
								}
								ViewStatusShell.otherName[ViewStatusShell.totalKind - 5] = Cname;
								ViewStatusShell.at[ViewStatusShell.totalKind][0].className = Cname;
								ViewStatusShell.at[ViewStatusShell.totalKind][0].dName = name;
								ViewStatusShell.at[ViewStatusShell.totalKind][ViewStatusShell.at[ViewStatusShell.totalKind][0].dNum].num = t;
								ViewStatusShell.at[ViewStatusShell.totalKind][0].dNum++;
								ViewStatusShell.Lother[ViewStatusShell.totalKind - 5].setText("Current States of " + Cname + ":");
								ViewStatusShell.setOther[ViewStatusShell.totalKind - 5].setText("Set New States");
								ViewStatusShell.setOther[ViewStatusShell.totalKind - 5].setEnabled(true);
								ViewStatusShell.getInfo(ViewStatusShell.totalKind);
								ViewStatusShell.totalNum++;
								ViewStatusShell.totalKind++;
								int temph = ViewStatusShell.List.getHeight() + 210;
								//System.out.println("qwqw"+temph);
								ViewStatusShell.List.setPreferredSize(new Dimension(320, temph));
								ViewStatusShell.List.setSize(new Dimension(320, temph));
								//temph = Main.List.getHeight() + 340;
								//System.out.println("qwqw"+temph);
							}
						}//here
					}
				}
				//s.close();
				fi.close();
				sleep(3000);
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	private File getCopyFile(String path) {
		File f = new File(path);
		String np = f.getAbsolutePath()+".copy";
		File nf = new File(np);
		if(nf.exists())
			nf.delete();
		
		try {
			nf.createNewFile();
			FileReader fr = new FileReader(f);
			FileWriter fw = new FileWriter(new File(np));
			BufferedReader br = new BufferedReader(fr);
			BufferedWriter bw = new BufferedWriter(fw);
			String line;
			while(null != (line = br.readLine()))
			{
				bw.write(line);
				bw.newLine();
			}
			bw.close();
			br.close();
			fw.close();
			fr.close();
			return nf;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
}