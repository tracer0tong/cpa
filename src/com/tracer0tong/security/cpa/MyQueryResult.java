package com.tracer0tong.security.cpa;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Formatter;

/**
 * User: @tracer0tong (tracer.tong@yandex.ru)
 * Date: 04.12.12
 * Time: 1:47
 */
public class MyQueryResult extends Activity {

    private Button btn;
    private EditText result_text;
    private ImageView iv;
    private String authority;
    private String projection;
    private String selection;
    private String selection_args;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        btn = (Button)findViewById(R.id.res_close);
        result_text = (EditText)findViewById(R.id.res_text);
        iv = (ImageView)findViewById(R.id.image);

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

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return sb.toString();
    }

    private void showResult()
    {
        String Columns[];
        byte Image[];
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
            Columns = new String[col_c];
            for (int i=0; i<col_c; i++)
            {
                s += c.getColumnName(i);
                s += ":";
                Columns[i] = c.getColumnName(i);
            }
            s += "\n";
            if (c.moveToFirst()) {
                do {
                    for (int i=0; i<col_c; i++)
                    {
                        if (Columns[i].toLowerCase().contains("image"))
                        {
                            Image = c.getBlob(i);
                            //iv.setImageBitmap(BitmapFactory.decodeByteArray(Image,0,Image.length));
                            //s += "<IMAGE?>";
                            byte[] blob = c.getBlob(i);
                            s +=  bytesToHexString(blob);
                        }
                        else
                        {
                            try
                            {
                                s += c.getString(i);
                            }
                            catch (Exception e)
                            {
                                byte[] blob = c.getBlob(i);
                                s += bytesToHexString(blob);
                            }
                        }
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