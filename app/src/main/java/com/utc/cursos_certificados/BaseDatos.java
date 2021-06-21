package com.utc.cursos_certificados;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {
    private static final String nombreBdd = "bdd_certificados"; //definiendo el nombre dela Bdd
    private static final int versionBdd = 2; //definiendo la version de la BDD

    //estructura de la tabla usuarios
    private static final String tablaUsuario = "create table usuario(id_usu integer primary key autoincrement," +
            "cedula_usu text, nombre_usu text, apellido_usu text, telefono_usu text, is_admin_usu boolean, email_usu text, password_usu text)"; // definiendo estructura de la tabla usuarios

    //estructura de la tabla Curso
    private static final String tablaCurso = "create table curso(id_cur integer primary key autoincrement," +
            "nombre_cur text, fecha_inicio_cur text, fecha_fin_cur text, duracion_cur integer, precio_cur float);";


    /*
    //estructura de la tabla cliente
    private static final String tablaCliente = "create table cliente(id_cli integer primary key autoincrement," +
            "cedula_cli text, apellido_cli text, nombre_cli text, telefono_cli text, direccion_cli text)";

    //estructura de la tabla Productos
    private static final String tablaProducto = "create table producto(id_pro integer primary key autoincrement," +
            "nombre_pro text, precio_pro float, iva_pro boolean, stock_pro integer, fecha_caducidad_pro text);";
     */

    //Constructor
    public BaseDatos(Context contexto) {
        super(contexto, nombreBdd, null, versionBdd);
    }

    //proceso 1 crear la BDD
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tablaUsuario); // ejecutando el query DDl(sentencia de definicion de datos) para crear la tabla usuarios con sus atributos
        db.execSQL(tablaCurso);
        //db.execSQL(tablaCliente);// ejecutando el query DDl(sentencia de definicion de datos) para crear la tabla clientes
        //db.execSQL(tablaProducto);// ejecutando el query DDl(sentencia de definicion de datos) para crear la tabla Producto
    }

    //proceso 2: metodo que se ejecuta automaticamente cuando se detectan cambios en la version de nuestra bdd
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //cuando se actualice
        db.execSQL("DROP TABLE IF EXISTS usuario");//elimincacion de la version anterior de la tabla usuarios se puee usar otro comando Dll como alter table
        db.execSQL(tablaUsuario); //Ejecucion del codigo para crear la tabla usuaios con su nueva estructura

        db.execSQL("DROP TABLE IF EXISTS curso");//elimincacion de la version anterior de la tabla usuarios se puee usar otro comando Dll como alter table
        db.execSQL(tablaCurso); //Ejecucion del codigo para crear la tabla usuaios con su nueva estructura


        /*
        db.execSQL("DROP TABLE IF EXISTS cliente");//elimincacion de la version anterior de la tabla cliente se puee usar otro comando Dll como alter table
        db.execSQL(tablaCliente); //Ejecucion del codigo para crear la tabla cliente con su nueva estructura

        db.execSQL("DROP TABLE IF EXISTS producto");//elimincacion de la version anterior de la tabla producto se puee usar otro comando Dll como alter table
        db.execSQL(tablaProducto); //Ejecucion del codigo para crear la tabla producto con su nueva estructura
         */
    }

    /*crud
         c -> create
         r -> leer
         U -> ACTUALIZAR
         D -> eliminar

     */

    //proceso 3: metodo para insertar datos de la tabla usuarios , retorno true cuando inserto y false cuando hay algun error
    public boolean agregarUsuario(String cedula, String nombre, String apellido, String telefono, Boolean is_admin, String email, String password) {
        SQLiteDatabase miBdd = getWritableDatabase(); //llamando a la base de datos en el objeto mi Ddd
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into usuario(cedula_usu, nombre_usu, apellido_usu, telefono_usu, is_admin_usu, email_usu, password_usu) " +
                    "values('"+ cedula + "','" + nombre + "','" + apellido + "','" + telefono +"','" + is_admin +"','" + email + "','" + password + "')");    //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }

    //Proceso 4 para consultar usuarios por email y password.
    public Cursor obtenerUsuarioPorEmailPassword(String email, String password) {
        SQLiteDatabase miBdd = getWritableDatabase(); // llamado a la base de datos
        //crear un cursor donde inserto la consulta sql y almaceno los resultados en el objeto usuario
        Cursor usuario = miBdd.rawQuery("select * from usuario where " +
                "email_usu='" + email + "' and password_usu = '" + password + "';", null);

        //validar si existe o no la consulta
        if (usuario.moveToFirst()) { //metodo movetofirst nueve al primer elemento encontrado si hay el usuario
            return usuario; //retornamos los datos encontrados
        } else {
            //no se encuentra informacion de usuaio -> no existe o porque el email y password son incorrectos
            return null; //devuelvo null si no hay
        }
    }

    public Cursor obtenerEstudiantes(){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos

        //consultando los productos en la base de datos y guardando en un cursor
        Cursor cursos=miBdd.rawQuery("select * from usuario;", null);
        //validar si se encontro o no clientes
        if (cursos.moveToFirst()){
            miBdd.close();
            //retornar el cursor que contiene el listado de cliente
            return cursos; // retornar el cursor que contiene el listado de productos
        }else{
            return null; //se retorna nulo cuando no hay productos dentro de la tabla
        }
    }

    //CURSOS
    public boolean agregarCurso(String nombreCurso, String fechaInicio, String fechaFin, Integer duracionCurso, Float precioCurso){
        SQLiteDatabase miBdd =getWritableDatabase();
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into curso(nombre_cur, fecha_inicio_cur, fecha_fin_cur,duracion_cur, precio_cur) " +
                    "values  ('"+nombreCurso+"','"+fechaInicio+"','"+fechaFin+"','"+duracionCurso+"','"+precioCurso+"');");
            //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }


    //Metodo para consultar productos existentes en la base ded datos
    public Cursor obtenerCursos(){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos

        //consultando los productos en la base de datos y guardando en un cursor
        Cursor cursos=miBdd.rawQuery("select * from curso;", null);

        if (cursos.moveToFirst()){ //validar si se encontro o no clientes
            miBdd.close();
            //retornar el cursor que contiene el listado de cliente
            return cursos; // retornar el cursor que contiene el listado de productos
        }else{
            return null; //se retorna nulo cuando no hay productos dentro de la tabla
        }
    }

    public boolean actualizarCurso(String nombreCurso, String fechaInicio, String fechaFin, Integer duracionCurso,
                                   Float precioCurso, String id){
        SQLiteDatabase miBdd = getWritableDatabase(); // objeto para manejar la base de datos
        if(miBdd != null){
            //proceso de actualizacion
            miBdd.execSQL("update curso set nombre_cur='"+nombreCurso+"', " +
                    "fecha_inicio_cur='"+fechaInicio+"', fecha_fin_cur='"+fechaFin+"', " +
                    "duracion_cur='"+duracionCurso+"',precio_cur='"+precioCurso+"' where id_cur ="+id);
            miBdd.close(); //cerrando la conexion coon la BDD
            return true; //retornamos verdero ya que el proceso de actulaicacion fue exitoso
        }
        return false; // se retorna falso cuando no existe la base de datos
    }

    /*
    // CRUD para clientes

    //agregar un nuevo cliente
    public boolean agregarCliente(String cedula, String apellido, String nombre, String telefono, String direccion){
        SQLiteDatabase miBdd =getWritableDatabase();
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into cliente(cedula_cli, apellido_cli, nombre_cli,telefono_cli, direccion_cli) " +
                    "values  ('"+cedula+"','"+apellido+"','"+nombre+"','"+telefono+"','"+direccion+"');");
              //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }

    //Metodo para consultar clientes existentes en la base ded datos
    public Cursor obtenerClientes(){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos
        //consultando los clientes en la base de datos y guardando en un cursor
        Cursor clientes=miBdd.rawQuery("select * from cliente;", null);
        //validar si se encontro o no clientes
        if (clientes.moveToFirst()){
            miBdd.close();
            //retornar el cursor que contiene el listado de cliente
            return clientes; // retornar el cursor que contiene el listado de clietnes
        }else{
            return null; //se retorna nulo cuando no hay clientes dentro de la tabla
        }
    }

    //metodo para actializr un cliente
    public boolean actualizarCliente(String cedula, String apellido, String nombre, String telefono,
                                     String direccion, String id){
        SQLiteDatabase miBdd = getWritableDatabase(); // objeto para manejar la base de datos
        if(miBdd != null){
            //proceso de actualizacion
            miBdd.execSQL("update cliente set cedula_cli='"+cedula+"', " +
                    "apellido_cli='"+apellido+"', nombre_cli='"+nombre+"', " +
                    "telefono_cli='"+telefono+"',direccion_cli='"+direccion+"' where id_cli="+id);
            miBdd.close(); //cerrando la conexion coon la BDD
            return true; //retornamos verdero ya que el proceso de actulaicacion fue exitoso
        }
        return false; // se retorna falso cuando no existe la base de datos
    }


    //Metodo para eliminar un regostro de clietnes
    public boolean eliminarCliente(String id){
        SQLiteDatabase miBdd = getWritableDatabase(); // objeto para manejar la base de datos
        if(miBdd != null){ //validando que la bdd realmente exista
            miBdd.execSQL("delete from cliente where id_cli="+id); //ejecucion de la sentencia Sql para eliminar
            miBdd.close();
            return true; // //retornamos verdero ya que el proceso de eliminacion fue exitoso
        }
        return false; // se retorna falso cuando no existe la base de datos
    }





    //crud para productos
    //agregar un nuevo Producto
    public boolean agregarProducto(String nombre, Float precio, Boolean iva, Integer stock, String fechaCaducidad){
        SQLiteDatabase miBdd =getWritableDatabase();
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into producto(nombre_pro, precio_pro, iva_pro,stock_pro, fecha_caducidad_pro) " +
                    "values  ('"+nombre+"','"+precio+"','"+iva+"','"+stock+"','"+fechaCaducidad+"');");
            //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }



    //Metodo para consultar productos existentes en la base ded datos
    public Cursor obtenerProductos(){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos
        //consultando los productos en la base de datos y guardando en un cursor
        Cursor productos=miBdd.rawQuery("select * from producto;", null);
        //validar si se encontro o no clientes
        if (productos.moveToFirst()){
            miBdd.close();
            //retornar el cursor que contiene el listado de cliente
            return productos; // retornar el cursor que contiene el listado de productos
        }else{
            return null; //se retorna nulo cuando no hay productos dentro de la tabla
        }
    }

     */

}




