# 🚨 Alert on the Way - API de Gestión de Incidentes en Rutas

## 📌 Descripción
Tribu JavaShark - Cápsula Bases No Relacionales y Manejo de Caché.

**Alert on the Way** es una API REST diseñada para la gestión de incidentes en rutas. Permite registrar, consultar, eliminar incidentes y generar reportes de rutas, almacenando datos en MongoDB y optimizando consultas con Redis.

## 🛠 Tecnologías Utilizadas
- Java 17
- Spring Boot
- MongoDB
- Redis
- Docker

## 🚀 Instalación y Ejecución
1. Clona este repositorio:
   ```sh
   git clone https://github.com/tu_usuario/alert-on-the-way.git
   cd alert-on-the-way
   ```
2. Construye el proyecto con Maven:
   ```sh
   mvn clean package
   ```
3. Levanta los servicios con Docker:
   ```sh
   docker-compose up -d
   ```

## 📡 Endpoints Disponibles

### 1️⃣ Registrar un Incidente
**POST** `/api/alert-on-the-way/register`
- **Ejemplo de solicitud:**
  ```json
  {
      "routeName": "Ruta 1",
      "kilometer": 150,
      "type": "Accidente",
      "comments": "Choque entre dos vehículos"
  }
  ```
- **Respuesta esperada:**
  ```json
  {
    "id": "67b9ed95719af91418da0e1e",
    "route": {
        "id": "67b9ec48719af91418da0e12",
        "name": "Ruta 1",
        "origin": "Ciudad A",
        "destination": "Ciudad B",
        "distance": 300.0,
        "intersections": [
            {
                "routeId": "67b9ec48719af91418da0e13",
                "kilometer": 100.0
            },
            {
                "routeId": "67b9ec48719af91418da0e14",
                "kilometer": 200.0
            }
        ]
    },
    "kilometer": 120.0,
    "type": {
        "id": "67b9ec48719af91418da0e0a",
        "name": "Accidente",
        "severity": 3
    },
    "timestamp": "2025-02-22T12:30:29",
    "comments": "Choque entre dos vehículos"
  }
  ```

### 2️⃣ Consultar Incidentes de una Ruta
**GET** `/api/alert-on-the-way/incidents?name=Ruta 1&kmInit=50`
- **Respuesta esperada:**
  ```json
  {
  "routeId": "67b9ec48719af91418da0e12",
  "kmStart": 100.0,
  "kmEnd": 200.0,
  "incidents": [
    {
      "id": "67b9ecb1719af91418da0e1d",
      "kilometer": 150.0,
      "type": "Accidente",
      "severity": 3,
      "comments": "Choque entre dos vehículos",
      "timestamp": "2025-02-22T12:26:41"
    }
  ],
  "intersections": [
    {
      "intersectingRouteName": "Ruta 2",
      "intersectionKm": 100.0
    },
    {
      "intersectingRouteName": "Ruta 3",
      "intersectionKm": 200.0
    }
  ]
  }
  ```

### 3️⃣ Generar Reporte de Ruta
**GET** `/api/alert-on-the-way/route-report?name=Ruta 1`
- **Respuesta esperada:**
  ```json
  {
  "routeName": "Ruta 1",
  "reports": [
    {
      "segment": 100,
      "totalSeverity": 6
    },
    {
      "segment": 0,
      "totalSeverity": 4
    }
  ]
  }
  ```

### 4️⃣ Eliminar un Incidente
**DELETE** `/api/alert-on-the-way/delete/{id}`
- **Ejemplo:**
  ```sh
  DELETE http://localhost:8080/api/alert-on-the-way/delete/67b7e4b121a6f415c5120a64
  ```
- **Respuesta esperada:**
  ```json
  {
      "message": "Incident deleted successfully"
  }
  ```

## 📝 Notas Adicionales
- El archivo de Postman para probar los endpoints se encuentra en el directorio `src/main/resources`.
- MongoDB y Redis se ejecutan como contenedores dentro del `docker-compose.yaml`.
- Para detener los contenedores, usa:
  ```sh
  docker-compose down
  ```


