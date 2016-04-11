package it.kdevgroup.incaneva;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    ImageView imgEvent;
    TextView txtTitle, txtTipo, txtContent,txtStartEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgEvent = (ImageView)findViewById(R.id.imgEvent);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtTipo = (TextView)findViewById(R.id.txtTipo);
        txtContent = (TextView)findViewById(R.id.txtContent);
        txtStartEnd = (TextView)findViewById(R.id.txtStartEnd);

    }

}
