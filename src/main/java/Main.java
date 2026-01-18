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
      short request_api_version = ByteBuffer.wrap(buffer, 6, 2).getShort();
      int message_size = ByteBuffer.wrap(buffer, 0, 4).getInt();
      short request_api_key = ByteBuffer.wrap(buffer, 4, 2).getShort();

      System.out.println("Received request with API key: " + request_api_key + ", API version: " + request_api_version + ", Correlation ID: " + correlationId + ", Message Size: " + message_size);

      sendCorrelationID(clientSocket, correlationId, request_api_version);

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

  private static void sendCorrelationID(Socket clientSocket, int correlationId, short request_api_version ) throws IOException {
    Message message = new Message(0, correlationId);
    checkApiVersionAndSetError(request_api_version, message);

    // Send the response to the client
    clientSocket.getOutputStream().write(message.toBytes());
    clientSocket.getOutputStream().flush();
  }

  private static void checkApiVersionAndSetError(short request_api_version, Message message) {
    if (request_api_version < 5){
      message.setErrorCode(Errors.NONE);
    } else {
      message.setErrorCode(Errors.UNSUPPORTED_VERSION); 
    }
  }
}
