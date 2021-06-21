package com.utc.cursos_certificados;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/*
@autores:Sandoval,Sanchez,Robayo
@creación: 19/06/2021
@Modificación: 19/06/2021
@descripción: Registro de Curso
*/

public class RegistroCursosActivity extends AppCompatActivity implements View.OnClickListener {

    //Entrada
    EditText txtNombreCurso, txtFechaInicioCurso, txtFechaFinCurso, txtDuracionCurso, txtPrecioCurso;
    Button btnFechaInicio, btnFechaFin;

    BaseDatos miBdd;// creando un objeto para acceder a los procesos de la BDD SQlite

    //Salida
    ListView listCursos;
    ArrayList<String> listaCursos = new ArrayList<>(); //cargar los datos de la BDD

    //declaracion global
    Cursor cursosObtenidos;

    // Capturar fecha actual y fecha ingresada
    private int Anio_act, Mes_act, Dia_act;
    private int Anio_inicio, Mes_inicio, Dia_inicio;
    private int Anio_fin, Mes_fin, Dia_fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cursos);

        //Mapeo de elementos
        txtNombreCurso = (EditText) findViewById(R.id.txtNombreCurso);
        txtFechaInicioCurso = (EditText) findViewById(R.id.txtFechaInicioCurso);
        txtFechaFinCurso = (EditText) findViewById(R.id.txtFechaFinCurso);
        txtDuracionCurso = (EditText) findViewById(R.id.txtDuracionCurso);
        txtPrecioCurso = (EditText) findViewById(R.id.txtPrecioCurso);

        btnFechaInicio = (Button) findViewById(R.id.btnFechaInicio);
        btnFechaInicio.setOnClickListener(this); //al dar click
        btnFechaFin = (Button) findViewById(R.id.btnFechaFin);
        btnFechaFin.setOnClickListener(this); //al dar click

        listCursos = (ListView) findViewById(R.id.listCursos);

        //instanciar /construir la base de datos en el objeto mi bdd
        miBdd = new BaseDatos(getApplicationContext());

        consultarCursos(); //invoca al metodo de listar los cursos

        //generar acciones cuando se da click sore un elemento de la lista de cursos
        listCursos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //programacion de acciones cuando se da click en un elemento/item de la lista de clientes

                cursosObtenidos.moveToPosition(position); // moviendo el cursor hacia la posicion donde se dio click

                //mediante el cursor se obtiene los datos del cliente seleccionado en la lista mediante las posiciones en el cursor
                String idCurso = cursosObtenidos.getString(0);
                String nombreCurso = cursosObtenidos.getString(1);
                String fechaInicioCurso = cursosObtenidos.getString(2);
                String fechaFinCurso = cursosObtenidos.getString(3);
                String duracionCurso = cursosObtenidos.getString(4);
                String precioCurso = cursosObtenidos.getString(5);

                //manejando el objeto para abrir la ventana de editarCurso
                Intent ventanaEditarCurso = new Intent(getApplicationContext(), EditarCursoActivity.class);
                ventanaEditarCurso.putExtra("idCurso", idCurso); //pasando el id del cliente como parametro
                ventanaEditarCurso.putExtra("nombreCurso", nombreCurso);
                ventanaEditarCurso.putExtra("fechaInicioCurso", fechaInicioCurso);
                ventanaEditarCurso.putExtra("fechaFinCurso", fechaFinCurso);
                ventanaEditarCurso.putExtra("duracionCurso", duracionCurso);
                ventanaEditarCurso.putExtra("precioCurso", precioCurso);

                startActivity(ventanaEditarCurso); //solicitando que se habra la ventana para editar curso
                finish(); //cerrando la ventana de editar curso una vez terminado el prceso de edicion
            }
        });
    }

    //Boton Cancelar
    public void LimpiarRegistroCurso(View vista) {
        txtNombreCurso.setText("");
        txtFechaInicioCurso.setText("");
        txtFechaFinCurso.setText("");
        txtDuracionCurso.setText("");
        txtPrecioCurso.setText("");
        txtNombreCurso.requestFocus();
    }

    //Boton Salir
    public void salirRegistroCurso(View vista) { //metodo para cerrar
        Intent ventanaGestionCursos = new Intent(getApplicationContext(), GestionCursosActivity.class);
        startActivity(ventanaGestionCursos);
        finish(); //cerrando la activity
    }

    //Boton guardar
    public void registrarCurso(View vista) {
        boolean is_admin_usu = true; //definir is_admin como falso por defecto.

        //capturando los valores ingresados por el usuario en variables Java de tipo String
        String nombreCurso = txtNombreCurso.getText().toString();
        String fechaInicio = txtFechaInicioCurso.getText().toString();
        String fechaFin = txtFechaFinCurso.getText().toString();
        String duracion = txtDuracionCurso.getText().toString();
        String precio = txtPrecioCurso.getText().toString();

        //validaciones
        //campos vacios
        if (nombreCurso.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty() || duracion.isEmpty() || precio.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Para continuar con el registro llene todos los campos solicitados",
                    Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast
        } else {
            if (contieneSoloLetras(nombreCurso) == false) {
                Toast.makeText(getApplicationContext(), "El nombre no debe contener números",
                        Toast.LENGTH_LONG).show(); //mostrando error de nombre
            } else { //DURACION diferente de 0
                int duracion2 = Integer.parseInt(duracion);
                if (duracion2 <= 0) {
                    Toast.makeText(getApplicationContext(), "La duración del curso debe ser mayor a 0 horas",
                            Toast.LENGTH_LONG).show();
                } else { //PRECIO diferente de 0
                    float precio2 = Float.parseFloat(precio);
                    if (precio2 > 0) {
                        miBdd.agregarCurso(nombreCurso, fechaInicio, fechaFin, duracion2, precio2);
                        LimpiarRegistroCurso(null); //limpia los campos y como es llamado desde la vista envia null
                        Toast.makeText(getApplicationContext(), "Curso creado exitosamente",
                                Toast.LENGTH_LONG).show();
                        consultarCursos(); //recargando el listado luego de la insercion

                    } else {
                        Toast.makeText(getApplicationContext(), "El Precio debe ser un valor aceptable",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public void consultarCursos() {
        listaCursos.clear(); //vaciando el listado

        cursosObtenidos = miBdd.obtenerCursos(); //consultando cursos y guardandolos en un cursor

        if (cursosObtenidos != null) { //verificando que realmente haya datos dentro de SQLite
            //proceso cuando si se encuentren cursos almacenados en la BDD
            do {
                String id = cursosObtenidos.getString(0).toString();
                String nombreCurso = cursosObtenidos.getString(1).toString();
                String fechaInicio = cursosObtenidos.getString(2).toString();
                String fechaFin = cursosObtenidos.getString(3).toString();
                String duracion = cursosObtenidos.getString(4).toString();

                //construyendo las filas para presentar datos en el ListView E
                listaCursos.add(id + ": "+nombreCurso+" Inicio: "+fechaInicio+" Finaliza: "+fechaFin+" Duración: "+duracion+" horas");

                //crear un adaptador para presentar los datos del listado de cursos (Java) en una lista simple (XML)
                ArrayAdapter<String> adaptadorCursos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCursos);
                listCursos.setAdapter(adaptadorCursos); //presentando el adaptador de cursos dentro del listview

            } while (cursosObtenidos.moveToNext()); //validando si aun existen cursos dentro del cursor
        } else {
            //presentando un mensaje de informacion cuando no existan cursos
            Toast.makeText(getApplicationContext(), "No existen cursos", Toast.LENGTH_LONG).show();
        }
    }

    public boolean contieneSoloLetras(String cadena) {
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ' || c == 'ñ' || c == 'Ñ'
                    || c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú'
                    || c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó' || c == 'Ú')) {
                return false;
            }
        }
        return true;
    }

    //METODOS PARA OBTENER Y VALIDAR LA FECHA DE CADUCIDAD
    @Override
    public void onClick(View v) {
        // obtenemos la fecha actual
        final Calendar calendario = Calendar.getInstance();
        Anio_act = calendario.get(Calendar.YEAR);
        Mes_act = calendario.get(Calendar.MONTH);
        Dia_act = calendario.get(Calendar.DAY_OF_MONTH);

        if (v == btnFechaInicio) {
            //creamos la instancia de tipo date
            //this indicamos que se despliegue en el mismo formulario
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                //captura la fecha seleccionada por el usuario
                @Override
                public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                    //las variables declaradas para fecha inicio toman la fecha seleccionada
                    Anio_inicio = anio;
                    Mes_inicio = mes;
                    Dia_inicio = dia;
                    validarFechaInicio();
                    //validarFechaFin();
                }
            }, Anio_act, Mes_act, Dia_act); //presenta picker con la fecha actual
            datePickerDialog.show(); //mostrando el date picker
        }

        if (v == btnFechaFin) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                //captura la fecha seleccionada por el usuario
                @Override
                public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                    Anio_fin = anio;
                    Mes_fin = mes;
                    Dia_fin = dia;
                    validarFechaFin();
                }
            }, Anio_act, Mes_act, Dia_act); //presenta picker con la fecha actual
            datePickerDialog.show(); //mostrando el date picker
        }
    }

    public boolean validarFechaInicio() {
        if (Anio_inicio >= Anio_act && Mes_inicio >= Mes_act && Dia_inicio >= Dia_act) {
            txtFechaInicioCurso.setText(Dia_inicio + "/" + (Mes_inicio + 1) + "/" + Anio_inicio);
            Toast.makeText(getApplicationContext(), "Fecha correcta", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Fecha seleccionada es incorrecta", Toast.LENGTH_LONG).show();
            txtFechaInicioCurso.setText("");
            return false;
        }
    }

    public boolean validarFechaFin() {
        if (Anio_fin >= Anio_inicio && Mes_fin >= Mes_inicio && Dia_fin >= Dia_inicio) {
            txtFechaFinCurso.setText(Dia_fin + "/" + (Mes_fin + 1) + "/" + Anio_fin);
            Toast.makeText(getApplicationContext(), "Fecha correcta", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Seleccione una fecha mayor a la fecha de inicio", Toast.LENGTH_LONG).show();
            txtFechaFinCurso.setText("");
            return false;
        }
    }
}