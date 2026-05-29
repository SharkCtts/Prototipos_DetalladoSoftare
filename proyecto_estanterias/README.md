# Estanterías Inteligentes INC — Prototipos de Patrones de Diseño

Ana María Bautista Rodríguez (95429) — Juan Andrés Veintinilla (124596)
USB Cali — Diseño Detallado de Software 2026-1

---

## Requisitos

- Java JDK 11 o superior
- Visual Studio Code
- Extensión: **Extension Pack for Java** (Microsoft)
  → Buscarla en el Marketplace de VS Code e instalarla

---

## Estructura del proyecto

```
proyecto_estanterias/
│
├── patron_composite/          ← Patrón Composite (Módulo Datos/Contenido)
│   ├── .vscode/
│   │   ├── settings.json
│   │   └── launch.json
│   └── src/composite/
│       ├── InventarioComponent.java   (interfaz común)
│       ├── Producto.java              (hoja)
│       ├── Estanteria.java            (compuesto nivel 3)
│       ├── Bodega.java                (compuesto nivel 2)
│       ├── Empresa.java               (raíz)
│       └── MainComposite.java         (demo ejecutable)
│
└── patron_visitor/            ← Patrón Visitor (Módulo Arquitectura)
    ├── .vscode/
    │   ├── settings.json
    │   └── launch.json
    └── src/
        ├── composite/
        │   ├── Producto.java          (elemento con accept())
        │   ├── Estanteria.java        (elemento con accept() + sensores)
        │   └── SensorIoT.java         (modelo del sensor IoT)
        └── visitor/
            ├── InventarioVisitor.java      (interfaz visitor)
            ├── ReporteStock.java           (visitante concreto #1)
            ├── AlertaMantenimiento.java    (visitante concreto #2)
            └── MainVisitor.java            (demo ejecutable)
```

---

## Cómo abrir y ejecutar en VS Code

### Patrón Composite
1. Abrir VS Code
2. `File → Open Folder` → seleccionar la carpeta `patron_composite`
3. Esperar a que VS Code indexe el proyecto (barra inferior azul)
4. Abrir `src/composite/MainComposite.java`
5. Hacer clic en **▶ Run** (aparece encima del método `main`)
   — o usar el atajo `F5`

### Patrón Visitor
1. Abrir VS Code
2. `File → Open Folder` → seleccionar la carpeta `patron_visitor`
3. Esperar a que VS Code indexe el proyecto
4. Abrir `src/visitor/MainVisitor.java`
5. Hacer clic en **▶ Run** encima del método `main`
   — o usar el atajo `F5`

> ⚠️ Abrir cada patrón como carpeta separada, NO abrir `proyecto_estanterias`
> completo como un solo folder, ya que son dos proyectos independientes.

---

## Qué hace cada demo

### MainComposite
Construye el árbol: Empresa → 2 Bodegas → 3 Estanterías → 7 Productos.
Demuestra que `getCantidadTotal()`, `getValorTotal()` y `mostrar()` funcionan
igual en cualquier nivel de la jerarquía (Empresa, Bodega, Estantería o Producto).
Al final agrega una nueva Bodega para demostrar la escalabilidad del patrón.

### MainVisitor
Construye 3 estanterías con productos y sensores IoT.
Aplica dos visitors sobre la misma jerarquía:
- **ReporteStock**: acumula datos de cada producto y genera un informe completo.
- **AlertaMantenimiento**: revisa los sensores de cada estantería y reporta fallos.
Demuestra que agregar una nueva funcionalidad (nuevo visitor) no requiere
modificar Estanteria, Producto ni SensorIoT.

### Demos gráficos
Se agregaron dos clases GUI para probar los patrones con botones y paneles de salida:
- `patron_composite/src/composite/MainCompositeGUI.java` → demo gráfico del Patrón Composite.
- `patron_visitor/src/visitor/MainVisitorGUI.java` → demo gráfico del Patrón Visitor.

Abrir cada proyecto en VS Code y ejecutar `MainCompositeGUI` o `MainVisitorGUI`
para ver el prototipo de front básico.
