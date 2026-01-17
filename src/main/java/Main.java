import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import models.Message;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible
    // when running tests.
    System.err.println("Logs from your program will appear here!");

    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    int port = 9092;
    try {
      serverSocket = new ServerSocket(port);
      // Since the tester restarts your program quite often, setting SO_REUSEADDR
      // ensures that we don't run into 'Address already in use' errors
      serverSocket.setReuseAddress(true);
      // Wait for connection from client.
      clientSocket = serverSocket.accept();

      byte[] buffer = new byte[1024];
      clientSocket.getInputStream().read(buffer);

      int correlationId = ByteBuffer.wrap(buffer, 8, 4).getInt();

      sendCorrelationID(clientSocket, correlationId);

    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    } finally {
      try {
        if (clientSocket != null) {
          clientSocket.close();
        }
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
    }
  }

  private static void sendCorrelationID(Socket clientSocket, int correlationId) throws IOException {
    Message message = new Message(0, correlationId);
    byte[] responseBytes = message.toBytes();

    // Send the response to the client
    clientSocket.getOutputStream().write(responseBytes);
    clientSocket.getOutputStream().flush();
  }
}
