package com.piles.common.util;

/**
 * CRC16相关计算
 * <p>
 * encode: utf-8
 */
public class CRC16Util {

    public static int getCRC(byte[] bytes) {
        int crc = 0x0000;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12)
        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }

        crc &= 0xffff;
        return crc;
    }

    /**
     * 数据校验
     *
     * @param msg
     * @return
     */
    public static boolean checkMsg(byte[] msg) {
        //起始位必须是0x68
        if (0x68 != msg[0]) {
            return false;
        }
        //长度小于3 抛弃
        if (msg.length > 3) {
            byte[] temp = new byte[msg.length - 3];
            System.arraycopy( msg, 1, temp, 0, temp.length );
            int crc = getCRC( temp );
            byte[] crcByte=new byte[]{msg[msg.length-2],msg[msg.length-1]};
            if (Integer.toHexString( crc ).equalsIgnoreCase( Integer.toHexString( BytesUtil.bytesToInt(crcByte,0)) )) {
                return true;
            }
        }
        return false;

    }


    /**
     * 循道 求crc算法，是将数据内容转int累加 然后取模256，返回两位字节
     * @param dataBytes
     * @return
     */
    public static byte[] getXunDaoCRC(byte[] dataBytes) {
        int crc = 0;//默认值
        if(dataBytes == null){
            return null;
        }
        for(int i = 0;i<dataBytes.length;i++){
            byte dataByte = dataBytes[i];
            int y = BytesUtil.bytesToIntLittle(new byte[]{dataByte});
            crc += y;
        }
        crc = crc%256;
        return BytesUtil.intToBytesLittle(crc,2);
    }

    /**
     * 循道 求crc算法，是将数据内容转int累加 然后取模256，返回两位字节
     *
     * @param dataBytes
     * @return
     */
    public static Byte getType3CRC(byte[] dataBytes) {
        int crc = 0;//默认值
        if (dataBytes == null) {
            return null;
        }
        for (int i = 0; i < dataBytes.length; i++) {
            byte dataByte = dataBytes[i];
            int y = BytesUtil.bytesToIntLittle(new byte[]{dataByte});
            crc += y;
        }
        byte[] temp = BytesUtil.intToBytes(crc);

        return temp[temp.length - 1];
    }

    public static void main(String[] args) {
        byte[] msg = new byte[]{
                (byte) 0xaa, (byte) 0xf5, (byte) 0x6d, (byte) 0x0, (byte) 0x3, (byte) 0x2e, (byte) 0x6a, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x2, (byte) 0x3c, (byte) 0x0, (byte) 0x0, (byte) 0x2, (byte) 0x1, (byte) 0x3, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x20, (byte) 0x19, (byte) 0x1, (byte) 0x6, (byte) 0x10, (byte) 0x35, (byte) 0x14, (byte) 0xff, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x1d, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xe5
        };
        byte[] crcBytes = BytesUtil.copyBytes(msg, msg.length - 1, 1);
        byte checkBytes = CRC16Util.getType3CRC(BytesUtil.copyBytes(msg, 6, msg.length - 7));
        System.out.println(crcBytes[0]);
        System.out.println(checkBytes);
    }

}