package com.example.dataprotection.code;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cycle743 {
    public HashMap<String, Integer> syndrome = new HashMap<String, Integer>();
    int charLen = 8;
    int encCharLen = 7;
    public Cycle743(){

    }

    public String encoder(String in){
        String res = new String();
        String strBinary = byteToBinary(in.getBytes());
        for(int i = 0; i < strBinary.length()/(charLen/2); i++){
            String slice = strBinary.substring(i*charLen/2, (i+1)*charLen/2);
            int[] arr = intArr(slice + "000");
            arr = excessBits(arr);
            res += IntStream.of(arr).mapToObj(Integer::toString).collect(Collectors.joining(""));
        }
        return  res;
    }

    public String decoder(String in){
        errorSyndrome();
        String res = new String();
        for(int i = 0; i < in.length()/encCharLen; i++){
            String slice = in.substring(i*encCharLen, (i+1)*encCharLen);
            int[] arr = intArr(slice);

            if(syndrome.containsKey(errorCode(arr))){
                int errorIndex = syndrome.get(errorCode(arr));
                arr[errorIndex] = arr[errorIndex] == 0 ? 1 : 0;
            }

            arr = deleteExcessBits(arr);
            res += IntStream.of(arr).mapToObj(Integer::toString).collect(Collectors.joining(""));

        }
        return new String(byteFromBinary(res));
    }

    public String byteToBinary(byte[] raw) {
        StringBuilder sb = new StringBuilder();
        for (byte b : raw) {

            StringBuilder sbb = new StringBuilder("00000000");
            for (int bit = 0; bit < charLen; bit++) {
                if (((b >> bit) & 1) > 0) {
                    sbb.setCharAt(charLen - bit - 1, '1');
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

    private int[] intArr(String in){
        char[] c = in.toCharArray();
        int[] b = new int[c.length];
        for(int i = 0; i < b.length; i++){
            b[i] = Integer.parseInt(String.valueOf(c[i]));
        }
        return  b;
    }

    private int[] excessBits(int[] arr) {
        int[] newArr = Arrays.copyOf(arr, arr.length);
        int[] b = {1, 0, 1, 1};
        for (int i = 0; i < arr.length - b.length + 1; i++) {
            int c = arr[i] / b[0];
            for (int j = 0; j < b.length; j++) {
                arr[i + j] -= b[j] * c;
            }
        }
        for(int i =0; i < b.length-1; i++){
            newArr[newArr.length-1-i] = Math.abs(arr[arr.length-i-1]%2);
        }
        return newArr;
    }

    public void errorSyndrome(){
        syndrome.put("001", 6);
        syndrome.put("010", 5);
        syndrome.put("100", 4);
        syndrome.put("101", 3);
        syndrome.put("111", 2);
        syndrome.put("011", 1);
        syndrome.put("110", 0);

    }

    public String errorCode(int[] arr){
        int[] newArr = Arrays.copyOf(arr, arr.length);
        String erCode = new String();
        int[] b = {1, 0, 1, 1};
        for (int i = 0; i < newArr.length - b.length + 1; i++) {
            int c = newArr[i] / b[0];
            for (int j = 0; j < b.length; j++) {
                newArr[i + j] -= b[j] * c;
            }
        }
        for(int i =0; i < b.length-1; i++){
            erCode += String.valueOf(Math.abs(newArr[newArr.length-i-1]%2));
        }
        return erCode;
    }

    private int[] deleteExcessBits(int[] arr){
        arr = Arrays.copyOf(arr, charLen/2);
        return arr;
    }

}
