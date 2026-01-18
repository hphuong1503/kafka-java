package models;
import java.nio.ByteBuffer;
 interface IMessageBuilder {
    Message setCorrelationId(int correlationId);
    Message setErrorCode(short errorCode);
}

public class Message   {
    private int messageSize;
    private int correlationId;
    private short errorCode;

    public Message(int messageSize, int correlationId) {
        this.messageSize = messageSize;
        this.correlationId = correlationId;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putInt(messageSize); // 4 bytes for message_size
        buffer.putInt(correlationId); // 4 bytes for correlation_id
        buffer.putShort(errorCode); // 2 bytes for error_code
        return buffer.array();
    }

    public void setErrorCode(short errorCode) {
        this.errorCode = errorCode;
    }

    public short getErrorCode() {
        return errorCode;
    } 

}
