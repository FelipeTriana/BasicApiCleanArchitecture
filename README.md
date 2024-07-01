# BasicApiCleanArchitecture

Ejemplo de arquitectura limpia en un proyecto Spring Webflux. Este proyecto administra un API de usuarios a través de operaciones reactivas.


## Pasos para ejecutar el proyecto

Clonar este repositorio ejecutando el siguiente comando en la consola:

```bash
git clone https://github.com/FelipeTriana/BasicApiCleanArchitecture.git
```

Ubicarse en la raíz del proyecto:
    
```bash
cd BasicApiCleanArchitecture
```

Verificar que la consola se este ejecutando con la versión 17 de Java: 

```bash
java -versión
```

(Si la versión es diferente se debe modificar la variable de entorno JAVA_HOME para que apunte a la versión 17 de Java)

Ejecutar el siguiente comando desde la raíz del proyecto si la consola es CMD:

```bash
gradlew.bat :applications-app-service:build
```

Para bash o powershell:

```bash
./gradlew :applications-app-service:build
```

Con esto tenemos generado el .jar


Ahora, tambien desde la raíz del proyecto ejecutar en la consola:

```bash
docker-compose up
```


Cuando los contenedores esten corriendo se puede acceder al siguiente get para ver una lista de usuarios inicialmente vacía:

[http://localhost:8080/api/user](http://localhost:8080/api/user) 

Desde postman se puede importar la siguiente colección para probar los endpoints:

[CleanArq.postman_collection.json](CleanArq.postman_collection.json)

Para obtener el reporte completo de Jacoco de cobertura de pruebas primero correr la tarea de applications-app-service en 
la sección verification llamada: **testCodeCoverageReport**, luego buscar el reporte generado en la ruta: 

applications/app-service/build/reports/jacoco/testCodeCoverageReport/html/index.html
