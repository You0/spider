package com.me.utils;

public class Log {
	private static boolean E = false;
	private static boolean D = false;
	public static void E(Object info){
		if(E==true){
			System.out.println(String.valueOf(info));
		}
	}
	
	public static void D(Object info){
		if(D==true){
			System.out.println(String.valueOf(info));
		}
	}
}
