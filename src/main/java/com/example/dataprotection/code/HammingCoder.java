package com.example.dataprotection.code;

import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class HammingCoder {
    private int charLen = 7;
    private int encCharLen = 11;
    private int excBits;

    public HammingCoder(){}

    public String encoder(String in){
        String res = new String();
        String strBinary = byteToBinary(in.getBytes());
        for(int i = 0; i < strBinary.length()/charLen; i++){
            String slice = strBinary.substring(i*charLen, (i+1)*charLen);
            excessBits(slice);
            int[] arr = generateCode(slice);
            calculation(arr);
            res += IntStream.of(arr).mapToObj(Integer::toString).collect(Collectors.joining(""));
        }
        return  res;
    }

    public String decoder(String in){
        String res = new String();
        for(int i = 0; i < in.length()/encCharLen; i++){
            excessBits(encCharLen);
            int[] ar = intArr(in.substring(i*encCharLen, (i+1)*encCharLen));
            int index = decoderCalculation(ar);
            if(index != -1)
                ar[index] = ar[index] == 0 ? 1 : 0;
            res += new String(byteFromBinary(deleteExcessBits(ar)));
        }
        return res;
    }

    private void excessBits(String in){
        excBits = 1;
        int M = in.length();
        while(Math.pow(2, excBits) < (M + excBits + 1)){
            excBits++;
        }
    }

    private void excessBits(int sizeBlock){
        excBits = 1;
        while(Math.pow(2, excBits) < (sizeBlock)){
            excBits++;
        }
    }

    private int[] calculation(int[] ar) {
        for(int i = 0; i < excBits; i++){
            int x = (int) Math.pow(2, i);
            int count = x-1;
            for(int j = x-1; j < ar.length; j++){
                ar[x-1] += ar[j];
                if(count > 0){
                    count--;
                }
                else{
                    count = x-1;
                    j+=count+1;
                }
            }
            ar[x-1] %=2;
        }

        return ar;
    }

    private int decoderCalculation(int[] ar) {
        int indexError = 0;

        for(int i = 0; i < excBits; i++){
            int helpNum = 0;
            int x = (int) Math.pow(2, i);
            int count = x-1;
            for(int j = x-1; j < ar.length; j++){
                if(j != x-1)
                    helpNum += ar[j];
                if(count > 0)
                    count--;
                else{
                    count = x-1;
                    j+=count+1;
                }
            }
            if((helpNum%2) != ar[x-1]){
                indexError += x;
            }
        }
        indexError--;

        return indexError;
    }

    private int[] generateCode(String str) {
        int M = str.length();
        int[] arr = new int[M+excBits];
        int j = 0;
        for(int i = 0; i < arr.length; i++){
            if(i == Math.pow(2, j)-1){
                arr[i] = 0;
                j++;
            }
            else{
                arr[i] = str.charAt(i-j) - '0';
            }
        }
        return arr;
    }

    private int[] intArr(String in){
        char[] c = in.toCharArray();
        int[] num = new int[c.length];
        for(int i = 0; i < num.length; i++){
            num[i] = Integer.parseInt(String.valueOf(c[i]));
        }
        return  num;
    }

    private String deleteExcessBits(int[] ar){
        int[] newAr = new int[ar.length - excBits];
        int count = 0;
        for(int j = 0; j < ar.length; j++){
            if(j != Math.pow(2, count)-1){
                newAr[j-count] = ar[j];
            }
            else{
                count++;
            }
        }
        return IntStream.of(newAr).mapToObj(Integer::toString).collect(Collectors.joining(""));
    }

    private String byteToBinary(byte[] raw) {
        StringBuilder sb = new StringBuilder();
        for (byte b : raw) {

            StringBuilder sbb = new StringBuilder("0000000");
            for (int bit = 0; bit < charLen; bit++) {
                if (((b >> bit) & 1) > 0) {
                    sbb.setCharAt(charLen- 1 - bit, '1');
                }
            }
            sb.append(sbb);
        }
        return sb.toString();
    }

    private byte[] byteFromBinary(String in) {
        byte[] rez = new byte[in.length() / charLen];
        int i = 0;
        while (in.length() >= charLen) {
            String slice = in.substring(0, charLen);
            in = in.substring(charLen);
            rez[i] = Byte.parseByte(slice, 2);
            i++;
        }
        return rez;
    }
}
