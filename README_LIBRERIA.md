# LibCategoria — Librería Java Profesional v1.0.0

Paquete: `com.geordy.libcategoria` | Java 21 | Spring Boot 3.x | MySQL 8.x

---

## Estructura del proyecto

```
LibCategoria/
├── src/com/geordy/libcategoria/
│   ├── dao/
│   │   ├── CategoriaDAO.java
│   │   └── Conexion.java
│   └── dto/
│       └── CategoriaDTO.java
├── tools/
│   └── proguard.jar          ← Descargar manualmente (ver Paso 1)
├── libs/
│   └── mysql-connector-j.jar ← Copia automática (ant setup-libs)
├── dist-lib/
│   └── libcategoria-1.0.0.jar
├── dist-protected/
│   └── libcategoria-1.0.0-protected.jar
├── examples/
│   ├── CategoriaController.java
│   ├── build.gradle
│   └── pom.xml
├── proguard.cfg
├── proguard-dict.txt
└── build.xml
```

---

## PASO 1 — Descargar ProGuard

Ir a: https://github.com/Guardsquare/proguard/releases
Descargar `proguard-x.x.x.zip`, extraer y copiar:
`proguard/lib/proguard.jar` → `tools/proguard.jar`

> Versión mínima requerida: ProGuard 7.3+ (para Java 17/21)

---

## PASO 2 — Generar los JARs (desde terminal en el proyecto)

```bash
# JAR normal (sin ofuscar)
ant jar-library

# JAR protegido con ProGuard
ant jar-protected

# Ambos de una vez
ant jar-all
```

---

## PASO 3 — Usar la librería en Spring Boot

### Gradle
```gradle
dependencies {
    implementation files('libs/libcategoria-1.0.0-protected.jar')
    runtimeOnly 'com.mysql:mysql-connector-j:9.7.0'
    implementation 'org.springframework.boot:spring-boot-starter-jdbc'
}
```

### Maven (instalar en repo local primero)
```bash
mvn install:install-file -Dfile=libcategoria-1.0.0-protected.jar \
  -DgroupId=com.geordy -DartifactId=libcategoria -Dversion=1.0.0 -Dpackaging=jar
```

### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/neptuno?useUnicode=true&characterEncoding=UTF-8&serverTimezone=America/Lima&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### Controller REST
```java
@Autowired DataSource dataSource;

@GetMapping
public List<CategoriaDTO> getAll() throws SQLException {
    try (CategoriaDAO dao = new CategoriaDAO(dataSource)) {
        return dao.obtenerTodas();
    }
}
```

### Uso standalone (sin Spring Boot)
```java
Conexion.configurar(
    "jdbc:mysql://localhost:3306/neptuno?useUnicode=true&characterEncoding=UTF-8&serverTimezone=America/Lima&useSSL=false&allowPublicKeyRetrieval=true",
    "root", ""
);
try (CategoriaDAO dao = new CategoriaDAO()) {
    dao.obtenerTodas().forEach(System.out::println);
}
```

---

## PASO 4 — Firma digital del JAR (opcional)

```bash
# Generar keystore (solo una vez)
keytool -genkey -alias libcategoria -keyalg RSA -keysize 2048 \
        -validity 3650 -keystore keystore.jks

# Firmar con Ant
ant jar-signed

# Verificar
jarsigner -verify dist-protected/libcategoria-1.0.0-protected.jar
```

---

## Recomendaciones anti-decompiler

| Técnica | Estado |
|---------|--------|
| Ofuscación de nombres (ProGuard) | Incluido |
| Diccionario confuso Il/lI/II | Incluido |
| Reempaquetado a paquete 'a' | Incluido |
| Firma digital del JAR | Ver Paso 4 |
| Cifrado de strings SQL | Implementar manualmente |
| Licenciamiento por clave cliente | Implementar manualmente |

Tras ProGuard: JD-GUI, CFR, Procyon y Fernflower verán
nombres como `Il`, `lI`, `II`, `lll` — completamente ilegibles.
