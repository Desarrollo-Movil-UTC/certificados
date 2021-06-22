package com.utc.cursos_certificados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/*
@autores:Sandoval,Sanchez,Robayo
@creación: 19/06/2021
@Modificación: 19/06/2021
@descripción: Lista de Cursos
*/

public class GestionCursosActivity extends AppCompatActivity {

    //para inicio de sesion
    SharedPreferences preferences; //objeto de tipo sharedpreferences
    SharedPreferences.Editor editor; //objetito de tipo editor de sharedpreferences
    String llave = "sesion";

    BaseDatos miBdd;// creando un objeto para acceder a los procesos de la BDD SQlite

    //Salida
    ListView listCursosPantallaPrincipal;
    ArrayList<String> listaCursosPrincipal = new ArrayList<>(); //cargar los datos de la BDD

    //cursor para obtener los cursos
    Cursor cursosObtenidosPantallaPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cursos);

        //inicializar elementos shared
        preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();

        listCursosPantallaPrincipal=(ListView) findViewById(R.id.listCursosPantallaPrincipal);
        miBdd= new BaseDatos(getApplicationContext());
        consultarCursosPantallaPrincipal(); //invoca al metodo de listar los cursos

        //generar acciones cuando se da click sobre un curso de la lista
        listCursosPantallaPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursosObtenidosPantallaPrincipal.moveToPosition(position);

                //mediante el cursor se obtiene los datos del curso seleccionado
                String idCurso = cursosObtenidosPantallaPrincipal.getString(0);
                String nombreCurso = cursosObtenidosPantallaPrincipal.getString(1);
                String fechaInicioCurso = cursosObtenidosPantallaPrincipal.getString(2);
                String fechaFinCurso = cursosObtenidosPantallaPrincipal.getString(3);
                String duracionCurso = cursosObtenidosPantallaPrincipal.getString(4);
                String precioCurso = cursosObtenidosPantallaPrincipal.getString(5);

                //manejando el objeto para abrir la ventana de Lista de Estudiantes de un curso
                Intent ventanaListarEstudiantes = new Intent(getApplicationContext(), EstudiantesPorCursoActivity.class);
                ventanaListarEstudiantes.putExtra("idCurso", idCurso); //pasando el id del curso como parametro
                ventanaListarEstudiantes.putExtra("nombreCurso", nombreCurso);
                ventanaListarEstudiantes.putExtra("fechaInicioCurso", fechaInicioCurso);
                ventanaListarEstudiantes.putExtra("fechaFinCurso", fechaFinCurso);
                ventanaListarEstudiantes.putExtra("duracionCurso", duracionCurso);
                ventanaListarEstudiantes.putExtra("precioCurso", precioCurso);

                startActivity(ventanaListarEstudiantes);
                finish();
            }
        });
    }

    public void consultarCursosPantallaPrincipal(){
        listaCursosPrincipal.clear(); //vaciando el listado
        cursosObtenidosPantallaPrincipal = miBdd.obtenerCursos(); //consultando cursos y guardandolos en un cursor
        if(cursosObtenidosPantallaPrincipal != null){ //verificando que realmente haya datos dentro de SQLite

            do{
                String id = cursosObtenidosPantallaPrincipal.getString(0).toString();
                String nombreCurso = cursosObtenidosPantallaPrincipal.getString(1).toString();
                String fechaInicio = cursosObtenidosPantallaPrincipal.getString(2).toString();
                String fechaFin= cursosObtenidosPantallaPrincipal.getString(3).toString();
                String duracion= cursosObtenidosPantallaPrincipal.getString(4).toString();

                //construyendo las filas para presentar datos en el ListView
                listaCursosPrincipal.add(id+": "+nombreCurso+" Inicio: ("+fechaInicio+") Duración: "+duracion+" horas");

                //crear un adaptador para presentar los datos del listado (Java) en una lista simple (XML)
                ArrayAdapter<String> adaptadorCursos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaCursosPrincipal);
                listCursosPantallaPrincipal.setAdapter(adaptadorCursos); //presentando el adaptador dentro del listview

            }while(cursosObtenidosPantallaPrincipal.moveToNext()); //validando si aun existen cursos dentro del cursor
        }else{
            Toast.makeText(getApplicationContext(), "No existen cursos", Toast.LENGTH_LONG).show();
        }
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

    //MENU DE OPCIONES
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.gestionCursos){
            //proceso de cada boton
            Toast.makeText(this, "Gestión de Cursos" ,Toast.LENGTH_LONG).show();

            //abriendo la ventana de gestion de cursos
            Intent ventanaCursos=new Intent(getApplicationContext(),RegistroCursosActivity.class);
            startActivity(ventanaCursos); //solicitamos que habra el menu
            finish(); //cerrando la activity

        }else if(id == R.id.gestionEstudiantes){

            Toast.makeText(this, "Gestión de Estudiantes" ,Toast.LENGTH_LONG).show();
            Intent ventanaEstudiantes=new Intent(getApplicationContext(),EstudiantesActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
            startActivity(ventanaEstudiantes); //solicitamos que habra el formulario de login
            finish(); //cerrando la activity

        }else if (id == R.id.cerrarSesion){
            //cerrar sesion
            editor.putBoolean(llave,false);
            editor.apply();
            Toast.makeText(getApplicationContext(), "Cerrar Sesión", Toast.LENGTH_LONG).show();
            //redirijo a la actividad de inicio de sesion y cierro el menu
            Intent ventanaLogin=new Intent(getApplicationContext(),MainActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
            startActivity(ventanaLogin); //solicitamos que habra el formulario de login
            finish(); //cerrando la activity
        }
        return super.onOptionsItemSelected(item);

    }

}