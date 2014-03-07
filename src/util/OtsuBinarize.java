package util;

import java.awt.Color;
import java.awt.image.BufferedImage;

//Image binarization - Otsu algorithm
public class OtsuBinarize {

    public static BufferedImage binarize(BufferedImage original) {
        int red;
        int newPixel;
        int threshold = otsuTreshold(original);
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if (red > threshold) {
                    newPixel = 255;
                } else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel);
            }
        }
        return binarized;
    }

    // Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }

    // Get binary treshold using Otsu's method
    private static int otsuTreshold(BufferedImage original) {
        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();
        double sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }
        double sumB = 0;
        int wB = 0;
        int wF = 0;
        double varMax = 0;
        int threshold = 0;
        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) {
                continue;
            }
            wF = total - wB;
            if (wF == 0) {
                break;
            }
            sumB += (float) (i * histogram[i]);
            double mB = sumB / wB;
            double mF = (sum - sumB) / wF;
            double varBetween = (double) wB * (double) wF * (mB - mF) * (mB - mF);
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
        return threshold;
    }

    // Return histogram of grayscale image
    private static int[] imageHistogram(BufferedImage input) {
        int[] histogram = new int[256];
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }
        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                int red = new Color(input.getRGB(i, j)).getRed();
                histogram[red]++;
            }
        }
        return histogram;
    }
}
