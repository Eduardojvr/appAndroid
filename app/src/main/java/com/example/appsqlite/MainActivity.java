package com.example.appsqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.appsqlite.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase mDatabase;
    public static final String PREFS_NAME = "preferencias";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = openOrCreateDatabase("minhaBase.db", Context.MODE_PRIVATE, null);


        setContentView(R.layout.activity_main);

    }

    public void criarBase(View view){
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS empregado (" +
                        " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " nome VARCHAR(200) NOT NULL," +
                        " departamento VARCHAR(200) NOT NULL," +
                        " salario double NOT NULL" +
                        ");"
        );
    }

    public void inserirDados(View view){
        int id = 1;
        String nome = "Dudu";
        String salario = "2500";
        String dept = "RH";

        String insertSQL = "INSERT INTO empregado" +
                "(nome, departamento, salario)" +
                "VALUES" +
                "(?,?,?);";

        mDatabase.execSQL(insertSQL, new String[]{nome, dept, salario});

        String mensagem = "Empregado adicionado com sucesso!";
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }



    public void atualizarDados(View view){
        String novoNome = "Eduardo";
        String novoSalario = "15000";

        String sql = "UPDATE empregado " +
                " set nome = ?," +
                "salario = ?" +
                " where id = ?;";

        mDatabase.execSQL(sql, new String[] {novoNome, String.valueOf(novoSalario), String.valueOf(1)});
        String mensagem = "Empregado Atualizado com sucesso!";
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    public void excluirDados(View view){
        String sql = "DELETE FROM empregado WHERE id = ?";

        mDatabase.execSQL(sql, new String[] {String.valueOf(2)});
    }

    public void listarDados(View view){

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM empregado", null);

        if(cursor.moveToFirst()){
            do {
                Log.d("Empregados", "id: " + cursor.getInt(0));
                Log.d("Empregados", "nome: " + cursor.getString(1));
                Log.d("Empregados", "departamento: " + cursor.getString(2));
                Log.d("Empregados", "salário: " + cursor.getString(3));


            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void mostrarResultadosNoTextView(View view) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM empregado", null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            StringBuilder resultados = new StringBuilder();

            do {
                @SuppressLint("Range")
                String nome = cursor.getString(cursor.getColumnIndex("nome"));

                @SuppressLint("Range")
                String departamento = cursor.getString(cursor.getColumnIndex("departamento"));

                @SuppressLint("Range")
                String salario = cursor.getString(cursor.getColumnIndex("salario"));

                resultados.append(nome).append("\n");
                resultados.append(departamento).append("\n");
                resultados.append(salario).append("\n");
                resultados.append("====================");


            } while (cursor.moveToNext());

            cursor.close();

            TextView textViewResultados = findViewById(R.id.resultadoTextView);

            textViewResultados.setText(resultados.toString());
        }
    }

    public void armazenar(View view) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("temperatura", "Celsius");
        editor.putString("medida", "polegadas");
        editor.putString("hora", "0-23");
        editor.putString("local", "São Paulo/Brasil");
        editor.commit();
    }

    public void recuperar(View view){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String temperatura, medida, hora, local;

        StringBuilder resultados = new StringBuilder();

        temperatura = settings.getString("temperatura", null);
        medida = settings.getString("medida", null);
        hora = settings.getString("hora", null);
        local = settings.getString("local", null);

        resultados.append(temperatura).append("\n");;
        resultados.append(medida).append("\n");;
        resultados.append(hora).append("\n");;
        resultados.append(local).append("\n");;

        Log.i("Dados ", "Conteúdo do arquivo: " +
                temperatura+ "\n" +
                hora + "\n" +
                local        + "\n" +
                medida
                );


        TextView textViewResultados = findViewById(R.id.resultadoTextView);

        textViewResultados.setText(resultados.toString());

    }


}