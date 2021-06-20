package com.utc.cursos_certificados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 19/06/2021
@fModificación 19/06/2021
@descripción: inicio de sesion.
*/

public class GestionCursosActivity extends AppCompatActivity {
    //para inicio de sesion
    SharedPreferences preferences; //objeto de tipo sharedpreferences
    SharedPreferences.Editor editor; //objetito de tipo editor de sharedpreferences
    String llave = "sesion";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cursos);
        //inicializar elementos sahred
                preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
/*
    public void cerrarSesion(View vista){
        editor.putBoolean(llave,false);
        editor.apply();
        Toast.makeText(getApplicationContext(), "Sesion Cerrada", Toast.LENGTH_LONG).show();
        //redirijo a la actividad de inicio de sesion y cierro el menu
        Intent ventanaLogin=new Intent(getApplicationContext(),MainActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
        startActivity(ventanaLogin); //solicitamos que habra el formulario de login
        finish(); //cerrando la activity de bienvenida
    }

 */

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.gestionCursos){
            //proceso de cada boton
            Toast.makeText(this, "gestion de Cursos" ,Toast.LENGTH_LONG).show();
        }else if(id == R.id.gestionEstudiantes){
            Toast.makeText(this, "gestion de Estudiantes" ,Toast.LENGTH_LONG).show();
        }else if (id == R.id.cerrarSesion){
            //cerrar sesion
            editor.putBoolean(llave,false);
            editor.apply();
            Toast.makeText(getApplicationContext(), "Sesion Cerrada", Toast.LENGTH_LONG).show();
            //redirijo a la actividad de inicio de sesion y cierro el menu
            Intent ventanaLogin=new Intent(getApplicationContext(),MainActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
            startActivity(ventanaLogin); //solicitamos que habra el formulario de login
            finish(); //cerrando la activity de bienvenida
        }
        return super.onOptionsItemSelected(item);

    }



}