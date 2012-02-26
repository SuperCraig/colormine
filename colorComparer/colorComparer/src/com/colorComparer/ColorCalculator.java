package com.colorComparer;

import com.colorComparer.colorSpace.ColorSpaceConverter;
import com.colorComparer.colorSpace.Hsl;
import com.colorComparer.colorSpace.Lab;
import com.colorComparer.colorSpace.Rgb;

public class ColorCalculator {

	final static String COLOR_DELIMITER = "-";
	final static String LAB_TYPE = "lab";
	final static String RGB_TYPE = "rgb";
	final static String TYPE = "type";

	public static double isComplement(String type, String value1, String value2) {
		double[] a = getRgbNumbers(value1);
		double[] b = getRgbNumbers(value2);

		Rgb rgb1 = new Rgb(a);
		Rgb rgb2 = new Rgb(b);

		Rgb complementRgb = getComplement(rgb1);

		return (complementRgb.isNearMatch(rgb2, 1.0)) ? 1 : 0;
	}
	public static Rgb getComplement(Rgb rgb) {
		
		Hsl hsl = ColorSpaceConverter.rgbToHsl(rgb);
		hsl = getHslComplement(hsl);
		
		return ColorSpaceConverter.hslToRgb(hsl);
	}
	public static Rgb[] GetTriadic(Rgb rgb){
		Rgb[] triadicColors = new Rgb[2];
		
		Hsl hsl = ColorSpaceConverter.rgbToHsl(rgb);
		hsl = getHslComplement(hsl);
		
		Hsl triad1 = moveHueOncolorWheel(hsl,120.0);
		Hsl triad2 = moveHueOncolorWheel(hsl,-120.0);
		
		triadicColors[0] = ColorSpaceConverter.hslToRgb(triad1);
		triadicColors[1] = ColorSpaceConverter.hslToRgb(triad2);
		
		return triadicColors;
	}
	private static Hsl getHslComplement(Hsl hsl) {
		
		Hsl ComplementH = moveHueOncolorWheel(hsl,180.0);

		return ComplementH;
	}
	private static Hsl moveHueOncolorWheel(Hsl hsl,double percent) {
		double H = hsl.H + percent/360;

		if (H > 1) {
			H-= 1;
		}

		return new Hsl(H,hsl.S,hsl.L);
	}
	
	public static double compare(String type, String value1, String value2) {
		double[] a = getRgbNumbers(value1);
		double[] b = getRgbNumbers(value2);

		if (LAB_TYPE.equals(type)) {
			return ColorComparer.compare(new Lab(a), new Lab(b));
		}

		if (RGB_TYPE.equals(type)) {
			return ColorComparer.compare(new Rgb(a), new Rgb(b));
		}

		throw new IllegalArgumentException("Must set a " + TYPE + " of " + LAB_TYPE + " or " + RGB_TYPE);
	}
	public static double[] getRgbNumbers(String value) {
		final int max = 3;

		String[] stringNumbers = value.split(COLOR_DELIMITER);
		double[] doubleNumbers = new double[max];

		for (int i = 0; i < max; i++) {
			doubleNumbers[i] = Double.parseDouble(stringNumbers[i]);
		}
		return doubleNumbers;
	}

}