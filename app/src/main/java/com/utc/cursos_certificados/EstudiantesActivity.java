package com.utc.cursos_certificados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 21/06/2021
@fModificación 19/06/2021
@descripción: inicio de sesion.
*/

public class EstudiantesActivity extends AppCompatActivity {
    //entrada
    EditText txtCedulaEstudiante, txtNombreEstudiante,txtApellidoEstudiante,txtTelefonoEstudiante, txtEmailEstudiante;
    BaseDatos bdd;

    //salida
    ListView lstEstudiantes;
    ArrayList<String> listaEstudiantes = new ArrayList<>();

    //Spiner***********************
    Spinner conboCursos; //defino variable para el spiner
    TextView txtCursoSeleccionado;
    //defino unas listas
    ArrayList<String> listaCursos;  //representa los datos que se muestran en el combo
    Cursor cursosObtenidos; //declaracion global para trabajat con los cursos.
    String cursoEstudiante;
    //*********************************

    // declaracion del cursor
    Cursor estudiantesObtenidos; //declaracion global para usarla desde culquier metodo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);
        //mapeo de elementos
        txtCedulaEstudiante = (EditText) findViewById(R.id.txtcedulaEst);
        txtNombreEstudiante = (EditText) findViewById(R.id.txtNombresEst);
        txtApellidoEstudiante = (EditText) findViewById(R.id.txtapellidosEst);
        txtTelefonoEstudiante = (EditText) findViewById(R.id.txtTelefonoEst);
        txtEmailEstudiante = (EditText) findViewById(R.id.txtEmailEst);
        lstEstudiantes = (ListView) findViewById(R.id.lstEstudiantes);
        //mapeo spiner************
        txtCursoSeleccionado=(TextView)findViewById(R.id.txtCursoSeleccionado);
        conboCursos = (Spinner) findViewById(R.id.comboCursos);
        //************************
        bdd = new BaseDatos(getApplicationContext()); // instanciando el objeto para llamar a los metodos de la base de datos
        //llenar spiner************
        consultarCursos();
        //cuando seleccione un elemento
        conboCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //que hacer cuando seleccione
                if(position != 0){
                    cursoEstudiante=listaCursos.get(position);
                }else{
                    cursoEstudiante="";
                }
                txtCursoSeleccionado.setText(cursoEstudiante);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //***************************

