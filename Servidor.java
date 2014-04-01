import java.io.* ;
import java.net.* ;
class Servidor {
	static final int PUERTO=5000;
	public Servidor( ) {
		BufferedReader b;
		int mensaje= 0;
		char opcion= 0;
		int numCli = 0;
		
		try {
			ServerSocket skServidor = new ServerSocket( PUERTO );
			System.out.println("Escucho el puerto " + PUERTO );
			while(true){
				Socket skCliente = skServidor.accept(); // Acceptamos el cliente
				System.out.println("Sirvo al cliente " + numCli);
				b= new BufferedReader(new InputStreamReader( skCliente.getInputStream())); //Creamos el lector del Buffer
				mensaje = b.read(); //Leemos la entrada
				
				System.out.println("Su mensaje es:"+mensaje);
				if (mensaje==71){ //Se trata de un cliente Arduino
					OutputStream aux = skCliente.getOutputStream(); 
					DataOutputStream flujo= new DataOutputStream( aux );
					flujo.writeChar(opcion); //Escribimos la última acción que hemos recibido
					System.out.println("Enviamos "+opcion+" a la tarjeta Arduino");
				}
				else if(mensaje==97){ //Se trata de un cliente Android y
					opcion = 'a'; //Abre
				}
				else if(mensaje==99){ //Se trata de un cliente Android y
					opcion= 'c'; //Cierra
				}
				
				System.out.println(opcion);
				b.close();
				
				skCliente.close();
				numCli++;
			}
		}
		catch( Exception e ) {
			System.out.println( e.getMessage() );
		}
	}
	public static void main( String[] arg ) {
		new Servidor();
	}
}
