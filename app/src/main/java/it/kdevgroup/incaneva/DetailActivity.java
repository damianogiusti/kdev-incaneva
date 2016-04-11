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

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    ImageView imgEvent;
    TextView txtTitle, txtTipo, txtContent, txtStartEnd, txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgEvent = (ImageView) findViewById(R.id.imgEvent);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTipo = (TextView) findViewById(R.id.txtTipo);
        txtContent = (TextView) findViewById(R.id.txtContent);
        txtStartEnd = (TextView) findViewById(R.id.txtStartEnd);
        txtData = (TextView) findViewById(R.id.txtData);

        Bundle vBundle = getIntent().getBundleExtra(HomeActivity.BUNDLE_KEY_FOR_ARRAY);

        if (vBundle != null) {
            // imgEvent = vBundle.getString()
            txtTitle.setText(vBundle.getString(BlogEvent.KEY_post_title));
            txtTipo.setText(vBundle.getString(BlogEvent.KEY_event_type));
            txtContent.setText(vBundle.getString(BlogEvent.KEY_post_content));

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy");
            long dataInizio = vBundle.getLong(BlogEvent.KEY_evcal_srow) * 1000;
            long dataFine = vBundle.getLong(BlogEvent.KEY_evcal_erow) * 1000;

            String dataInizioFormat = sdf.format(new Date(dataInizio));
            String dataFineFormat = sdf.format(new Date(dataFine));
            if (dataInizio == dataFine){
                txtStartEnd.setText(dataInizioFormat);
            }else {
                txtStartEnd.setText(dataInizioFormat + " - " + dataFineFormat);
            }
            //txtStartEnd.setText();//da finire
           // txtData.setText(vBundle.getString(BlogEvent.));
        }

    }

}
