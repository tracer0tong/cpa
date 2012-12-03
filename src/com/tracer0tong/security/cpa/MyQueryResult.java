package com.tracer0tong.security.cpa;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * User: @tracer0tong (tracer.tong@yandex.ru)
 * Date: 04.12.12
 * Time: 1:47
 */
public class MyQueryResult extends Activity {

    private Button btn;
    private TextView result_text;
    private String authority;
    private String projection;
    private String selection;
    private String selection_args;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        btn = (Button)findViewById(R.id.res_close);
        result_text = (TextView)findViewById(R.id.res_text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        authority = intent.getStringExtra("authority");
        if (authority.equals("")) authority = null;
        projection  = intent.getStringExtra("projection");
        if (projection.equals("")) projection = null;
        selection  = intent.getStringExtra("selection");
        if (selection.equals("")) selection = null;
        selection_args  = intent.getStringExtra("selection_args");
        if (selection_args.equals("")) selection_args = null;
        showResult();
    }

    private void showResult()
    {
        result_text.setText("Running...");
        String s = "";
        s += authority;
        try
        {
            Uri uri = Uri.parse(authority);
            String[] prj = {projection};
            String[] sel_args = {selection_args};
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