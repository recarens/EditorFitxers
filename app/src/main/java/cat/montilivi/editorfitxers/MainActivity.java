package cat.montilivi.editorfitxers;

import android.app.AlertDialog;
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
    Button btnLlegeix;
    Button btnSave;
    EditText etNom;
    EditText etCategoria;
    EditText etPreu;
    EditText etUnits;
    RadioButton rbInterna;
    RadioButton rbExterna;
    RadioButton rbRecursos;
    NumberPicker npLinea;
    ArrayList<String[]> liniaAL = new ArrayList<String[]>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLlegeix = (Button) findViewById(R.id.btnLlegeix);
        btnSave = (Button) findViewById(R.id.btnSave);
        etNom = (EditText) findViewById(R.id.etNom);
        etCategoria = (EditText) findViewById(R.id.etCategoria);
        etPreu = (EditText) findViewById(R.id.etPreu);
        etUnits = (EditText) findViewById(R.id.etUnitats);
        rbExterna = (RadioButton) findViewById(R.id.rbExterna);
        rbInterna = (RadioButton) findViewById(R.id.rbInterna);
        rbRecursos = (RadioButton) findViewById(R.id.rbRecursos);
        npLinea = (NumberPicker) findViewById(R.id.npNLineaText);

        npLinea.setMaxValue(77);
        npLinea.setMinValue(1);
        btnLlegeix.setOnClickListener(this);
        npLinea.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                Mostrar(numberPicker.getValue());
                npLinea.setValue(i2);
            }
        });
    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnLlegeix:
            {
                LlegeixFitxer();
                Mostrar(1);
                break;
            }
            case R.id.btnSave:
            {
                SaveFitxer();
                break;
            }
        }
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
    public  void Mostrar(int fila)
    {
        //npLinea.setValue(Integer.parseInt(liniaAL.get(fila)[0]));
        etNom.setText(liniaAL.get(fila-1)[1]);
        etCategoria.setText(liniaAL.get(fila-1)[2]);
        etPreu.setText(liniaAL.get(fila-1)[3]);
        etUnits.setText(liniaAL.get(fila-1)[4]);
    }
}
