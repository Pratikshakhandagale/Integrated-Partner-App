/*
 * Copyright 2013 David Schreiber
 *           2013 John Paul Nalog
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.ekstep.genie.customview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * This class has only internal use (package scope).
 * <p/>
 * It is responsible for applying additional effects to each coverflow item, that can only be applied at view level
 * (e.g. color saturation).
 * <p/>
 * This is a ViewGroup by intention to enable child views in layouts to stay interactive (like buttons) though
 * transformed.
 * <p/>
 * Since this class is only used within the FancyCoverFlowAdapter it doesn't need to check if there are multiple
 * children or not (there can only be one at all times).
 */
@SuppressWarnings("ConstantConditions")
class FancyCoverFlowItemWrapper extends ViewGroup {

    // =============================================================================
    // Private members
    // =============================================================================

    private float saturation;

    private boolean isReflectionEnabled = false;

    private float imageReflectionRatio;

    private int reflectionGap;

    private float originalScaledownFactor;

    /**
     * This is a matrix to apply color filters (like saturation) to the wrapped view.
     */
    private ColorMatrix colorMatrix;

    /**
     * This paint is used to draw the wrapped view including any filters.
     */
    private Paint paint;

    /**
     * This is a cache holding the wrapped view's visual representation.
     */
    private Bitmap wrappedViewBitmap;

    /**
     * This canvas is used to let the wrapped view draw it's content.
     */
    private Canvas wrappedViewDrawingCanvas;


    // =============================================================================
    // Constructor
    // =============================================================================

    public FancyCoverFlowItemWrapper(Context context) {
        super(context);
        this.init();
    }

    public FancyCoverFlowItemWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public FancyCoverFlowItemWrapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    private void init() {
        this.paint = new Paint();
        this.colorMatrix = new ColorMatrix();
        this.setSaturation(1);
    }

    // =============================================================================
    // Getters / Setters
    // =============================================================================

    public void setSaturation(float saturation) {
        if (saturation != this.saturation) {
            this.saturation = saturation;
            this.colorMatrix.setSaturation(saturation);
            this.paint.setColorFilter(new ColorMatrixColorFilter(this.colorMatrix));
        }
    }

    // =============================================================================
    // Supertype overrides
    // =============================================================================

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.remeasureChildren();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int measuredWidth = this.getMeasuredWidth();
            int measuredHeight = this.getMeasuredHeight();

            if (this.wrappedViewBitmap == null || this.wrappedViewBitmap.getWidth() != measuredWidth || this.wrappedViewBitmap.getHeight() != measuredHeight) {
                this.wrappedViewBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
                this.wrappedViewDrawingCanvas = new Canvas(this.wrappedViewBitmap);
            }

            View child = getChildAt(0);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int childLeft = (measuredWidth - childWidth) / 2;
            int childRight = measuredWidth - childLeft;
            child.layout(childLeft, 0, childRight, childHeight);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void dispatchDraw(Canvas canvas) {
        View childView = getChildAt(0);

        if (childView != null) {
            // If on honeycomb or newer, cache the view.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (childView.isDirty()) {
                    childView.draw(this.wrappedViewDrawingCanvas);
                }
            } else {
                childView.draw(this.wrappedViewDrawingCanvas);
            }
        }

        canvas.drawBitmap(this.wrappedViewBitmap, (this.getWidth() - childView.getWidth()) / 2, 0, paint);
    }

    // =============================================================================
    // Methods
    // =============================================================================

    private void remeasureChildren() {
        View child = this.getChildAt(0);

        if (child != null) {
            // When reflection is enabled calculate proportional scale down factor.
            final int originalChildHeight = this.getMeasuredHeight();
            this.originalScaledownFactor = this.isReflectionEnabled ? (originalChildHeight * (1 - this.imageReflectionRatio) - reflectionGap) / originalChildHeight : 1.0f;
            final int childHeight = (int) (this.originalScaledownFactor * originalChildHeight);
            final int childWidth = (int) (this.originalScaledownFactor * getMeasuredWidth());

            int heightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST);
            int widthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST);
            this.getChildAt(0).measure(widthSpec, heightSpec);
        }
    }

}
