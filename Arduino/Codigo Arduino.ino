#include <SPI.h>
#include <Ethernet.h>
#include <Servo.h>


// Introduce la dirección MAC y la dirección IP para la tarjeta Arduino:

byte mac[] = { 0x38, 0xC8, 0x5C, 0x7F, 0xCF, 0x88 }; 
byte ip[]={192,168,1,25 };

byte server []= { 192,168,1,80}; // dirección IP del servidor
int delayTime1 = 15;

Servo miservo;
int pos=0;
int angulo=90; // Ángulo que queremos que gire nuestro servo
char c;
char aux=0;

EthernetClient client;

void setup()
{
  miservo.attach(9);// liga el servo conectado en el pin 9 al objeto servo  
}

void loop(){
  connectToPC(); // Conectamos con el servidor
  delay(300);
  
  while (client.available()) {

    c = client.read(); //Leemos la señal que proviene del servidor
    Serial.println(c);
     if(c==aux);
     else{
         if(c=='a'){ //Abre
            moveBackward();
         }
         else if(c=='c'){ //Cierra
            moveForward();
         }
    }
}
  

  if (!client.connected()) {
    Serial.println();
    Serial.println("Disconnecting");
    client.stop();
    delay(6000);
  }
  //delay(3000);
       aux=c; //Nos quedamos con el valor de la última acción para no repetirla
}


void connectToPC(){
  Serial.begin(9600);
  Ethernet.begin(mac, ip);
  
  delay(1000);

  Serial.println("Connecting...");

  if (client.connect(server, 5000)) {
    Serial.println("Connected");
    client.println("GET /search?q=arduino HTTP/1.0"); //NECESARIO PARA ESTABLECER LA CONEXIÓN
  } 
  else {
    Serial.println("Connection failed");
  }
}
  
void moveForward() {
  for(pos=0;pos<165;pos+=1){ 
        miservo.write(pos); //Vamos incrementando el ángulo     
        delay(delayTime1); //Controlamos la velocidad a partir del delay
  }
}

void moveBackward() {
  for(pos=angulo;pos>=1;pos-=1){
        miservo.write(pos);
        delay(delayTime1);    
  }
}

