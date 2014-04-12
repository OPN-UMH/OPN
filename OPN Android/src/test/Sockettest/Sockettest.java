package test.Sockettest;

import java.io.*;
import java.net.Socket;

import test.Sockettest.R;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/* cliente*/
public class Sockettest extends Activity {
	/** Called when the activity is first created. */

	private Button btconect, btdisconect, bt1, bt2;
	private EditText ipinput, portinput;
	private ImageView leds;
	Socket miCliente;

	private boolean connected = false;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Mensaje_data mdata;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);

		leds = (ImageView) findViewById(R.id.leds);

		ipinput = (EditText) findViewById(R.id.ipinput);
		portinput = (EditText) findViewById(R.id.portinput);
		
		btconect = (Button) findViewById(R.id.btcnt);
		btdisconect = (Button) findViewById(R.id.btdisc);
		

		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		//bt3 = (Button) findViewById(R.id.bt3);
		//bt4 = (Button) findViewById(R.id.bt4);

	
		//Al clickear en conectar
		btconect.setOnClickListener(new OnClickListener() {
			@Override
			// conectar
			public void onClick(View v) {
			//Nos conectamos y obtenemos el estado de la conexion
				boolean conectstatus = Connect();
				//si nos pudimos conectar
				if (conectstatus) {//mostramos mensaje 
					mensaje("Conexion OK!");
					Change_leds(true);//camiamos img a verde

				} else {//error al conectarse 
					Change_leds(false);//camiamos img a rojo
					//mostramos msg de error
					mensaje("Error.. ");
				}
			}
		});
		//Al clickear en desconectar
		btdisconect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Disconnect();
			}
		});


		//Botones de Accion
		bt1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 PrintWriter salida;
					try {
						salida = new PrintWriter(
						new OutputStreamWriter(miCliente.getOutputStream()),true);
					
					 salida.println("a");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 PrintWriter salida;
					try {
						salida = new PrintWriter(
						new OutputStreamWriter(miCliente.getOutputStream()),true);
					
					 salida.println("c");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		
		/************/
	}
	//cambia el imageview segun status 
	public void Change_leds(boolean status) {
		if (status)
			leds.setImageResource(R.drawable.bombience);
		else
			leds.setImageResource(R.drawable.bombiapaga);
	}

	//Conectamos
	public boolean Connect() {
		//Obtengo datos ingresados en campos
		String IP = ipinput.getText().toString();
		int PORT = Integer.valueOf(portinput.getText().toString());

		try {//creamos sockets con los valores anteriores
			miCliente = new Socket(IP, PORT);
			//si nos conectamos
			if (miCliente.isConnected() == true) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			//Si hubo algun error mostrmos error
			mensaje(" !!! ERROR  !!!");
			Log.e("Error connect()", "" + e);
			return false;
		}
	}

	//Metodo de desconexion
	public void Disconnect() {
		try {
			//Prepramos mensaje de desconexion
			Mensaje_data msgact = new Mensaje_data();
			msgact.texto = "";
			msgact.Action = -1;
			msgact.last_msg = true;
			//avisamos al server que cierre el canal
			boolean val_acc = Snd_Msg(msgact);

			if (!val_acc) {//hubo un error
				mensaje(" Desconectado ");
				Change_leds(false);
				Log.e("Disconnect() -> ", "!ERROR!");

			} else {//ok nos desconectamos
				mensaje("Desconectado");
				//camibmos led a rojo
				Change_leds(false);
				Log.e("Disconnect() -> ", "!ok!");
				//cerramos socket	
				miCliente.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!miCliente.isConnected())
			Change_leds(false);
	}

	//Enviamos mensaje de accion segun el boton q presionamos
	public void Snd_Action(int bt) {
		Mensaje_data msgact = new Mensaje_data();
		//no hay texto
		msgact.texto = "";
		//seteo en el valor action el numero de accion
		msgact.Action = bt;
		//no es el ultimo msg
		msgact.last_msg = false;
		//mando msg
		boolean val_acc = Snd_Msg(msgact);
		//error al enviar
		if (!val_acc) {
			mensaje(" Error  ");
			Change_leds(false);
			Log.e("Snd_Action() -> ", "!ERROR!");

		}

		if (!miCliente.isConnected())
			Change_leds(false);
	}

	//Envio mensaje de texto 
	public void Snd_txt_Msg(String txt) {

		Mensaje_data mensaje = new Mensaje_data();
		//seteo en texto el parametro  recibido por txt
		mensaje.texto = txt;
		//action -1 no es mensaje de accion
		mensaje.Action = -1;
		//no es el ultimo msg
		mensaje.last_msg = false;
		//mando msg
		boolean val_acc = Snd_Msg(mensaje);
		//error al enviar
		if (!val_acc) {
			mensaje(" Error  ");
			Change_leds(false);
			Log.e("Snd_txt_Msg() -> ", "!ERROR!");
		}
		if (!miCliente.isConnected())
			Change_leds(false);
	}

	/*Metodo para enviar mensaje por socket
	 *recibe como parmetro un objeto Mensaje_data
	 *retorna boolean segun si se pudo establecer o no la conexion
	 */
	public boolean Snd_Msg(Mensaje_data msg) {

		try {
			//Accedo a flujo de salida
			oos = new ObjectOutputStream(miCliente.getOutputStream());
			//creo objeto mensaje
			Mensaje_data mensaje = new Mensaje_data();

			if (miCliente.isConnected())// si la conexion continua
			{
				//lo asocio al mensaje recibido
				
				mensaje = msg;
				//Envio mensaje por flujo
				oos.writeObject(mensaje);
			
				//envio ok
				return true;
				
								
			} else {//en caso de que no halla conexion al enviar el msg
				mensaje("Error...");//error
				return false;
			}

		} catch (IOException e) {// hubo algun error
			Log.e("Snd_Msg() ERROR -> ", "" + e);

			return false;
		}
	}
	public void mensaje(String cadena)
	{
		Toast.makeText(this, cadena, Toast.LENGTH_SHORT).show();
		
	}
}