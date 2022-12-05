package org.example.helper;

import lombok.SneakyThrows;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class PacketBuilder {
    private byte[] header;
    private byte[] ipPart;
    private byte[] udpPart;
    private byte[] payload;
    private int addCount = 0;


    public PacketBuilder addHeader(String iface){

        /* NPF_Loopback Length = 4 bytes other 14? */
        header = new byte[(iface.equals("\\Device\\NPF_Loopback") ? 4 : 14)];
        /* set NPF_Loopback as iface to use. WORKS ONLY FOR WINDOWS*/
        for (int i = 0, j = 7; i < 1; i++, j++) header[i] = longToBytes(0x02)[j];

        return this;
    }

    @SneakyThrows
    public PacketBuilder addIpPart(String srcAddress, String dstAddress) {

        /* IP Header Length = 20 bytes */
        ipPart = new byte[20];
        for (int i = 0 + addCount, j = 7; i < 1 + addCount; i++, j++) ipPart[i] = longToBytes(69)[j];
        /* Differentiated Services Field */
        for (int i = 1 + addCount, j = 7; i < 2 + addCount; i++, j++) ipPart[i] = longToBytes(0x00)[j];
        /* Total Length add in build */
        /* Identification - for fragmented packages */
        for (int i = 4 + addCount, j = 6; i < 6 + addCount; i++, j++) ipPart[i] = longToBytes(33500)[j];
        /* Flag, Fragment Offset - for fragmented packages */
        for (int i = 6 + addCount, j = 6; i < 8 + addCount; i++, j++) ipPart[i] = longToBytes(0x00)[j];
        /* Time to Live - max limit for moving through the network */
        for (int i = 8 + addCount, j = 7; i < 9 + addCount; i++, j++) ipPart[i] = longToBytes(128)[j];
        /* Protocol - UDP */
        for (int i = 9 + addCount, j = 7; i < 10 + addCount; i++, j++) ipPart[i] = longToBytes(17)[j];
        /* Header Checksum, can be 0x00 if it is not calculated */
        for (int i = 10 + addCount, j = 6; i < 12 + addCount; i++, j++) ipPart[i] = longToBytes(0x00)[j];
        /* Source address */
        for (int i = 12 + addCount, j = 0; i < 16 + addCount; i++, j++) ipPart[i] = InetAddress
                .getByName(srcAddress).getAddress()[j];
        /* Destination address */
        for (int i = 16 + addCount, j = 0; i < 20 + addCount; i++, j++) ipPart[i] = InetAddress
                .getByName(dstAddress).getAddress()[j];

        return this;
    }

    public PacketBuilder addUdpPart(long portToSend) {
        if (portToSend <= 1000 || portToSend >= 65535){
            throw new RuntimeException("Port must be greater than 1000 and less than 65535");
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

    public PacketBuilder addPayload(String data) {

        if (header != null && udpPart != null) {
            /* payload */
            payload = data.getBytes();

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
            if (ipPart == null)
                this.addIpPart("127.0.0.1", "255.255.255.255");

            /* Total Length */
            int totalLength = ipPart.length + udpPart.length + payload.length;
            for (int i = 2 + addCount, j = 6; i < 4 + addCount; i++, j++) ipPart[i] = longToBytes(totalLength)[j];

            /* collect packet */
            byte [] packet = new byte[totalLength + header.length];
            System.arraycopy(header, 0, packet, 0, header.length);
            System.arraycopy(ipPart, 0, packet, header.length, ipPart.length);
            System.arraycopy(udpPart, 0, packet, header.length + ipPart.length, udpPart.length);
            System.arraycopy(payload, 0, packet, header.length + ipPart.length
                    + udpPart.length, payload.length);

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
