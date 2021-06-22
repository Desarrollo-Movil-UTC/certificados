package com.utc.cursos_certificados;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 19/06/2021
@fModificación 19/06/2021
@descripción: inicio de sesion.
*/
public class ActualizarEliminarEstudiantesActivity extends AppCompatActivity {
    //definicion de variables
    String id,cedula,apellido,nombre,telefono,email,curso; // variables para capturar valores que vienen cmo parametro
    //definicion de objetos que vienen del xml
    TextView txtIdEstudianteEditar,txtCursoEstudianteEditar;
    EditText txtCedulaEstudianteEditar,txtNombreEstudianteEditar,txtApellidoEstudianteEditar,
            txtTelefonoEstudianteEditar, txtEmailEstudianteEditar;
    //objeto para manejar conexciones a BDD
    BaseDatos bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_eliminar_estudiantes);
        //mapeo de elmentos
        txtIdEstudianteEditar=(TextView)findViewById(R.id.txtIdEst);
        txtCursoEstudianteEditar=(TextView)findViewById(R.id.txtCursoEst);
        txtCedulaEstudianteEditar=(EditText)findViewById(R.id.txtcedulaEst);
        txtNombreEstudianteEditar=(EditText)findViewById(R.id.txtNombresEst);
        txtApellidoEstudianteEditar=(EditText)findViewById(R.id.txtapellidosEst);
        txtTelefonoEstudianteEditar=(EditText)findViewById(R.id.txtTelefonoEst);
        txtEmailEstudianteEditar=(EditText)findViewById(R.id.txtEmailEst);
        //capturar valores que vienen como parametro
        Bundle parametrosExtra=getIntent().getExtras(); //captura los parametros que se han pasado a esta actividad
        if (parametrosExtra != null){
            try {  // manejo de exepciones
                //intente realizar estas lineas de codigo:
                id = parametrosExtra.getString("id");
                cedula = parametrosExtra.getString("cedula");
                nombre = parametrosExtra.getString("nombre");
                apellido = parametrosExtra.getString("apellido");
                telefono = parametrosExtra.getString("telefono");
                email = parametrosExtra.getString("email");
                curso = parametrosExtra.getString("curso");

            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),"Error al procesar la solicitud" +ex.toString(),Toast.LENGTH_LONG).show();
            }
            txtIdEstudianteEditar.setText(id);
            txtCedulaEstudianteEditar.setText(cedula);
            txtNombreEstudianteEditar.setText(nombre);
            txtApellidoEstudianteEditar.setText(apellido);
            txtTelefonoEstudianteEditar.setText(telefono);
            txtEmailEstudianteEditar.setText(email);
            txtCursoEstudianteEditar.setText(curso);

            bdd=new BaseDatos(getApplicationContext()); //instanciacion el objeto a traves del cual se llaman los procesos de la base de datos

        }
    }

    //proceso de volver
    public void volver(View vista){
        finish();//cerrando la ventana de editar/cliente
        //creando un objeto paramanejar la bentana
        Intent ventanaGestionEstudiantes=new Intent(getApplicationContext(),EstudiantesActivity.class);
        startActivity(ventanaGestionEstudiantes); // solicitando que se abra la ventana de gestion de clietnes
    }

    //metodo para procesar la eliminacion para presentar una ventana de confirmacion
    public void eliminarEstudiante (View vista){
        //que se habla una ventanita pequeña con las opciones de eliminacion
        AlertDialog.Builder estructuraConfirmacion = new AlertDialog.Builder(this)
                .setTitle("CONFIRMACION")
                .setMessage("¿esta seguro de eliminar el estudiante seleccionado?")
                .setPositiveButton("si, Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //procesar la eliminarcion
                        bdd.eliminarEstudiante(id); //llamo al metodo eliminar y le paso el id del estudiante
                        Toast.makeText(getApplicationContext(),"Estudiante Eliminado" ,Toast.LENGTH_SHORT).show();
                        volver(null); //invocando al metodo volver
                    }
                }).setNegativeButton("No, Calcelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //si le da a este boton no se hara ninguna accin y solo aparece el toast.
                        Toast.makeText(getApplicationContext(),"Eliminacion Cancelada" ,Toast.LENGTH_SHORT).show();

                    }
                }).setCancelable(true); //que se pueda cnacelar la operacion que se esta realziando
        AlertDialog cuadroDialogo = estructuraConfirmacion.create(); //instanciando el cuadro de dialogo con la estructura indicada
        cuadroDialogo.show(); //mostrando en pantalla el cuadro de dialogo
    }

    //metodo para actualizar el registro del estudiante
    public  void actualizar(View vista){
        //capturar los valores que el usuario a ingresado o modificado
        String cedula = txtCedulaEstudianteEditar.getText().toString();
        String nombre = txtNombreEstudianteEditar.getText().toString();
        String apellido = txtApellidoEstudianteEditar.getText().toString();
        String telefono = txtTelefonoEstudianteEditar.getText().toString();
        String email = txtEmailEstudianteEditar.getText().toString();
        String curso = txtCursoEstudianteEditar.getText().toString();
        //validaciones
        if (cedula.isEmpty() ||nombre.isEmpty() ||apellido.isEmpty() ||telefono.isEmpty() || email.isEmpty() || curso.isEmpty() ){ //si algun campo esta vacio
            Toast.makeText(getApplicationContext(), "Para continuar con el registro llene todos los campos solicitados",
                    Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast
        } else {
            if(validarCedula(cedula)==false){
                Toast.makeText(getApplicationContext(), "La cedula es de 10 digitos y no debe contener letras",
                        Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast
            }else {
                if (contieneSoloLetras(nombre) == false) {
                    Toast.makeText(getApplicationContext(), "El nombre no debe contener numeros",
                            Toast.LENGTH_LONG).show(); //mostrando error de nombre
                } else {
                    if (contieneSoloLetras(apellido) == false) {
                        Toast.makeText(getApplicationContext(), "El apellido no debe contener numeros",
                                Toast.LENGTH_LONG).show(); //mostrando error de apellido
                    } else {
                        if (validartelefono(telefono) == false) {
                            Toast.makeText(getApplicationContext(), "El numero de télefono debe tener 10 digitos, empezar con 09 y no tener letras ",
                                    Toast.LENGTH_LONG).show(); //mostrando error de nombre
                        } else {
                            Pattern pattern = Patterns.EMAIL_ADDRESS;
                            if (pattern.matcher(email).matches() == false) { //no cumple el correo
                                Toast.makeText(getApplicationContext(), "Ingrese un Email Valido",
                                        Toast.LENGTH_LONG).show(); //mostrando correo invalido
                            } else {
                                //proceso de insercion en la base de datos
                                bdd.actualizarEstudiante(cedula,nombre,apellido,telefono,email,curso,id); //modificando en la tabla cliente respecto al id
                                Toast.makeText(getApplicationContext(),"Datos guardados",Toast.LENGTH_LONG).show(); //presentando un mensaje de confirmacion
                                volver(null); //llamo al metodo volver
                            }
                        }
                    }
                }
            }
        }

    }


    public boolean validarCedula(String cedula) {
        if (cedula.length() != 10){
            return false;
        } else{
            for (int x = 0; x < cedula.length(); x++) {
                char c = cedula.charAt(x);
                // Si no está entre a y z, ni entre A y Z, ni es un espacio
                if (!(c >= '0' && c <= '9')) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean contieneSoloLetras(String cadena) {
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ' || c =='ñ' || c=='Ñ'
                    || c=='á' || c=='é' || c=='í' || c=='ó' || c=='ú'
                    || c=='Á' || c=='É' || c=='Í' || c=='Ó' || c=='Ú')) {
                return false;
            }
        }
        return true;
    }

    public boolean validartelefono(String telefono) {
        if (telefono.length() != 10){
            return false;
        } else{
            for (int x = 0; x < telefono.length(); x++) {
                char c = telefono.charAt(x);
                //si el primero no es 0
                if( x==0 && c != '0'){
                    return false;
                }
                //si el segundo no es 9
                if(x==1 && c != '9'){
                    return false;
                }
                //si el resto no tiene numeros
                if (!(c >= '0' && c <= '9')) {
                    return false;
                }
            }
        }
        return true;
    }

}