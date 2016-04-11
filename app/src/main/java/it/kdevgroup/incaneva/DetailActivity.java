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

import com.squareup.picasso.Picasso;

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

        BlogEvent event = getIntent().getParcelableExtra(HomeActivity.BUNDLE_KEY_FOR_ARRAY);

        if (event != null) {
            Picasso.with(getApplicationContext()).load(event.getImageLink()).fit().into(imgEvent);
           // imgEvent.
            txtTitle.setText(event.getPostTitle());
//            txtTipo.setText(event.);
            txtContent.setText(event.getPostContent());

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy");
            long dataInizio = event.getStartTime()* 1000;
            long dataFine = event.getEndTime() * 1000;

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
