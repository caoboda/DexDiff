package com.example.view_day_03;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;

public class MainActivity extends AppCompatActivity {
    QQStepView qqStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qqStepView=(QQStepView)findViewById(R.id.step_view);
        qqStepView.setStepMax(10000);
        // 属性动画 后面讲的内容
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 8000);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentStep = (float) animation.getAnimatedValue();
                qqStepView.setCurrentStep((int)currentStep);
            }
        });
        valueAnimator.start();

    }
}