        consultarDatos(); //llamando al metodo para cargar los datos de clietnes en el listview
        //generar acciones cuando se da clic sobre un elemento de la lista de clientes
        lstEstudiantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //recibe la vista y la posision
                //programacion de acciones cuando se da clic en un elemento /item de la lista de estudiantes
                //Toast.makeText(getApplicationContext(),"seleccionaste la posicion "+position ,Toast.LENGTH_LONG).show();
                estudiantesObtenidos.moveToPosition(position); // moviendo el cursor a la posisicon donde se dio click
                //obteniendo la informacion de cada uno de los campos de la fila de cliente seleccionada
                String idEstudiante = estudiantesObtenidos.getString(0);
                String cedulaEstudiante = estudiantesObtenidos.getString(1);
                String nombreEstudiante = estudiantesObtenidos.getString(2);
                String apellidoEstudiante = estudiantesObtenidos.getString(3);
                String telefonoCliente = estudiantesObtenidos.getString(4);
                String emailEstudiante = estudiantesObtenidos.getString(5);
                String cursoEstudiante = estudiantesObtenidos.getString(6);
                //generando el objeto para abrir la ventana de edicion/eliminacion de estudiantes enviando los datos del estudiante como parametros
                Intent ventanaEditarEstudiante = new Intent(getApplicationContext(), ActualizarEliminarEstudiantesActivity.class);
                //enviar parametros al intent
                ventanaEditarEstudiante.putExtra("id", idEstudiante);
                ventanaEditarEstudiante.putExtra("cedula", cedulaEstudiante);
                ventanaEditarEstudiante.putExtra("nombre", nombreEstudiante);
                ventanaEditarEstudiante.putExtra("apellido", apellidoEstudiante);
                ventanaEditarEstudiante.putExtra("telefono", telefonoCliente);
                ventanaEditarEstudiante.putExtra("email", emailEstudiante);
                ventanaEditarEstudiante.putExtra("curso", cursoEstudiante);
                startActivity(ventanaEditarEstudiante); //solicitando que se abra la ventana de edicion /eliminacion
                finish(); //cerando la ventana actual
            }
        });

    }

    //metodo para consultar los estudiantes existentes en sqlite y presentarlos en una lista
    public void consultarDatos(){
        //limapiado por si hay datos anteriormente
        listaEstudiantes.clear();
        estudiantesObtenidos=bdd.obtenerEstudiantes(); // consultando estudiantes y guardandoles en un cursor
        //validar si hay datos
        if (estudiantesObtenidos != null ){ // si es diferrente de null
            //proceso cuando si hay estudiantes en la bdd
            do{
                String id=estudiantesObtenidos.getString(0).toString(); //capturando el id de lciente
                String nombre = estudiantesObtenidos.getString(2) ;// Capturando el nombre
                String apellido = estudiantesObtenidos.getString(3); //capturando el apellido
                //construyendo las filas para presentar datos en el listview ej => 1: bryan sandoval
                listaEstudiantes.add(id+": "+nombre+" "+apellido);
                //creando un adaptador para poder presentar los datos del listado de clientes(java)
                ArrayAdapter<String> adaptadorEstudiantes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaEstudiantes);
                lstEstudiantes.setAdapter(adaptadorEstudiantes); //presentando el adaptador de clientes dentro del list view
            }while(estudiantesObtenidos.moveToNext()); //movetonext nos dira si ahun existen clientes

        }else{
            Toast.makeText(getApplicationContext(),"no existen Estudiantes",Toast.LENGTH_LONG).show();
        }

    }

    //metodo para limpiar elementps
    public void limpiarCampos (View Vista) {
        txtCedulaEstudiante.setText("");
        txtNombreEstudiante.setText("");
        txtApellidoEstudiante.setText("");
        txtTelefonoEstudiante.setText("");
        txtEmailEstudiante.setText("");
        txtCedulaEstudiante.requestFocus();
    }

    //procesosalir
    public void cerrarPantalla(View vista){ //metodo para cerrar
        Intent ventanaMenu=new Intent(getApplicationContext(),GestionCursosActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
        startActivity(ventanaMenu); //solicitamos que habra el menu
        finish(); //cerrando la activity

    }

    public void guardarEstudiante(View vista){
        //capturar valores ingresados por el usuario
        String cedula = txtCedulaEstudiante.getText().toString();
        String nombre = txtNombreEstudiante.getText().toString();
        String apellido = txtApellidoEstudiante.getText().toString();
        String telefono = txtTelefonoEstudiante.getText().toString();
        String email = txtEmailEstudiante.getText().toString();

        //validaciones
        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || email.isEmpty()){ //si algun campo esta vacio
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
                                bdd.agregarEstudiante(cedula,nombre,apellido,telefono,email,cursoEstudiante); //insercion en la tabla cliente
                                limpiarCampos(null); //liampiando los cmapos del formulario
                                //presentando un mensaje de confirmacion
                                Toast.makeText(getApplicationContext(),"Datos guardados",Toast.LENGTH_LONG).show();
                                //actualizar lista de lcientes
                                consultarDatos(); //recarga el lisntado luego de la insercion
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
    //******************* trabajando con spiner ************
    public void consultarCursos(){
        cursosObtenidos=bdd.obtenerCursos(); // consultando los cursos y guardandoles en un cursor
        //validar si hay datos
        if (cursosObtenidos != null ){ // si es diferente de null
            //proceso cuando si hay cursos en la bdd
             listaCursos = new ArrayList<String>();
             listaCursos.add("Curso que va a tomar");
             for (cursosObtenidos.moveToFirst(); !cursosObtenidos.isAfterLast(); cursosObtenidos.moveToNext()){
                 String id=cursosObtenidos.getString(0).toString(); //capturando el id de lciente
                 String nombre = cursosObtenidos.getString(1) ;// Capturando el nombre
                 String fechaInicio = cursosObtenidos.getString(2); //capturando la fecha de inicio
                 if(validarFechaCursos(fechaInicio)==true){
                     //construyendo las filas para presentar datos en el listview ej => 1: karate
                     listaCursos.add(nombre);
                     //creando un adaptador para poder presentar los datos en el espiner
                     ArrayAdapter<CharSequence> adaptador= new ArrayAdapter(this, android.R.layout.simple_spinner_item,listaCursos);
                     conboCursos.setAdapter(adaptador); //oresentando el adaptador dentro del spiner.
                     //ArrayAdapter<String> adaptadorEstudiantes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaEstudiantes);
                     //lstEstudiantes.setAdapter(adaptadorEstudiantes); //presentando el adaptador de clientes dentro del list view
                 }

             }

        }else{
            Toast.makeText(getApplicationContext(),"no existen cursos registrados para matricular un estudiante",Toast.LENGTH_LONG).show();
        }
    }

    public boolean validarFechaCursos(String fechaInicioCursoString){
        //formato que quiero de  fecha
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            //obtener fecha de hoy y se lo guarda en stringFechaHoy con tipo fecha
            String stringFechaHoy = df.format(Calendar.getInstance().getTime());
            //casting de fechas a tipo Date
            Date fechaInicioCurso = df.parse(fechaInicioCursoString);
            Date fechaHoy =df.parse(stringFechaHoy);
            //validar que no sea igual o menos a la fecha actual

            if(fechaInicioCurso.before(fechaHoy)){ // si esta eligiendo el curso con fecha vencida
               // Toast.makeText(getApplicationContext(), "ya no se puede matricular el curso ya inicio o finalizo", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Error de formato en la fecha", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    //**********************************************

}