package com.utc.cursos_certificados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/*
@autores:Sandoval,Sanchez,Robayo
@creación: 19/06/2021
@Modificación: 19/06/2021
@descripción: certificado de Estudiante
*/

public class CertificadoEstudiante extends AppCompatActivity {

    //definicion de variables: para capturar los datos del estudiante
    String nombreEstudiante, apellidoEstudiante;

    //datos del curso
    String idCurso, nombreCurso, duracionCurso, fechaInicio, fechaFin;
    String nombreDirector;

    TextView textNombreCompletoEstudianteCertificado,
            textNombreCursoCertificado, textDuracionCursoCertificado, textFechaInicioCursoCertificado, textFechaFinCursoCertificado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificado_estudiante);

        //mapeo de elementos
        textNombreCompletoEstudianteCertificado=(TextView) findViewById(R.id.textNombreCompletoEstudianteCertificado);
        textNombreCursoCertificado=(TextView) findViewById(R.id.textNombreCursoCertificado);
        textDuracionCursoCertificado=(TextView) findViewById(R.id.textDuracionCursoCertificado);
        textFechaInicioCursoCertificado=(TextView) findViewById(R.id.textFechaInicioCursoCertificado);
        textFechaFinCursoCertificado=(TextView) findViewById(R.id.textFechaFinCursoCertificado);

        //Recibe datos del estudiante por curso y datos del curso
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                nombreEstudiante = parametrosExtra.getString("nombre");
                apellidoEstudiante = parametrosExtra.getString("apellido");

                idCurso = parametrosExtra.getString("idCur");
                nombreCurso = parametrosExtra.getString("cursoInscrito");
                fechaInicio = parametrosExtra.getString("fechaIni");
                fechaFin = parametrosExtra.getString("fechaFin");
                duracionCurso = parametrosExtra.getString("duracionC");
            }catch (Exception ex){ //ex recibe el tipo de error
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud "+ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
        //presentar los datos del estudiante, curso en pantalla
        String nombreCompleto = nombreEstudiante+" "+apellidoEstudiante;
        textNombreCompletoEstudianteCertificado.setText(nombreCompleto);

        textNombreCursoCertificado.setText(nombreCurso);
        textDuracionCursoCertificado.setText(duracionCurso);
        textFechaInicioCursoCertificado.setText(fechaInicio);
        textFechaFinCursoCertificado.setText(fechaFin);
    }

    //Boton Salir
    public void salirCertificadoEstudiante(View vista) {
        Intent ventanaListaEstudiantesPorCurso = new Intent(getApplicationContext(), EstudiantesPorCursoActivity.class);
        ventanaListaEstudiantesPorCurso.putExtra("idCurso", idCurso);
        ventanaListaEstudiantesPorCurso.putExtra("nombreCurso", nombreCurso);
        ventanaListaEstudiantesPorCurso.putExtra("fechaInicioCurso", fechaInicio);
        ventanaListaEstudiantesPorCurso.putExtra("fechaFinCurso", fechaFin);
        ventanaListaEstudiantesPorCurso.putExtra("duracionCurso", duracionCurso);
        startActivity(ventanaListaEstudiantesPorCurso);
        finish();
    }
}