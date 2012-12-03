package com.tracer0tong.security.cpa;

import android.view.View;
import android.widget.Button;

/**
 * User: @tracer0tong (tracer.tong@yandex.ru)
 * Date: 04.12.12
 * Time: 1:00
 */
public class MyOnClickListener implements Button.OnClickListener {
    private MainActivity myma;

    MyOnClickListener(MainActivity ma)
    {
        myma = ma;
    }

    public void onClick(View view) {
        myma.ShowResult();
    }
}
