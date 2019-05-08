package com.atom.utill;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CommonUtil {

	public static String AtomSignatureGenerate(String hashKey,String ...param) {
		String resp = null;
		StringBuilder sb = new StringBuilder();
		for (String s : param) {
			sb.append(s);
		}
		try{
			System.out.println("[getEncodedValueWithSha2]String to Encode ="
					+ sb.toString());
			resp = byteToHexString(encodeWithHMACSHA2(sb.toString(),
					hashKey));
//resp = URLEncoder.encode(resp,"UTF-8");
		}catch(Exception e)
		{
			System.out.println("[getEncodedValueWithSha2]Unable to encocd value with key :" + hashKey + " and input :" + sb.toString());
			e.printStackTrace();
		}
		return resp;
	}

	public static byte[] encodeWithHMACSHA2(String text,String keyString) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		java.security.Key sk = new
				javax.crypto.spec.SecretKeySpec(keyString.getBytes("UTF8"),"HMACSHA512");
		javax.crypto.Mac mac =
				javax.crypto.Mac.getInstance(sk.getAlgorithm());
		mac.init(sk);
		byte[] hmac = mac.doFinal(text.getBytes("UTF-8"));
		return hmac;
	}

	public static String byteToHexString(byte byData[])
	{
		StringBuilder sb = new StringBuilder(byData.length * 2);
		for(int i = 0; i < byData.length; i++)
		{
			int v = byData[i] & 0xff;
			if(v < 16)
				sb.append('0');
			sb.append(Integer.toHexString(v));
		}
		return sb.toString();
	}

	public static synchronized String getAtomTransactionString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}
}
