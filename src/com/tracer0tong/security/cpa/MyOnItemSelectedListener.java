package com.tracer0tong.security.cpa;

import android.view.View;
import android.widget.AdapterView;

/**
 * User: @tracer0tong (tracer.tong@yandex.ru)
 * Date: 04.12.12
 * Time: 0:50
 */
public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    MainActivity myma;

    MyOnItemSelectedListener(MainActivity ma)
    {
        myma = ma;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        myma.ChangeAuthority();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Stub
    }
}
