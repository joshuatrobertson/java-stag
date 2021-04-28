package stag;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class StagServer
{
    private static final Logger logger = Logger.getLogger(StagServer.class.getName());
    final GameMain gameMain;

    public static void main(String args[])
    {
        if(args.length != 2) logger.warning("Usage: java StagServer <entity-file> <action-file>");
        else new StagServer(args[0], args[1], 8888);
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {
        gameMain = new GameMain(entityFilename, actionFilename);
        gameMain.loadGame();
        try {
            var ss = new ServerSocket(portNumber);
            logger.info("Server listening");
            while(true) acceptNextConnection(ss);
        } catch(IOException ioe) {
            logger.log(Level.SEVERE, ioe.toString(), ioe);
        }
    }

    private void acceptNextConnection(ServerSocket ss)
    {
        try {
            // Next line will block until a connection is received
            var socket = ss.accept();
            var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            logger.log(Level.SEVERE, ioe.toString(), ioe);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException
    {
        String command = in.readLine();
        out.write(gameMain.runCommand(command.toLowerCase()));
    }
}
