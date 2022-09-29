package ss.colytitse.fuckdmzj.tool;

public class Base64Util {

    static private final int BASELENGTH = 128;
    static private final int FOURBYTE = 4;
    static private final char PAD = '=';
    static final private byte[] base64Alphabet = new byte[BASELENGTH];

    static {
        for (int i = 0; i < BASELENGTH; ++i)
            base64Alphabet[i] = -1;
        for (int i = 'Z'; i >= 'A'; i--)
            base64Alphabet[i] = (byte) (i - 'A');
        for (int i = 'z'; i >= 'a'; i--)
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        for (int i = '9'; i >= '0'; i--)
            base64Alphabet[i] = (byte) (i - '0' + 52);
        base64Alphabet['+'] = 62;
        base64Alphabet['/'] = 63;
    }

    private static boolean isWhiteSpace(char octect) {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }

    private static boolean isPad(char octect) {
        return (octect == PAD);
    }

    private static boolean isData(char octect) {
        return (octect >= BASELENGTH || base64Alphabet[octect] == -1);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        char[] base64Data = encoded.toCharArray();
        int len = removeWhiteSpace(base64Data);
        if (len % FOURBYTE != 0) return null;
        int numberQuadruple = (len / FOURBYTE);
        if (numberQuadruple == 0) return new byte[0];
        byte[] decodedData;
        byte b1, b2, b3, b4;
        char d1, d3, d2, d4;
        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[(numberQuadruple) * 3];
        for (; i < numberQuadruple - 1; i++) {
            if (isData((d1 = base64Data[dataIndex++])) || isData((d2 = base64Data[dataIndex++]))
                    || isData((d3 = base64Data[dataIndex++]))
                    || isData((d4 = base64Data[dataIndex++]))) return null;
            b1 = base64Alphabet[d1];
            b2 = base64Alphabet[d2];
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }
        if (isData((d1 = base64Data[dataIndex++])) || isData((d2 = base64Data[dataIndex++]))) {
            return null;
        }
        b1 = base64Alphabet[d1];
        b2 = base64Alphabet[d2];
        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex];
        if (isData((d3)) || isData((d4))) {//Check if they are PAD characters
            if (isPad(d3) && isPad(d4)) {
                if ((b2 & 0xf) != 0) return null;
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (!isPad(d3) && isPad(d4)) {
                b3 = base64Alphabet[d3];
                if ((b3 & 0x3) != 0) return null;
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else  return null;
        } else {
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex] = (byte) (b3 << 6 | b4);
        }
        return decodedData;
    }

    private static int removeWhiteSpace(char[] data) {
        if (data == null) return 0;
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++)
            if (!isWhiteSpace(data[i]))
                data[newSize++] = data[i];
        return newSize;
    }
}
