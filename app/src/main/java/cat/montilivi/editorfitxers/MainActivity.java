package cat.montilivi.editorfitxers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MainActivity extends ActionBarActivity{

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
    private Context context;

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

        btnLlegeix.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LlegeixFitxer();
                Mostrar(1);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SaveFitxer();

            }
        });

        npLinea.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                Mostrar(numberPicker.getValue());
                npLinea.setValue(i2);
            }
        });
    }
    private void SaveFitxer()
    {
        try {
            if (rbExterna.isChecked()) {
                if (isExternalStorageReadable()) {
                    Integer linia = 0;
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), NOM_FITXER);
                    FileOutputStream fisr = new FileOutputStream(file);
                    OutputStreamWriter is = new OutputStreamWriter(fisr);
                    BufferedWriter br = new BufferedWriter(is);
                    while (linia<77) {
                        br.write(liniaAL.get(linia)[0]+";"+liniaAL.get(linia)[1]+";"+liniaAL.get(linia)[2]+";"+liniaAL.get(linia)[3]+";"+liniaAL.get(linia)[4]+"\n");
                        linia++;
                    }
                    br.close();
                    file.createNewFile();
                }
            }
            else if (rbInterna.isChecked())
            {
                String yourFilePath = context.getFilesDir() + "/" + NOM_FITXER;
                File fileInterna = new File( yourFilePath );
                FileOutputStream fisr = new FileOutputStream(fileInterna);
                OutputStreamWriter is = new OutputStreamWriter(fisr);
                BufferedWriter br = new BufferedWriter(is);
                Integer linia = 0;
                while (linia <77) {
                    br.write(liniaAL.get(linia)[0] + ";" + liniaAL.get(linia)[1] + ";" + liniaAL.get(linia)[2] + ";" + liniaAL.get(linia)[3] + ";" + liniaAL.get(linia)[4] + "\n");
                    linia++;
                }
                br.close();
                fileInterna.createNewFile();
            }
        } catch (Exception e) {
            Log.e("Fitxers:", "Error de fitxer");
        }
    }

    private void LlegeixFitxer() {
        try {
            if (rbRecursos.isChecked()) {
                InputStream isr = getResources().openRawResource(R.raw.productes);
                InputStreamReader is = new InputStreamReader(isr);
                BufferedReader br = new BufferedReader(is);
                String linia = "";
                while ((linia = br.readLine()) != null) {
                    liniaAL.add(linia.split(";"));
                }
                br.close();
            } else if (rbExterna.isChecked()) {
                if (isExternalStorageReadable()) {
                    String linia = "";
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), NOM_FITXER);
                    FileInputStream fisr = new FileInputStream(file);
                    InputStreamReader is = new InputStreamReader(fisr);
                    BufferedReader br = new BufferedReader(is);
                    while ((linia = br.readLine()) != null) {
                        liniaAL.add(linia.split(";"));
                    }
                }
            }
            else if (rbInterna.isChecked())
            {
                String yourFilePath = context.getFilesDir() + "/" + NOM_FITXER;
                File fileInterna = new File( yourFilePath );
                FileInputStream fis = new FileInputStream(fileInterna);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String linia = "";
                while ((linia = br.readLine()) != null) {
                    liniaAL.add(linia.split(";"));
                }
            }
        } catch (Exception e) {
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

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
