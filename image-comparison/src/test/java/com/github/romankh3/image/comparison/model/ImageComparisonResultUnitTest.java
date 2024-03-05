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

import static com.github.romankh3.image.comparison.ImageComparisonUtil.readImageFromResources;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit-level testing for {@link ImageComparisonResult} object")
public class ImageComparisonResultUnitTest {

    @DisplayName("Should properly work object creation")
    @Test
    public void shouldProperlyWorkObjectCreation() {
        // when
        ImageComparisonResult imageComparisonResult = new ImageComparisonResult();

        // then
        assertNotNull(imageComparisonResult);
    }

    @DisplayName("Should properly work getters and setters")
    @Test
    public void shouldProperlyWorkGettersAndSetters() {
        // when
        List<Rectangle> rectangles = new ArrayList<>();
        rectangles.add(Rectangle.createDefault());
        ImageComparisonResult imageComparisonResult = new ImageComparisonResult()
                .setImageComparisonState(ImageComparisonState.MATCH)
                .setExpected(readImageFromResources("expected.png"))
                .setActual(readImageFromResources("actual.png"))
                .setResult(readImageFromResources("result.png"))
                .setRectangles(rectangles);

        // then
        assertEquals(ImageComparisonState.MATCH, imageComparisonResult.getImageComparisonState());
        assertNotNull(imageComparisonResult.getExpected());
        assertNotNull(imageComparisonResult.getActual());
        assertNotNull(imageComparisonResult.getResult());
        assertEquals(imageComparisonResult.getRectangles(), rectangles);
    }

}
