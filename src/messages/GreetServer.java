/**
 * @author		GigiaJ
 * @filename	GreetServer.java
 * @date		Mar 27, 2020
 * @description 
 */

package messages;

import java.net.ServerSocket;

public class GreetServer {
    public GreetServer() {
    }

    public static void main(String[] args) {
        try {
            String[] arguments = new String[]{"127.0.0.1", "80", "9999"};
            if (arguments.length == 3) {
                String host = arguments[0];
                int remoteport = Integer.parseInt(arguments[1]);
                int localport = Integer.parseInt(arguments[2]);
                System.out.println("Starting proxy for " + host + ":" + remoteport + " on port " + localport);
                @SuppressWarnings("resource")
				ServerSocket server = new ServerSocket(localport);
                
                while(true) {
                    new ThreadProxy(server.accept(), host, remoteport);
                }
                
            }

            throw new IllegalArgumentException("Wrong number of arguments.");
        } catch (Exception var6) {
            System.err.println(var6);
            System.err.println("Usage: java ProxyMultiThread <host> <remoteport> <localport>");
        }
    }
}
