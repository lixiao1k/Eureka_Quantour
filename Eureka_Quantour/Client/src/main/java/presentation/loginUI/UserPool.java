package presentation.loginUI;

import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import org.controlsfx.control.Notifications;

import logic.service.ClientLogicInterface;
import rmi.RemoteHelper;

public class UserPool implements Runnable{
	private String username;
	private RemoteHelper remoteHelper = RemoteHelper.getInstance();
	private ClientLogicInterface cl;
	public UserPool()
	{
		cl = remoteHelper.getClientLogic();
	}
	public void setName(String userName)
	{
		this.username=userName;
	}
	@Override
	public void run() {
		while(true)
		{
			if(username==null)
			{
				
			}
			else
			{
				
				try {
					cl.getConn(username);
				} catch (RemoteException e) {
					JOptionPane.showMessageDialog(null, "网络异常", "网络断开，程序即将关闭！", JOptionPane.ERROR_MESSAGE); 
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					System.exit(0);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
