package com.ding;

public class Demo {
	public static void say(String hello) {
		System.out.println(hello);
		}


	public static void main(String[] args) throws InterruptedException {
		Demo demo = new Demo();
		while (true) {
			demo.say("hello");
			Thread.sleep(10* 1000);
		}
	}
}
