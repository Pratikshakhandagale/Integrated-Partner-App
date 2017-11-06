package org.ekstep.genie.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;

import org.ekstep.genie.R;

/**
 * Created by swayangjit_gwl on 4/23/2016.
 */
public class AnimationUtils {

    public static final int ANIMATION_DURATION_SHORTEST = 150;
    public static final int ANIMATION_DURATION_SHORT = 250;
    public static final int ANIMATION_DURATION_MEDIUM = 400;
    public static final int ANIMATION_DURATION_LONG = 800;

    @TargetApi(21)
    public static void circleRevealView(View view, int duration) {
        // get the center for the clipping circle
        int cx = view.getWidth();
        int cy = view.getHeight() / 2;

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        if (duration > 0) {
            anim.setDuration(duration);
        } else {
            anim.setDuration(ANIMATION_DURATION_MEDIUM);
        }

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(21)
    public static void circleRevealView(View view) {
        circleRevealView(view, ANIMATION_DURATION_SHORT);
    }

    @TargetApi(21)
    public static void circleHideView(final View view, AnimatorListenerAdapter listenerAdapter) {
        circleHideView(view, ANIMATION_DURATION_SHORT, listenerAdapter);
    }

    @TargetApi(21)
    public static void circleHideView(final View view, int duration, AnimatorListenerAdapter listenerAdapter) {
        // get the center for the clipping circle
        int cx = view.getWidth();
        int cy = view.getHeight() / 2;

        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(listenerAdapter);

        if (duration > 0) {
            anim.setDuration(duration);
        } else {
            anim.setDuration(ANIMATION_DURATION_SHORT);
        }

//        anim.setStartDelay(200);

        // start the animation
        anim.start();
    }

    public static void fadeInView(View view) {
        fadeInView(view, ANIMATION_DURATION_SHORTEST);
    }

    /**
     * Reveal the provided View with a fade-in animation.
     *
     * @param view     The View that's being animated.
     * @param duration How long should the animation take, in millis.
     */
    public static void fadeInView(View view, int duration) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);

        // Setting the listener to null, so it won't keep getting called.
        ViewCompat.animate(view).alpha(1f).setDuration(duration).setListener(null);
    }

    /**
     * Hide the provided View with a fade-out animation. Fast.
     *
     * @param view The View that's being animated.
     */
    public static void fadeOutView(View view) {
        fadeOutView(view, ANIMATION_DURATION_SHORTEST);
    }

    /**
     * Hide the provided View with a fade-out animation.
     *
     * @param view     The View that's being animated.
     * @param duration How long should the animation take, in millis.
     */
    public static void fadeOutView(final View view, int duration) {
        ViewCompat.animate(view).alpha(0f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                view.setDrawingCacheEnabled(true);
            }

            @Override
            public void onAnimationEnd(View view) {
                view.setVisibility(View.GONE);
                view.setAlpha(1f);
                view.setDrawingCacheEnabled(false);
            }

            @Override
            public void onAnimationCancel(View view) {
            }
        });
    }

    /**
     * Cross animate two views, showing one, hiding the other.
     *
     * @param showView The View that's going to be visible after the animation.
     * @param hideView The View that's going to disappear after the animation.
     */


    public static void crossFadeViews(View showView, final View hideView, int duration) {
        fadeInView(showView, duration);
        fadeOutView(hideView, duration);
    }

    public static void hideSearchBar(final View view, final View anotherView) {
        AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                if (anotherView != null) {
                    showSearchBar(anotherView);
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationUtils.circleHideView(view, listenerAdapter);
        } else {
            AnimationUtils.fadeOutView(view);
        }
    }

    public static void showSearchBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationUtils.circleRevealView(view);
        } else {
            AnimationUtils.fadeInView(view);
        }
    }

    public static void stopShakeAnimation(View view) {
        view.clearAnimation();
    }

    public static void showSlowShakeAnimation(View view) {
        Animation shake = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), R.anim.slow_shake);
        view.startAnimation(shake);
    }

    public static void showShakeAnimation(View view) {
        Animation shake = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), R.anim.shake);
        view.startAnimation(shake);
    }

    public static Animation showPulseAnimation(View view) {
        Animation pulseAnimation = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), R.anim.pulse);
        view.startAnimation(pulseAnimation);
        return pulseAnimation;
    }
}
