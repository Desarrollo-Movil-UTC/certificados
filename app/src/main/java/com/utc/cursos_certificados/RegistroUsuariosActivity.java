package com.utc.cursos_certificados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/*
@autores:Sandoval,sanchez,Robayo
@creación/ 19/06/2021
@fModificación 19/06/2021
@descripción: Registro de usuarios.
*/
public class RegistroUsuariosActivity extends AppCompatActivity {
    //entrada
    EditText txtCedulaRegistro, txtNombresRegistro,
             txtApellidosRegistro, txtTelefonoRegistro,
             txtEmailRegistro,txtPasswordRegistro, txtPasswordConfirmada; // definiendo objetos para capturar datos

    BaseDatos miBdd;// creando un objeto para acceder a los procesos de la BDD SQlite

    //proceso1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);
        //mapeo de elementos
        txtCedulaRegistro = (EditText)findViewById(R.id.txtcedulaEst);
        txtNombresRegistro= (EditText)findViewById(R.id.txtNombresEst);
        txtApellidosRegistro=(EditText)findViewById(R.id.txtapellidosEst);
        txtTelefonoRegistro=(EditText)findViewById(R.id.txtTelefonoEst);
        txtEmailRegistro= (EditText)findViewById(R.id.txtEmailEst);
        txtPasswordRegistro=(EditText)findViewById(R.id.txtPasswordRegistro);
        txtPasswordConfirmada= (EditText)findViewById(R.id.txtPasswordConfirmada);
        miBdd= new BaseDatos(getApplicationContext()); //instanciar /construir la base de datos en el objeto mi bdd
    }

    public void registrarUsuario(View vista){
        //capturando los valores ingresados por el usuario en variables Java de tipo String
        String cedula= txtCedulaRegistro.getText().toString();
        String nombres=txtNombresRegistro.getText().toString();
        String apellidos=txtApellidosRegistro.getText().toString();
        String telefono=txtTelefonoRegistro.getText().toString();
        String email=txtEmailRegistro.getText().toString();
        String password=txtPasswordRegistro.getText().toString();
        String passwordConfirmada=txtPasswordConfirmada.getText().toString();

        //validaciones
        if (cedula.isEmpty() ||nombres.isEmpty() ||apellidos.isEmpty() ||telefono.isEmpty() || email.isEmpty()  || password.isEmpty()){ //si algun campo esta vacio
            Toast.makeText(getApplicationContext(), "Para continuar con el registro llene todos los campos solicitados",
                    Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast
        } else {
            if(validarCedula(cedula)==false){
                Toast.makeText(getApplicationContext(), "La cedula es de 10 digitos y no debe contener letras",
                        Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast
            }else {
                if (contieneSoloLetras(nombres) == false) {
                    Toast.makeText(getApplicationContext(), "El nombre no debe contener numeros",
                            Toast.LENGTH_LONG).show(); //mostrando error de nombre
                } else {
                    if (contieneSoloLetras(apellidos) == false) {
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
                                if (password.length() < 8) {
                                    Toast.makeText(getApplicationContext(), "Ingrese una contraseña mínimo de 8 dígitos",
                                            Toast.LENGTH_LONG).show(); //mostrando mensaje de contraseña invalida
                                } else {
                                    if (validarpassword(password) == false) {
                                        Toast.makeText(getApplicationContext(), "la contraseña debe tener numeros y letras",
                                                Toast.LENGTH_LONG).show(); //mostrando mensaje de contraseña invalida
                                    } else {
                                        if (password.equals(passwordConfirmada)) {
                                            password = getMD5(password);
                                            miBdd.agregarUsuario(cedula, nombres, apellidos, telefono, email, password);
                                            Toast.makeText(getApplicationContext(), "Usuario registrado correctamente",
                                                    Toast.LENGTH_LONG).show(); //mostrando mensaje de usuario registrado
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Las contraseñas ingresadas no coinciden",
                                                    Toast.LENGTH_LONG).show(); //mostrando un mensaje de error/alerta
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }


    //procesosalir
    public void cerrarPantalla(View vista){ //metodo para cerrar
        Intent ventanaMenu=new Intent(getApplicationContext(),MainActivity.class); //construyendo un objeto de tipo ventana para poder abrir la ventana de login
        startActivity(ventanaMenu); //solicitamos que habra el menu
        finish(); //cerrando la activity

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

    public boolean validarpassword(String password) {
        boolean numeros = false;
        boolean letras = false;
        for (int x = 0; x < password.length(); x++) {
            char c = password.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')  || c =='ñ' || c=='Ñ'
                    || c=='á' || c=='é' || c=='í' || c=='ó' || c=='ú'
                    || c=='Á' || c=='É' || c=='Í' || c=='Ó' || c=='Ú')) {
                letras = true;
            }
            if ((c >= '0' && c <= '9') ) {
                numeros = true;
            }

        }
        if (numeros == true && letras ==true){
            return true;
        }
        return false;
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