package cat.montilivi.editorfitxers;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    final String NOM_FITXER = "productes.txt";
    Button btnRead;
    Button btnAdd;
    Button btnSave;
    EditText etNom;
    EditText etCategoria;
    EditText etPreu;
    EditText etUnits;
    RadioButton rbInterna;
    RadioButton rbExterna;
    RadioButton rbRecursos;
    NumberPicker npLinea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRead = (Button) findViewById(R.id.btnLlegeix);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnAdd = (Button) findViewById(R.id.btnAfageix);
        etNom  = (EditText) findViewById(R.id.etNom);
        etCategoria = (EditText) findViewById(R.id.etCategoria);
        etPreu  = (EditText) findViewById(R.id.etPreu);
        etUnits  = (EditText) findViewById(R.id.etUnitats);
        rbExterna = (RadioButton) findViewById(R.id.rbExterna);
        rbInterna = (RadioButton) findViewById(R.id.rbInterna);
        rbRecursos = (RadioButton) findViewById(R.id.rbRecursos);
        npLinea = (NumberPicker) findViewById(R.id.npNLineaText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnLlegeix:
            {
                LlegeixFitxer();
                break;
            }
            case R.id.btnSave:
            {
                SaveFitxer();
                break;
            }
            case R.id.btnAfageix:
            {
                AfageixFitxer();
                break;
            }
        }

    }

    private void AfageixFitxer()
    {

    }

    private void SaveFitxer()
    {

    }

    private void LlegeixFitxer()
    {
        try {


            if(rbRecursos.isChecked())
            {
                InputStream isr = getResources().openRawResource(R.raw.productes);
                InputStreamReader is = new InputStreamReader(isr);
                BufferedReader br = new BufferedReader(is);
                ArrayList<String[]> liniaAL = new ArrayList<String[]>();
                String linia = "";
                while ((linia = br.readLine())!= null)
                {
                    liniaAL.add(linia.split(";"));
                }
                br.close();
            }
            else if(rbExterna.isChecked())
            {
                String storageState = Environment.getExternalStorageState();
                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                    File file = Environment.getExternalStoragePublicDirectory(NOM_FITXER);
                    FileInputStream fos2 = new FileInputStream(file);

                    fos2.close();
                }

            }
            else
            {}
  }
        catch (Exception e)
        {
            Log.e("Fitxers:", "Error de fitxer");
        }

    }
}
