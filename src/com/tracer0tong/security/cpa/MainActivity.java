package com.tracer0tong.security.cpa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

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
    private ArrayList<MyContentProviderInfo> cps;
    private ProviderInfo[] providers;

    private void FillSpinner()
    {
        getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS).size();
        cps = new ArrayList<MyContentProviderInfo>();
        int i = 0;
        for (PackageInfo pack : getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)) {
            providers = pack.providers;
            if (providers != null) {
                for (ProviderInfo provider : providers) {
                    if (provider.authority != null)
                    {
                        MyContentProviderInfo mcpi = new MyContentProviderInfo("content://" + provider.authority.split(";")[0]);
                        mcpi.SetPermission(provider.readPermission,provider.writePermission);
                        cps.add(mcpi);
                        i++;
                    }
                }
            }
        }
        cs.setAdapter(new MyAdapter(MainActivity.this, R.layout.spinner, cps));
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
        MyContentProviderInfo mcpi = cps.get(cs.getSelectedItemPosition());
        mcpi.setAuthority(auth.getText().toString());
        mcpi.setProjection(proj.getText().toString());
        mcpi.setSelection(sel.getText().toString());
        mcpi.setSelectionArgs(sel_arg.getText().toString());
        my_intent.putExtra("cpi",mcpi);
        startActivity(my_intent);
    }

    public void ChangeAuthority()
    {
        auth.setText(cps.get(cs.getSelectedItemPosition()).getAuthority());
    }

    public class MyAdapter extends ArrayAdapter<String>{

        MyContentProviderInfo[] cpinfo;

        public MyAdapter(Context context, int textViewResourceId,   ArrayList<MyContentProviderInfo> cpi) {
            super(context, textViewResourceId, new String[cpi.size()]);
            cpinfo = new MyContentProviderInfo[cpi.size()];
            int i = 0;
            for (MyContentProviderInfo cp : cpi)
            {
                cpinfo[i] = cp;
                i++;
            }
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner, parent, false);
            TextView label = (TextView)row.findViewById(R.id.content_provider_authority);
            label.setText(cpinfo[position].getAuthority());
            TextView read_perm = (TextView)row.findViewById(R.id.read_permission);
            String read_permission_string = cpinfo[position].getPermissions()[MyContentProviderInfo.PERMISSIONS_READ];
            read_perm.setTextColor(getResources().getColor(R.drawable.green));
            read_perm.setText("Read permissions:" + read_permission_string);
            if (read_permission_string == null)
            {
                read_perm.setTextColor(getResources().getColor(R.drawable.red));
                read_perm.setText("Read permissions: -");
            }
            TextView write_perm = (TextView)row.findViewById(R.id.write_permission);
            String write_permission_string = cpinfo[position].getPermissions()[MyContentProviderInfo.PERMISSIONS_WRITE];
            write_perm.setTextColor(getResources().getColor(R.drawable.green));
            write_perm.setText("Write permissions:" + write_permission_string);
            if (write_permission_string == null)
            {
                write_perm.setTextColor(getResources().getColor(R.drawable.red));
                write_perm.setText("Write permissions: -");
            }
            return row;
        }
    }
}