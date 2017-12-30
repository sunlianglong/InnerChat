import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class MyServer{
	public static List<Socket> socketList = Collections.synchronizedList(new ArrayList<>());
	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(30000);
		while(true){
		
			Socket s = ss.accept();
			socketList.add(s);
		
			new Thread(new ServerThread(s)).start();
			System.out.println("connect success");
		
		}
	}
}

class ServerThread implements Runnable{
	Socket s = null;
	BufferedReader br = null;
	public ServerThread(Socket s) throws Exception{
		this.s = s;
		br = new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));
	}

	public void run() {
		try {
			String content = null;
			while ((content = readFromClient()) != null) {
				for(Iterator<Socket> it = MyServer.socketList.iterator();it.hasNext();){
					Socket s = it.next();
					try{
						OutputStream os =s.getOutputStream();
						os.write((content+"\n").getBytes("utf-8"));
					}catch(SocketException e){
						e.printStackTrace();
						it.remove();
						System.out.println(MyServer.socketList);
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private String readFromClient(){
		try {
			return br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			MyServer.socketList.remove(s);
		}
		return null;
	}
}