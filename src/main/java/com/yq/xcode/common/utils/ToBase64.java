package com.yq.xcode.common.utils;

import java.util.Arrays;


public class ToBase64 {
	
   private static final char[] S_BASE64CHAR = {
       'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
       'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
       'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
       'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
       'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
       'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
       '8', '9', '+', '/'
   };
   private static final char S_BASE64PAD = '=';
   private static final byte[] S_DECODETABLE = new byte[128];
   static {
       for (int i = 0;  i < S_DECODETABLE.length;  i ++)
           S_DECODETABLE[i] = Byte.MAX_VALUE;  // 127
       for (int i = 0;  i < S_BASE64CHAR.length;  i ++) // 0 to 63
           S_DECODETABLE[S_BASE64CHAR[i]] = (byte)i;
   }

   private static int decode0(char[] ibuf, byte[] obuf, int wp) throws Exception {
       int outlen = 3;
       if (ibuf[3] == S_BASE64PAD)  outlen = 2;
       if (ibuf[2] == S_BASE64PAD)  outlen = 1;
       int b0 = S_DECODETABLE[ibuf[0]];
       int b1 = S_DECODETABLE[ibuf[1]];
       int b2 = S_DECODETABLE[ibuf[2]];
       int b3 = S_DECODETABLE[ibuf[3]];
       switch (outlen) {
       case 1:
           obuf[wp] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 0x3);
           return 1;
       case 2:
           obuf[wp++] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 0x3);
           obuf[wp] = (byte)(b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
           return 2;
       case 3:
           obuf[wp++] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 0x3);
           obuf[wp++] = (byte)(b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
           obuf[wp] = (byte)(b2 << 6 & 0xc0 | b3 & 0x3f);
           return 3;
       default:
           throw new Exception("internalError00");
       }
   }

   /**
    *
    */
   public static byte[] decode(char[] data, int off, int len) throws Exception {
       char[] ibuf = new char[4];
       int ibufcount = 0;
       byte[] obuf = new byte[len/4*3+3];
       int obufcount = 0;
       for (int i = off;  i < off+len;  i ++) {
           char ch = data[i];
           if (ch == S_BASE64PAD
               || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != Byte.MAX_VALUE) {
               ibuf[ibufcount++] = ch;
               if (ibufcount == ibuf.length) {
                   ibufcount = 0;
                   obufcount += decode0(ibuf, obuf, obufcount);
               }
           }
       }
       if (obufcount == obuf.length)
           return obuf;
       byte[] ret = new byte[obufcount];
       System.arraycopy(obuf, 0, ret, 0, obufcount);
       return ret;
   }

   /**
    *
    */
   public static byte[] decode(String data) throws Exception {
       char[] ibuf = new char[4];
       int ibufcount = 0;
       byte[] obuf = new byte[data.length()/4*3+3];
       int obufcount = 0;
       for (int i = 0;  i < data.length();  i ++) {
           char ch = data.charAt(i);
           if (ch == S_BASE64PAD
               || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != Byte.MAX_VALUE) {
               ibuf[ibufcount++] = ch;
               if (ibufcount == ibuf.length) {
                   ibufcount = 0;
                   obufcount += decode0(ibuf, obuf, obufcount);
               }
           }
       }
       if (obufcount == obuf.length)
           return obuf;
       byte[] ret = new byte[obufcount];
       System.arraycopy(obuf, 0, ret, 0, obufcount);
       return ret;
   }


   /**
    * Returns base64 representation of specified byte array.
    */
   public static String encode(byte[] data) {
       return encode(data, 0, data.length);
   }

   /**
    * Returns base64 representation of specified byte array.
    */
   public static String encode(byte[] data, int off, int len) {
       if (len <= 0)  return "";
       char[] out = new char[len/3*4+4];
       int rindex = off;
       int windex = 0;
       int rest = len-off;
       while (rest >= 3) {
           int i = ((data[rindex]&0xff)<<16)
                   +((data[rindex+1]&0xff)<<8)
                   +(data[rindex+2]&0xff);
           out[windex++] = S_BASE64CHAR[i>>18];
           out[windex++] = S_BASE64CHAR[(i>>12)&0x3f];
           out[windex++] = S_BASE64CHAR[(i>>6)&0x3f];
           out[windex++] = S_BASE64CHAR[i&0x3f];
           rindex += 3;
           rest -= 3;
       }
       if (rest == 1) {
           int i = data[rindex]&0xff;
           out[windex++] = S_BASE64CHAR[i>>2];
           out[windex++] = S_BASE64CHAR[(i<<4)&0x3f];
           out[windex++] = S_BASE64PAD;
           out[windex++] = S_BASE64PAD;
       } else if (rest == 2) {
           int i = ((data[rindex]&0xff)<<8)+(data[rindex+1]&0xff);
           out[windex++] = S_BASE64CHAR[i>>10];
           out[windex++] = S_BASE64CHAR[(i>>4)&0x3f];
           out[windex++] = S_BASE64CHAR[(i<<2)&0x3f];
           out[windex++] = S_BASE64PAD;
       }
       return new String(out, 0, windex);
   }
   private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
            .toCharArray();
   private static final int[] IA = new int[256];
    static {
        Arrays.fill(IA, -1);
        for (int i = 0, iS = CA.length; i < iS; i++)
            IA[CA[i]] = i;
        IA['='] = 0;
    }

   public final static char[] encodeToChar(byte[] sArr, boolean lineSep) {
        // Check special case
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0)
            return new char[0];

        int eLen = (sLen / 3) * 3; // Length of even 24-bits.
        int cCnt = ((sLen - 1) / 3 + 1) << 2; // Returned character count
        int dLen = cCnt + (lineSep ? (cCnt - 1) / 76 << 1 : 0); // Length of
                                                                // returned
                                                                // array
        char[] dArr = new char[dLen];

        // Encode even 24-bits
        for (int s = 0, d = 0, cc = 0; s < eLen;) {
            // Copy next three bytes into lower 24 bits of int, paying attension
            // to sign.
            int i = (sArr[s++] & 0xff) << 16 | (sArr[s++] & 0xff) << 8
                    | (sArr[s++] & 0xff);

            // Encode the int into four chars
            dArr[d++] = CA[(i >>> 18) & 0x3f];
            dArr[d++] = CA[(i >>> 12) & 0x3f];
            dArr[d++] = CA[(i >>> 6) & 0x3f];
            dArr[d++] = CA[i & 0x3f];

            // Add optional line separator
            if (lineSep && ++cc == 19 && d < dLen - 2) {
                dArr[d++] = '\r';
                dArr[d++] = '\n';
                cc = 0;
            }
	   }
		return dArr;
   }
}
