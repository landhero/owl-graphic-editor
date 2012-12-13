package cn.edu.pku.ogeditor.display;

public class DiaryText
{
    static String content = new String();
    static String getS()
    {
    	content += "解决了发送接受的问题"+"\n";
    	content += "设备名不能重复\n";
    	content += "dialog中按下EXIT后正常�?出\n";
    	content += "分开前后灯\n";
    	content += "按钮与输入设备不兼容\n";
    	//content += "2012.5.1\n";
    	content += "解决了不能加入新属�?的问题\n";
    	content += "禁止了空字符串的输入\n";
    	content += "添加拖拽图标功能\n";
    	content += "解决了矩形显示问题\n";
    	content += "美化了set界面\n";
    	content += "解决了添加screen和projector时会输出null:null的bug\n";
    	content += "禁止了对模�?窗口大小的改变\n";
    	content += "增大了鼠标滚轮的灵敏度\n";
    	//content += "5.2\n";
    	content += "增加了set对话框中输入框的事件监听\n";
    	content += "dragNo下标�?�?��\n";
    	content += "定时repaint\n";
    	content += "set采用默认名\n";
    	//content += "5.3\n";
    	content += "添加拖拽添加功能\n";
    	content += "解决处理空串后不能删除的bug\n";
    	content += "增加鼠标事件\n";
    	//5.4
    	content += "解决按钮不能添加默认名设备的bug\n";
    	content += "解决拖曳的设备不能取消的问题\n";
    	return content;
    }
}
