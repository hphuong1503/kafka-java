package models;

import java.nio.ByteBuffer;

public class Message {
    private int messageSize;
    private int correlationId;

    public Message(int messageSize, int correlationId) {
        this.messageSize = messageSize;
        this.correlationId = correlationId;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(messageSize); // 4 bytes for message_size
        buffer.putInt(correlationId); // 4 bytes for correlation_id
        return buffer.array();
    }
}
