package cn.edu.pku.ogeditor.display;
   public class Attribute
   {
        String[] name, value;
        String className, dName;//device name
        int dNum,num;//number of device, number of attribute
        int[] type; 
        Attribute()
        {
           num = 0;
           dNum = 0;
           name = new String[ViewStatusShell.MAX];
           value = new String[ViewStatusShell.MAX]; 
           type = new int[ViewStatusShell.MAX];       
        }
        void reset()
        {
           num = 0;
           dNum = 0;             
        }
   }