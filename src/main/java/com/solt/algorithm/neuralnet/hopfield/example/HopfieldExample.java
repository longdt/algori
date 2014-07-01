package com.solt.algorithm.neuralnet.hopfield.example;

import java.util.Arrays;

import com.solt.algorithm.neuralnet.hopfield.HopfieldNetwork;

public class HopfieldExample {
	public static void main(String[] args) {
		HopfieldNetwork hn = new HopfieldNetwork(4);
		//train pattern
		boolean[] pattern1 = new boolean[] {true, true, false, false};
		//present pattern
		boolean[] pattern2 = new boolean[] {false, true, true, false};
		//trainning step
		System.out.println("train hopfield network with pattern: " + Arrays.toString(pattern1));
		for (int i =0; i < 1; ++i) {
			hn.train(pattern1);
			hn.train(pattern2);
		}
		//present pattern1
		boolean[] result = hn.present(pattern1);
		System.out.println("present pattern: " + Arrays.toString(pattern1) + " and get: " + Arrays.toString(result));
		
		//present pattern2
		result = hn.present(pattern2);
		System.out.println("present pattern: " + Arrays.toString(pattern2) + " and get: " + Arrays.toString(result));
	}
}
