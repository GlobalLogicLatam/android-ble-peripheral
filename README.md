# Globallogic - Bluetooth Low Energy workshop

Ejemplo desarrollado en el meetup llevado a cabo en Globallogic que consiste de un dispositivo de tipo "peripheral" que expone una caracteristica sobre la cual los dispositivos tipo "central" pueden realizar lecturas

## Descripcion

Esta aplicacion inicia un servicio para realizar el advertising del UUID de un servicio BLE a traves de Bluetooth Low Energy. Este paquete de advertising puede ser leido por cualquier otro dispositivo "central" que se encuentre escaneando en las cercanias.
Ademas implementa un Gatt server con un servicio ("163bb01e-f71c-48f4-a9de-2a0000aaaaaa") que incluye una caracteristica de lectura ("163bb01e-f71c-48f4-a9de-2a0000bbbbbb") que puede ser consultada por dispositivos "centrales".
Esta caracteristica devuelve el timestamp actual del dispositivo.


### Prerequisites

* Android API 21 o superior
* Dispositivo con chipset compatible con BLE

#### Instrucciones

Una vez instalado en tu dispositivo, la aplicacion comenzara a advertisear un UUID.
Este advertisement puede ser leido facilmente con una app de escaneo (existen varias gratuitas disponibles en el Play Store)
Ademas, puede conectarse al Gatt server de la aplicacion y leer el valor de la caracteristica que devuelve el timestamp actual del dispositivo


