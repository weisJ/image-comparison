/*
 * MIT License
 *
 * Copyright (c) 2024 Jannis Weis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.github.romankh3.image.comparison.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.List;

import com.github.romankh3.image.comparison.ImageComparisonUtil;

/**
 * Data transfer objects which contains all the needed data for result of the comparison.
 */
public class ImageComparisonResult {

    /**
     * {@link BufferedImage} object of the expected.
     */
    private BufferedImage expected;

    /**
     * {@link BufferedImage} object of the actual.
     */
    private BufferedImage actual;

    /**
     * {@link BufferedImage} object of the comparison result.
     */
    private BufferedImage result;

    /**
     * State of the comparison.
     */
    private ImageComparisonState imageComparisonState;

    /**
     * The difference percentage between two images.
     */
    private float differencePercent;

    /**
     * Rectangles of the differences
     */
    private List<Rectangle> rectangles;

    /**
     * Create default instance of the {@link ImageComparisonResult} with {@link ImageComparisonState#SIZE_MISMATCH}.
     *
     * @param expected          expected {@link BufferedImage} object.
     * @param actual            actual {@link BufferedImage} object.
     * @param differencePercent the percent of the differences between images.
     * @return instance of the {@link ImageComparisonResult} object.
     */
    public static ImageComparisonResult defaultSizeMisMatchResult(BufferedImage expected, BufferedImage actual,
            float differencePercent) {
        return new ImageComparisonResult()
                .setImageComparisonState(ImageComparisonState.SIZE_MISMATCH)
                .setDifferencePercent(differencePercent)
                .setExpected(expected)
                .setActual(actual)
                .setResult(actual)
                .setRectangles(Collections.emptyList());
    }

    /**
     * Create default instance of the {@link ImageComparisonResult} with {@link ImageComparisonState#MISMATCH}.
     *
     * @param expected expected {@link BufferedImage} object.
     * @param actual   actual {@link BufferedImage} object.
     * @param differencePercent the persent of the differences between images.
     * @return instance of the {@link ImageComparisonResult} object.
     */
    public static ImageComparisonResult defaultMisMatchResult(BufferedImage expected, BufferedImage actual,
            float differencePercent) {
        return new ImageComparisonResult()
                .setImageComparisonState(ImageComparisonState.MISMATCH)
                .setDifferencePercent(differencePercent)
                .setExpected(expected)
                .setActual(actual)
                .setResult(actual);
    }

    /**
     * Create default instance of the {@link ImageComparisonResult} with {@link ImageComparisonState#MATCH}.
     *
     * @param expected expected {@link BufferedImage} object.
     * @param actual   actual {@link BufferedImage} object.
     * @return instance of the {@link ImageComparisonResult} object.
     */
    public static ImageComparisonResult defaultMatchResult(BufferedImage expected, BufferedImage actual) {
        return new ImageComparisonResult()
                .setImageComparisonState(ImageComparisonState.MATCH)
                .setExpected(expected)
                .setActual(actual)
                .setResult(actual)
                .setRectangles(Collections.emptyList());
    }

    /**
     * Save the image to the provided {@link File} object.
     *
     * @param file the provided {@link File} object.
     * @return this {@link ImageComparisonResult} object.
     */
    public ImageComparisonResult writeResultTo(File file) {
        ImageComparisonUtil.saveImage(file, result);
        return this;
    }

    public BufferedImage getExpected() {
        return expected;
    }

    public ImageComparisonResult setExpected(BufferedImage expected) {
        this.expected = expected;
        return this;
    }

    public BufferedImage getActual() {
        return actual;
    }

    public ImageComparisonResult setActual(BufferedImage actual) {
        this.actual = actual;
        return this;
    }

    public BufferedImage getResult() {
        return result;
    }

    public ImageComparisonResult setResult(BufferedImage result) {
        this.result = result;
        return this;
    }

    public ImageComparisonState getImageComparisonState() {
        return imageComparisonState;
    }

    public ImageComparisonResult setImageComparisonState(ImageComparisonState imageComparisonState) {
        this.imageComparisonState = imageComparisonState;
        return this;
    }

    public float getDifferencePercent() {
        return differencePercent;
    }

    ImageComparisonResult setDifferencePercent(float differencePercent) {
        this.differencePercent = differencePercent;
        return this;
    }

    public List<Rectangle> getRectangles() {
        return rectangles;
    }

    public ImageComparisonResult setRectangles(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
        return this;
    }
}
