# Protocolo cliente-servidor y admin-servidor

En este documento se detallan como van a ser los mensajes que se van a transmitir los sistemas cliente, servidor y administrador del proyecto Recetas Divertidas.

El documento consta de 4 secciones: cliente a servidor; admin a servidor; servidor a cliente; servidor a admin. En cada una se detalla los mensajes con dirección correspondiente al nombre.

La estructura de los mensajes es la siguiente:
```
NOMBRE DEL MENSAJE [descripción]
<parámetro 1> [descripción]
<parámetro 2> [descripción]
...
<parámetro n> [descripción]
```

Estos mensajes hacen referencia a un array de Strings del lenguaje JAVA, donde el primer elemento del array va a ser el NOMBRE DEL MENSAJE y el resto de elementos, los parámetros. Si se desea enviar un mensaje sin parámetros, se envía un array con un solo elemento.

Si se desea agregar un mensaje más, se pide que se posicione en la categoría que corresponde y manteniendo el orden alfabético.
