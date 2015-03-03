package cat.montilivi.editorfitxers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

                    // Accedim a la targeta de memòria externa
                    boolean disponible = false;
                    boolean escrivible = false;

                    String estat = Environment.getExternalStorageState();

                    if(estat.equals(Environment.MEDIA_MOUNTED)) // Comprovem que la targeta està muntada
                    {
                        disponible = true;
                        escrivible = true;
                    }
                    else if (estat.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) // Comprobem si podem escriure
                    {
                        disponible = true;
                        escrivible = false;
                    }

                    if(disponible && escrivible)
                    {
                        //File ruta_sd = Environment.getExternalStorageDirectory();  //Obtenim l'arrel de la SD
                        File ruta_sd = Environment.getExternalStorageDirectory();
                        File f = new File(ruta_sd.getAbsolutePath(),NOM_FITXER);

                        OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(f));
                        BufferedWriter bw = new BufferedWriter(ow);

                        Integer linia = 0;

                        String[] dadesProducte = new String[4];
                        dadesProducte[0] = etNom.getText().toString();
                        dadesProducte[1] = etCategoria.getText().toString();
                        dadesProducte[2] = etPreu.getText().toString();
                        dadesProducte[3] = etUnits.getText().toString();

                        Log.e("NUM PICKER: ", ""+npLinea.getValue());

                        liniaAL.set(npLinea.getValue()-1, dadesProducte);

                        while (linia<77) {

                            if(linia == (npLinea.getValue()-1))
                            {
                                bw.write(linia+";"+etNom.getText().toString()+";"+etCategoria.getText().toString()+";"+etPreu.getText().toString()+";"+etUnits.getText().toString()+"\n");
                            }
                            else
                            {
                                bw.write(liniaAL.get(linia)[0]+";"+liniaAL.get(linia)[1]+";"+liniaAL.get(linia)[2]+";"+liniaAL.get(linia)[3]+";"+liniaAL.get(linia)[4]+"\n");
                            }
                            linia++;
                        }
                        bw.close();
                        //f.createNewFile();
                        Log.e("Fitxers:", "Fitxer guardat a Externa");
                    }
                }
            }
            else if (rbInterna.isChecked())
            {
                OutputStreamWriter is = new OutputStreamWriter(openFileOutput(NOM_FITXER,Context.MODE_PRIVATE));
                BufferedWriter br = new BufferedWriter(is);
                Integer linia = 0;
                while (linia <77) {
                    br.write(liniaAL.get(linia)[0] + ";" + liniaAL.get(linia)[1] + ";" + liniaAL.get(linia)[2] + ";" + liniaAL.get(linia)[3] + ";" + liniaAL.get(linia)[4] + "\n");
                    linia++;
                }
                br.close();
                Log.e("Fitxers","Guardat a interna");
            }
            else
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("No es pot guardar en els recursos");
                dlgAlert.setTitle("Error");
                dlgAlert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the dialog
                            }
                        });
            }


        } catch (Exception e) {
            Log.e("Fitxers:", "Error al guardar el fitxer");
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
            }
            else if (rbExterna.isChecked()) {
                if (isExternalStorageReadable()) {
                    String linia = "";
                    File rutaSD = Environment.getExternalStorageDirectory();
                    File fitxerSD = new File(rutaSD.getAbsolutePath(),NOM_FITXER);
                    FileInputStream fisr = new FileInputStream(fitxerSD);
                    InputStreamReader is = new InputStreamReader(fisr);
                    BufferedReader br = new BufferedReader(is);
                    while ((linia = br.readLine()) != null) {
                        liniaAL.add(linia.split(";"));
                    }
                    br.close();
                    Log.e("FITXERS:","LLegit d'externa?");
                }
            }
            else if (rbInterna.isChecked())
            {
                InputStreamReader isr = new InputStreamReader(openFileInput(NOM_FITXER));
                BufferedReader br = new BufferedReader(isr);
                String linia = "";
                while ((linia = br.readLine()) != null) {
                    liniaAL.add(linia.split(";"));
                }
                br.close();
                Log.e("Fitxers","Guardat a interna");
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
