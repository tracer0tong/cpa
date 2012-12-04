package com.tracer0tong.security.cpa;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: @tracer0tong (tracer.tong@yandex.ru)
 * Date: 03.12.12
 * Time: 23:55
 */
public class MainActivity extends Activity {

    private Spinner cs;
    private Button btn;
    private EditText auth;
    private EditText proj;
    private EditText sel;
    private EditText sel_arg;
    private ArrayAdapter<String> aa;
    private List<String>cps;
    private ProviderInfo[] providers;

    private void FillSpinner()
    {
        cps = new ArrayList<String>();
        for (PackageInfo pack : getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)) {
            providers = pack.providers;
            if (providers != null) {
                for (ProviderInfo provider : providers) {
                    cps.add(provider.authority);
                }
            }
        }
        aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cps);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cs.setAdapter(aa);
        cs.setSelection(0);
        cs.setPrompt("Choose content provider");
    }

    private void AddListeners()
    {
        cs.setOnItemSelectedListener(new MyOnItemSelectedListener(this));
        btn.setOnClickListener(new MyOnClickListener(this));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        cs = (Spinner)findViewById(R.id.cp_list);
        btn = (Button)findViewById(R.id.run);
        auth = (EditText)findViewById(R.id.authority);
        proj = (EditText)findViewById(R.id.projection);
        sel = (EditText)findViewById(R.id.selection);
        sel_arg = (EditText)findViewById(R.id.selection_arg);

        FillSpinner();
        AddListeners();
    }

    public void ShowResult()
    {
        Intent my_intent = new Intent(this, MyQueryResult.class);
        my_intent.putExtra("authority",auth.getText().toString());
        my_intent.putExtra("projection",proj.getText().toString());
        my_intent.putExtra("selection",sel.getText().toString());
        my_intent.putExtra("selection_args",sel_arg.getText().toString());
        startActivity(my_intent);
    }

    public void ChangeAuthority()
    {
        auth.setText("content://" + cs.getSelectedItem());
    }
}