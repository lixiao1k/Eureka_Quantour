package runner;

import rmi.RemoteHelper;

/*
 * @author: lxd
 * @time: 2017/3/12
 */
public class ServerRunner {
	public ServerRunner(){
		new RemoteHelper();
	}
	public static void main(String[] args) {
		new ServerRunner();
	}

}
