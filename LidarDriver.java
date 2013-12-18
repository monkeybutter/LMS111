package lidar;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class LidarDriver {
	static PrintWriter out;
    static BufferedReader in;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		File file = new File("Scan.txt");
    	FileWriter writer = new FileWriter(file);
    	BufferedWriter buffer = new BufferedWriter(writer);
		
		Socket socket = new Socket("169.254.34.226", 2111);
		out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        send("sMN SetAccessMode 3 F4724744");
        System.out.println(receive());
        send("sWN LMDscandatacfg 03 00 1 1 0 00 00 0 0 0 0 +1");
        System.out.println(receive());
        send("sEN LMDscandata 1");
        
        String myScan;
        String myDistances;
        
        for (int i = 0; i < 1000; i ++) {
        	myScan = receive();
        	myDistances = parseScan(myScan);
        	buffer.write(myDistances + "\n");
        }
        
		socket.close();
		
		buffer.close();
	}
	
private static String parseScan (String scan) {
        
	String out;
	
	/* Here goes the code that parses the string into another string with
	 * the actual values (out)
	 */
        out = "This are my values";
        return out;
}
	
	private static boolean send (final String cmd) {
        
        out.write(0x02);
        for(int i = 0; i < cmd.length(); i++)
                out.write(cmd.charAt(i));
        out.write(0x03);
        out.write(0x00);
        out.flush();
        return true;
}
	
	private static String receive() {
        
        final StringBuffer buf = new StringBuffer();
        int c = 0;

        /*Find STX*/
        do
                try {
                        c = in.read();
                } catch (final IOException e) {
                        e.printStackTrace();
                }
        while (c == 0);

        /*Read in message*/
        while(true){
                /*Read in character*/
                try {
                        c = in.read();
                } catch (final IOException e) {
                        e.printStackTrace();
                }

                /*End of stream*/
                if(c == -1)
                        break;

                /*Append character if not an ETX*/
                if(c == 0x03)
                        break;
                else
                        buf.append((char)c);
        }
        return buf.toString();
}
}
