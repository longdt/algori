package com.solt.algorithm.search.genetic.example;

public class City {
	private int xpos;
	private int ypos;
	
	public City(int x, int y) {
		this.xpos = x;
		this.ypos = y;
	}

	public double proximity(City city) {
		return proximity(city.xpos, city.ypos);
	}
	
	public double proximity(int x, int y) {
		int xdif = xpos - x;
		int ydif = ypos - y;
		return Math.sqrt(xdif * xdif + ydif * ydif);
	}

}
