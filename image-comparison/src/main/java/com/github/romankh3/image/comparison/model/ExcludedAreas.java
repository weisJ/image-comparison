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

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * The area that will be excluded, masked, in the image.
 */
public class ExcludedAreas {

    /**
     * The collection of the areas which would be excluded from the comparison.
     */
    private final List<Rectangle> excluded;

    /**
     * Create empty instance of the {@link ExcludedAreas}.
     */
    public ExcludedAreas() {
        excluded = new ArrayList<>();
    }

    /**
     * Create instance of the {@link ExcludedAreas} with provided {@link Rectangle} areas.
     *
     * @param excluded provided collection of the {@link Rectangle} objects.
     */
    public ExcludedAreas(List<Rectangle> excluded) {
        this.excluded = excluded;
    }

    /**
     * Check if this {@link Point} contains in the {@link ExcludedAreas#excluded}
     * collection of the {@link Rectangle}.
     *
     * @param point the {@link Point} object to be checked.
     *
     * @return {@code true} if this {@link Point} contains in areas from {@link ExcludedAreas#excluded}.
     */
    public boolean contains(Point point) {
        return excluded.stream().anyMatch(rectangle -> rectangle.containsPoint(point));
    }

    /**
     * Getter for excluded rectangles.
     *
     * @return the collection of the {@link Rectangle} objects.
     */
    public List<Rectangle> getExcluded() {
        return excluded;
    }
}
