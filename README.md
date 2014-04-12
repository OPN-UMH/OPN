OPN
===

Aplicación para controlar un servomotor desde un dispositivo Android.

Estan situadas en tres carpetas diferentes cada una de las partes de este proyecto. 
Por un lado está el código correspondiente a la aplicación Android que es la que envía la señal al servidor.
Por otro lado tenemos el código para el servidor Java que funciona de pasarela para reenviar la señal al servidor correspondiente.
Por último, tenemos la parte correspondiente al código de Arduino. En él, según la señal que reciba mandará al servo realizar
un movimiento de apertura o uno de cierre.

Si se quiere se puede abrir a Internet colocando la IP de salida a éste pero no es muy recomendable por temas de seguridad
en la red. En nuestro caso hemos utilizado la red LAN para comunicarnos entre los tres dispositivos.
