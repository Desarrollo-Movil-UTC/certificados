package com.utc.cursos_certificados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 19/06/2021
@fModificación 19/06/2021
@descripción: inicio de sesion.
*/
public class MainActivity extends AppCompatActivity {
    //entrada
    EditText txtEmailLogin, txtPasswordLogin;     //creo objetos
    BaseDatos bdd;//creo objetito tipo bdd
    //para inicio de sesion
    SharedPreferences preferences; //objeto de tipo sharedpreferences
    SharedPreferences.Editor editor; //objeto de tipo editor de sharedpreferences
    String llave = "sesion";
    //proceso1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mapero de elementos Xml a objetos JAVA
        txtEmailLogin = (EditText) findViewById(R.id.txtEmailLogin);
        txtPasswordLogin = (EditText) findViewById(R.id.txtPasswordLogin);
        bdd=new BaseDatos(getApplicationContext());
        //inicializar elementos sahred
        preferences = this.getSharedPreferences("sesiones", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //validar si la sesion esta guardada o no
        if (revisarSesion()== true){
            Intent ventanaMenu = new Intent(getApplicationContext(),GestionCursosActivity.class);
            startActivity(ventanaMenu);
        }
    }

    //proceso 3 para validar usuario
    public void iniciarSesion(View vista){
        boolean sesion = false;
        //logica de negocio para capturar los valores ingresados en el campo email y password y consultarlos en la BDD
        //capturar valores ingresados por el usuario
        String email=txtEmailLogin.getText().toString();
        String password = txtPasswordLogin.getText().toString();
        //encripto el valor ingresado para que coincida con el que esta en la base de datos.
        password = getMD5(password);
        //siempre q hacemos consultas usamos el objeto cursor
        //COnsultar el usuario en la base de datos
        Cursor usuarioEncontrado = bdd.obtenerUsuarioPorEmailPassword(email,password);
        if(usuarioEncontrado != null){
            //Para el caso de que el email y contraseña ingresados sean correctos
            //Obteniendo el valor del email almacenadoe en la BDD
            String emailBdd = usuarioEncontrado.getString(5).toString();
            //Obteniendo el valor del email almacenadoe en la BDD
            String nombreBdd=usuarioEncontrado.getString(2).toString();
            //mostramos la bienvenida
            Toast.makeText(getApplicationContext(), "Bienvenido"+nombreBdd, Toast.LENGTH_LONG).show();
            //cerrando el formulario de inicio de seseion
            finish();
            //creando un objeto para manejar la ventana/antivyty del menu
            Intent ventanaMenu = new Intent(getApplicationContext(),GestionCursosActivity.class);
            sesion = true;
            guardarSesion(sesion);
            Toast.makeText(getApplicationContext(), "Sesion Guardada", Toast.LENGTH_LONG).show();
            //abrir el activity del menu de opciones
            startActivity(ventanaMenu);

        }else{
            //para el caso de que el usuarioEncontrado sea nulo se muestra un mensaje informativo al usuario
            Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_LONG).show();
            txtPasswordLogin.setText(""); //limpiamos el campo de la contraseña
        }
    }

    //proceso2
    public void abrirPantallaRegistro(View vista){ //metodo para abrir ventana de registro
        Intent pantallaRegistro= new Intent(getApplicationContext(),RegistroUsuariosActivity.class); //creando in intnt para invocar a registro activicty
        startActivity(pantallaRegistro); //iniciamos la pantalla de Registro

    }

    //metodos para verificar sesion
    private boolean revisarSesion(){
        boolean sesion = this.preferences.getBoolean(llave,false);
        return sesion;
    }

    //metodo para guardar sesion
    private void guardarSesion(boolean checked){
        editor.putBoolean(llave,checked); //editor que guardara la lave y el valor que tendra
        editor.apply(); //que guarde o aplique el cambio
    }

    //encriptar contraseña
    public static String getMD5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}