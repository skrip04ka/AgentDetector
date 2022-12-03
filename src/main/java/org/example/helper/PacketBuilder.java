package org.example.helper;

import jade.core.AID;
import lombok.SneakyThrows;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class PacketBuilder {
    byte[] header;
    byte[] udpPart;
    byte[] payload;
    private int addCount = 0;

    @SneakyThrows
    public PacketBuilder addHeader(String iface, String srcAddress, String dstAddress){

        /* NPF_Loopback Length = 4 bytes other 14? */
        header = new byte[20 + (iface.equals("\\Device\\NPF_Loopback") ? 4 : 14)];
        /* set NPF_Loopback as iface to use. WORKS ONLY FOR WINDOWS*/
        for (int i = 0, j = 7; i < 1; i++, j++) header[i] = longToBytes(0x02)[j];

        /* IP Header Length = 20 bytes */
        for (int i = 4 + addCount, j = 7; i < 5 + addCount; i++, j++) header[i] = longToBytes(69)[j];
        /* Differentiated Services Field */
        for (int i = 5 + addCount, j = 7; i < 6 + addCount; i++, j++) header[i] = longToBytes(0x00)[j];
        /* Total Length add in build*/
        /* Identification - for fragmented packages */
        for (int i = 8 + addCount, j = 6; i < 10 + addCount; i++, j++) header[i] = longToBytes(33500)[j];
        /* Flag, Fragment Offset - for fragmented packages */
        for (int i = 10 + addCount, j = 6; i < 12 + addCount; i++, j++) header[i] = longToBytes(0x00)[j];
        /* Time to Live - max limit for moving through the network */
        for (int i = 12 + addCount, j = 7; i < 13 + addCount; i++, j++) header[i] = longToBytes(128)[j];
        /* Protocol - UDP */
        for (int i = 13 + addCount, j = 7; i < 14 + addCount; i++, j++) header[i] = longToBytes(17)[j];
        /* Header Checksum, can be 0x00 if it is not calculated */
        for (int i = 14 + addCount, j = 6; i < 16 + addCount; i++, j++) header[i] = longToBytes(0x00)[j];
        /* Source address */
        for (int i = 16 + addCount, j = 0; i < 20 + addCount; i++, j++) header[i] = InetAddress
                .getByName(srcAddress).getAddress()[j];
        /* Destination address */
        for (int i = 20 + addCount, j = 0; i < 24 + addCount; i++, j++) header[i] = InetAddress
                .getByName(dstAddress).getAddress()[j];

        return this;
    }

    public PacketBuilder addUdpPart(long portToSend) {
        if (portToSend < 1000){
            throw new RuntimeException("Port must be greater than 1000");
        }

        /* UDP Header Length = 8 bytes
        * 4 byte - port
        * 4 byte - length data + check sum - add in payload*/

        udpPart = new byte[8];
        /* Source port */
        for (int i = 0 + addCount, j = 6; i < 2 + addCount; i++, j++) udpPart[i] = longToBytes(portToSend)[j];
        /* Destination port */
        for (int i = 2 + addCount, j = 6; i < 4 + addCount; i++, j++) udpPart[i] = longToBytes(portToSend)[j];
        return this;
    }

    public PacketBuilder addPayload(AID myAgent) {

        if (header != null && udpPart != null) {
            /* payload */
            payload = JsonParser.dataToString(myAgent).getBytes();

            /* continue UDP header */
            /* Length */
            int length = udpPart.length + payload.length;
            for (int i = 4 + addCount, j = 6; i < 6 + addCount; i++, j++) udpPart[i] = longToBytes(length)[j];
            /* Checksum, can be 0x00 if it is not calculated */
            for (int i = 6 + addCount, j = 8; i < 4 + addCount; i++, j++) udpPart[i] = longToBytes(0x0000)[j];

            return this;

        } else {
            throw new RuntimeException("not all fields are filled");
        }
    }

    public byte[] build() {
        if (header != null && udpPart != null && payload != null) {
            /* Total Length */
            int totalLength = header.length + udpPart.length + payload.length;
            for (int i = 6 + addCount, j = 6; i < 8 + addCount; i++, j++) header[i] = longToBytes(totalLength)[j];

            /* collect packet */
            byte [] packet = new byte[totalLength];
            System.arraycopy(header, 0, packet, 0, header.length);
            System.arraycopy(udpPart, 0, packet, header.length, udpPart.length);
            System.arraycopy(payload, 0, packet, udpPart.length + header.length, payload.length);

            return packet;

        } else {
            throw new RuntimeException("not all fields are filled");
        }
    }

    private byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

}
