package com.utc.cursos_certificados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/*
@autores:Sandoval,Sanchez,Robayo
@creación: 19/06/2021
@Modificación: 19/06/2021
@descripción: Datos del estudiante de Curso
*/

public class DatosEstudianteCursoActivity extends AppCompatActivity {

    //definicion de variables: para capturar los valores de lista de estudiantes por curso
    String cedula, nombre, apellido, telefono, email, cursoInscrito;

    //variables para capturar datos del curso
    String fechaIni, fechaFin, duracionC;

    TextView txtCedulaDatosEstudiante, txtNombresDatosEstudiante, txtApellidosDatosEstudiante, txtTelefonoDatosEstudiante,
            txtEmailDatosEstudiante, txtCursoInscritoDatosEstudiante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_estudiante_curso);

        //mapoe de elementos
        txtCedulaDatosEstudiante=(TextView) findViewById(R.id.txtCedulaDatosEstudiante);
        txtNombresDatosEstudiante=(TextView) findViewById(R.id.txtNombresDatosEstudiante);
        txtApellidosDatosEstudiante=(TextView) findViewById(R.id.txtApellidosDatosEstudiante);
        txtTelefonoDatosEstudiante=(TextView) findViewById(R.id.txtTelefonoDatosEstudiante);
        txtEmailDatosEstudiante=(TextView) findViewById(R.id.txtEmailDatosEstudiante);
        txtCursoInscritoDatosEstudiante=(TextView) findViewById(R.id.txtCursoInscritoDatosEstudiante);

        //Recibe datos del estudiante por curso
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                cedula = parametrosExtra.getString("cedulaEst");
                nombre = parametrosExtra.getString("nombreEst");
                apellido = parametrosExtra.getString("apellidoEst");
                telefono = parametrosExtra.getString("telefonoEst");
                email = parametrosExtra.getString("emailEst");

                cursoInscrito = parametrosExtra.getString("nombreCurso");
                fechaIni = parametrosExtra.getString("fechaInicio");
                fechaFin = parametrosExtra.getString("fechaFinal");
                duracionC = parametrosExtra.getString("duracion");
            }catch (Exception ex){ //ex recibe el tipo de error
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud "+ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
        //presentar los datos del estudiante en pantalla
        txtCedulaDatosEstudiante.setText(cedula);
        txtNombresDatosEstudiante.setText(nombre);
        txtApellidosDatosEstudiante.setText(apellido);
        txtTelefonoDatosEstudiante.setText(telefono);
        txtEmailDatosEstudiante.setText(email);
        txtCursoInscritoDatosEstudiante.setText(cursoInscrito);
    }

    //Boton Salir
    public void salirDatosEstudiante(View vista) {
        Intent ventanaListaEstudiantesPorCurso = new Intent(getApplicationContext(), EstudiantesPorCursoActivity.class);
        ventanaListaEstudiantesPorCurso.putExtra("nombreCurso", cursoInscrito);
        ventanaListaEstudiantesPorCurso.putExtra("fechaInicioCurso", fechaIni);
        ventanaListaEstudiantesPorCurso.putExtra("fechaFinCurso", fechaFin);
        ventanaListaEstudiantesPorCurso.putExtra("duracionCurso", duracionC);
        startActivity(ventanaListaEstudiantesPorCurso);
        finish();
    }

    //Boton Generar Certificado
    public void generarCertificadoEstudiante(View vista){
        //nombre apellido setudiante
        //nombre curso
        //duraccion
        //Fecha inicio
        //fecha Fin
        //nombre del administraor

        //manejando el objeto para abrir la ventana de Certificado del estudiante
        Intent ventanaCertificadoEstudiante = new Intent(getApplicationContext(), CertificadoEstudiante.class);
        ventanaCertificadoEstudiante.putExtra("nombre", nombre);
        ventanaCertificadoEstudiante.putExtra("apellido", apellido);

        ventanaCertificadoEstudiante.putExtra("cursoInscrito", cursoInscrito);
        ventanaCertificadoEstudiante.putExtra("fechaIni", fechaIni);
        ventanaCertificadoEstudiante.putExtra("fechaFin", fechaFin);
        ventanaCertificadoEstudiante.putExtra("duracionC", duracionC);
        startActivity(ventanaCertificadoEstudiante);
        finish();
    }
}