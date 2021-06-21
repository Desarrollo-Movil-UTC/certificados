package com.utc.cursos_certificados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
@autores:Sandoval,Sanchez,Robayo
@creación: 19/06/2021
@Modificación: 19/06/2021
@descripción: Lista de Estudiantes por Curso
*/

public class EstudiantesPorCursoActivity extends AppCompatActivity {

    //definicion de variables: para capturar los valores de gestionCursos
    String idCurso, nombreCurso, fechaInicio, fechaFinal, duracion, precio;

    //Entrada
    TextView txtNombreCursoEstudiantes, txtFechaInicioCursoEstudiantes, txtFechaFinCursoEstudiantes, txtDuracionCursoEstudiantes;
    BaseDatos miBdd;// creando un objeto para acceder a los procesos de la BDD SQlite

    //Salida
    ListView listEstudiantesPorCurso;
    ArrayList<String> listaEstudiantesPorCurso = new ArrayList<>(); //cargar los datos de la BDD

    //cursor para obtener los estudiantes inscritos en un curso
    Cursor estudiantesObtenidosPorCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes_por_curso);

        //mapeo de elementos
        txtNombreCursoEstudiantes=(TextView) findViewById(R.id.txtNombreCursoEstudiantes);
        txtFechaInicioCursoEstudiantes=(TextView) findViewById(R.id.txtFechaInicioCursoEstudiantes);
        txtFechaFinCursoEstudiantes=(TextView) findViewById(R.id.txtFechaFinCursoEstudiantes);
        txtDuracionCursoEstudiantes=(TextView) findViewById(R.id.txtDuracionCursoEstudiantes);

        listEstudiantesPorCurso=(ListView) findViewById(R.id.listEstudiantesPorCurso);
        miBdd= new BaseDatos(getApplicationContext());

        consultarEstudiantesPorCurso(); //invoca al metodo de listar cursos

        //Recibe los datos de gestion de cursos
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                idCurso = parametrosExtra.getString("idCurso");
                nombreCurso = parametrosExtra.getString("nombreCurso");
                fechaInicio = parametrosExtra.getString("fechaInicioCurso");
                fechaFinal = parametrosExtra.getString("fechaFinCurso");
                duracion = parametrosExtra.getString("duracionCurso");
                precio = parametrosExtra.getString("precioCurso");
            }catch (Exception ex){//ex recibe el tipo de error
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud "+ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
        //presentar los datos del curso en pantalla
        txtNombreCursoEstudiantes.setText(nombreCurso);
        txtFechaInicioCursoEstudiantes.setText(fechaInicio);
        txtFechaFinCursoEstudiantes.setText(fechaFinal);
        txtDuracionCursoEstudiantes.setText(duracion);

        //generar acciones cuando se da click sobre un estudiante
        listEstudiantesPorCurso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                estudiantesObtenidosPorCurso.moveToPosition(position);
                //mediante el cursor se obtiene los datos del estudiante seleccionado en la lista
                String idEst = estudiantesObtenidosPorCurso.getString(0);
                String cedulaEst = estudiantesObtenidosPorCurso.getString(1);
                String nombreEst = estudiantesObtenidosPorCurso.getString(2);
                String apellidoEst = estudiantesObtenidosPorCurso.getString(3);
                String telefonoEst = estudiantesObtenidosPorCurso.getString(4);
                String emailEst = estudiantesObtenidosPorCurso.getString(6);
                //String nombreCurso = estudiantesObtenidosPorCurso.getString(2);

                //manejando el objeto para abrir la ventana de Datos del estudiante
                Intent ventanaDatosEstudiante = new Intent(getApplicationContext(), DatosEstudianteCursoActivity.class);
                ventanaDatosEstudiante.putExtra("idEst", idEst); //pasando el id del estudiante como parametro
                ventanaDatosEstudiante.putExtra("cedulaEst", cedulaEst);
                ventanaDatosEstudiante.putExtra("nombreEst", nombreEst);
                ventanaDatosEstudiante.putExtra("apellidoEst", apellidoEst);
                ventanaDatosEstudiante.putExtra("telefonoEst", telefonoEst);
                ventanaDatosEstudiante.putExtra("emailEst", emailEst);

                //datos del curso
                ventanaDatosEstudiante.putExtra("nombreCurso", nombreCurso);
                ventanaDatosEstudiante.putExtra("fechaInicio", fechaInicio);
                ventanaDatosEstudiante.putExtra("fechaFinal", fechaFinal);
                ventanaDatosEstudiante.putExtra("duracion", duracion);

                startActivity(ventanaDatosEstudiante);
                finish();
            }
        });
    }

    //Boton Salir
    public void salirEstudiantesPorCurso(View vista) {
        Intent ventanaGestionCursos = new Intent(getApplicationContext(), GestionCursosActivity.class);
        startActivity(ventanaGestionCursos);
        finish();
    }

    public void consultarEstudiantesPorCurso(){
        listaEstudiantesPorCurso.clear(); //vaciando el listado
        estudiantesObtenidosPorCurso = miBdd.obtenerEstudiantes(); //consultando estudiantes y guardandolos en un cursor
        if(estudiantesObtenidosPorCurso != null){ //verificando que realmente haya datos dentro de SQLite
            do{
                String id = estudiantesObtenidosPorCurso.getString(0).toString();
                String cedula= estudiantesObtenidosPorCurso.getString(1).toString();
                String nombre = estudiantesObtenidosPorCurso.getString(2).toString();
                String apellido = estudiantesObtenidosPorCurso.getString(3).toString();

                //construyendo las filas para presentar datos en el ListView
                listaEstudiantesPorCurso.add(id+": CI.: "+cedula+" Nombres: "+nombre+" "+apellido);

                //crear un adaptador para presentar los datos del listado (Java) en una lista simple (XML)
                ArrayAdapter<String> adaptadorCursos = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,listaEstudiantesPorCurso);
                listEstudiantesPorCurso.setAdapter(adaptadorCursos); //presentando el adaptador de estudiantes dentro del listview

            }while(estudiantesObtenidosPorCurso.moveToNext()); //validando si aun existen estudiantes en el cursor
        }else{
            Toast.makeText(getApplicationContext(), "No existen estudiantes registrados en este curso", Toast.LENGTH_LONG).show();
        }
    }
}