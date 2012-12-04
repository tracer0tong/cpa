package com.tracer0tong.security.cpa;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * User: @tracer0tong (tracer.tong@yandex.ru)
 * Date: 04.12.12
 * Time: 1:47
 */
public class MyQueryResult extends Activity {

    private Button btn;
    private EditText result_text;
    private String authority;
    private String projection;
    private String selection;
    private String selection_args;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        btn = (Button)findViewById(R.id.res_close);
        result_text = (EditText)findViewById(R.id.res_text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        MyContentProviderInfo cpi = intent.getParcelableExtra("cpi");
        authority = cpi.getAuthority();
        if (authority.equals("")) authority = null;
        projection  = cpi.getProjection();
        if (projection.equals("")) projection = null;
        selection  = cpi.getSelection();
        if (selection.equals("")) selection = null;
        selection_args  = cpi.getSelectionArgs();
        if (selection_args.equals("")) selection_args = null;
        showResult();
    }

    private void showResult()
    {
        result_text.setText("Running...");
        String s = "Authority:";
        s += authority;
        s += "\nProjection:";
        s += projection;
        s += "\nSelection:";
        s += selection;
        s += "\nSelection args:";
        s += selection_args;
        s += "\n";
        try
        {
            Uri uri = Uri.parse(authority);
            String[] prj = {projection};
            if (projection == null) prj = null;
            String[] sel_args = {selection_args};
            if (selection_args == null) sel_args = null;
            Cursor c = getContentResolver().query(uri, prj, selection, sel_args, null);
            int col_c = c.getColumnCount();
            s += c.getCount();
            s +=":";
            s += col_c;
            s += "\n";
            for (int i=0; i<col_c; i++)
            {
                s += c.getColumnName(i);
                s += ":";
            }
            s += "\n";
            if (c.moveToFirst()) {
                do {
                    for (int i=0; i<col_c; i++)
                    {
                        s += c.getString(i);
                        s += ";";
                    }
                    s += "\n";
                } while (c.moveToNext());
            }
        }
        catch(Exception e)
        {
            s += e.getMessage();
        }
        result_text.setText(s);
    }
}