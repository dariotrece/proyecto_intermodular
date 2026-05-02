const API_URL = "/libros";
const USUARIOS_API = "/usuarios";

//Control de sesión

document.addEventListener("DOMContentLoaded", function () {

    if (window.location.pathname === "/" || window.location.pathname === "/index.html") {

        fetch("/auth/me")
            .then(response => {

                const contentType = response.headers.get("content-type");

                // Si no devuelve JSON, significa que no está autenticado
                if (!contentType || !contentType.includes("application/json")) {
                    mostrarBotonLogin();
                    throw new Error("No autenticado");
                }

                return response.json();
            })
            .then(user => {

                if (user.rol === "BIBLIOTECARIO") {
                    window.location.href = "/admin.html";
                } else if (user.rol === "USUARIO") {
                    window.location.href = "/usuario.html";
                }

            })
            .catch(() => {
                console.log("Usuario no autenticado");
            });
    }

    if (window.location.pathname === "/admin.html" || window.location.pathname === "/usuario.html") {
        mostrarUsuario();
    }

    if (window.location.pathname === "/admin.html") {
        fetch("/auth/me")
            .then(res => res.json())
            .then(user => {
                if (user.rol !== "BIBLIOTECARIO") {
                    window.location.href = "/usuario.html";
                }
            });
    }

    if (window.location.pathname === "/usuario.html") {
        fetch("/auth/me")
            .then(res => res.json())
            .then(user => {
                if (user.rol !== "USUARIO") {
                    window.location.href = "/admin.html";
                }
            });
    }



});


function mostrarBotonLogin() {
    const loginDiv = document.getElementById("login-area");
    if (loginDiv) {
        loginDiv.innerHTML = `<a href="/login.html">Login</a>`;
    }
}

function mostrarUsuario() {

    fetch("/auth/me")
        .then(response => response.json())
        .then(user => {
            const loginDiv = document.getElementById("login-area");
            if (loginDiv) {
                loginDiv.innerHTML = `
                    Hola ${user.username} (${user.rol}) 
                    <a href="/logout">Logout</a>
                `;
            }
        });
}

//Libros

async function crearLibro() {
    const isbn = document.getElementById("isbn").value;
    const titulo = document.getElementById("titulo").value;
    const autor = document.getElementById("autor").value;

    if (!isbn || !titulo || !autor) {
        alert("Todos los campos son obligatorios");
        return;
    }

    if (isbn.length < 10 || isbn.length > 13) {
        alert("El ISBN debe tener entre 10 y 13 caracteres");
        return;
    }

    if (!/^[0-9-]+$/.test(isbn)) {
        alert("El ISBN solo puede contener números y guiones");
        return;
    }

    if (titulo.length < 2) {
        alert("El título debe tener al menos 2 caracteres");
        return;
    }

    if (autor.length < 2) {
        alert("El nombre del autor debe tener al menos 2 caracteres");
        return;
    }

    const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ isbn, titulo, autor })
    });

    const result = await response.json();
    alert(JSON.stringify(result, null, 2));
}

async function listarLibros() {
    const response = await fetch(API_URL);

    if (response.status === 403) {
        alert("No autorizado: tu sesión no tiene rol de bibliotecario.");
        return;
    }

    if (response.status === 401) {
        alert("No has iniciado sesión.");
        window.location.href = "/login.html";
        return;
    }

    if (!response.ok) {
        alert("Error al cargar los libros.");
        return;
    }

    const libros = await response.json();
    const tabla = document.getElementById("tablaLibros");

    if (!tabla) return;

    tabla.innerHTML = "";

    libros.forEach(libro => {
        const estadoTexto = libro.disponible ? "Disponible" : "Prestado";
        const estadoClase = libro.disponible ? "disponible" : "prestado";

        tabla.innerHTML += `
            <tr>
                <td>${libro.isbn}</td>
                <td>${libro.titulo}</td>
                <td>${libro.autor}</td>
                <td class="${estadoClase}">${estadoTexto}</td>
            </tr>
        `;
    });
}

async function buscarLibroISBN() {
    const isbn = document.getElementById("isbnLibro").value;

    const response = await fetch(`${API_URL}/${isbn}`);

    if (!isbn) {
        alert("Introduce un ISBN para buscar");
        return;
    }

    if (isbn.length < 10 || isbn.length > 13) {
        alert("El ISBN debe tener entre 10 y 13 caracteres");
        return;
    }

    if (!response.ok) {
        alert("Libro no encontrado");
        return;
    }

    const libro = await response.json();
    const estadoTexto = libro.disponible ? "Disponible" : "Prestado";
    const estadoClase = libro.disponible ? "disponible" : "prestado";

    document.getElementById("resultadoBusqueda").innerHTML = `
        <p><strong>ISBN:</strong> ${libro.isbn}</p>
        <p><strong>Título:</strong> ${libro.titulo}</p>
        <p><strong>Autor:</strong> ${libro.autor}</p>
        <p><strong>Estado:</strong>
            <span class="${estadoClase}">${estadoTexto}</span>
        </p>
    `;

}
async function buscarLibroTitulo() {
    const titulo = document.getElementById("nombreLibro").value.trim();

    const response = await fetch(`${API_URL}/titulo/${titulo}`);

    if (!titulo) {
        alert("Introduce un título para buscar");
        return;
    }

    if (titulo.length < 2) {
        alert("El título debe tener al menos 2 caracteres");
        return;
    }

    if (!response.ok) {
        alert("Libro no encontrado");
        return;
    }

    const libros = await response.json();
    const contenedor = document.getElementById("resultadoBusquedaTitulo");

    contenedor.innerHTML = "";

    libros.forEach(libro => {
        const estadoTexto = libro.disponible ? "Disponible" : "Prestado";
        const estadoClase = libro.disponible ? "disponible" : "prestado";

        contenedor.innerHTML += `
            <p><strong>ISBN:</strong> ${libro.isbn}</p>
            <p><strong>Título:</strong> ${libro.titulo}</p>
            <p><strong>Autor:</strong> ${libro.autor}</p>
            <p><strong>Estado:</strong>
                <span class="${estadoClase}">${estadoTexto}</span>
            </p>
            <hr>
        `;
    });
}


//Prestamos

async function prestarLibro() {
    const isbn = document.getElementById("prestamoIsbn").value;
    const usuarioIdInput = document.getElementById("prestamoUsuario").value.trim();


    if (!isbn || !usuarioIdInput) {
        alert("Introduce el ISBN y el ID del usuario");
        return;
    }

    if (isNaN(usuarioIdInput) || usuarioIdInput <= 0) {
        alert("El ID del usuario debe ser un número positivo");
        return;
    }

    const usuarioId = parseInt(document.getElementById("prestamoUsuario").value);

    if (isbn.length < 10 || isbn.length > 13) {
        alert("El ISBN debe tener entre 10 y 13 caracteres");
        return;
    }

    const response = await fetch(`${API_URL}/prestamos`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ isbn, usuarioId })
    });

    const result = await response.json();
    alert(JSON.stringify(result, null, 2));

    if (response.ok) {
        alert("Préstamo registrado correctamente");
        // Limpiar formulario
        document.getElementById("prestamoIsbn").value = "";
        document.getElementById("prestamoUsuario").value = "";
    } else {
        alert(result.mensaje || "Error al prestar el libro");
    }
}

async function devolverLibro() {
    const isbn = document.getElementById("devolucionIsbn").value;
    const usuarioIdInput = document.getElementById("devolucionUsuario").value.trim();

    if (!isbn || !usuarioIdInput) {
        alert("Introduce el ISBN y el ID del usuario");
        return;
    }

    if (isNaN(usuarioIdInput) || usuarioIdInput <= 0) {
        alert("El ID del usuario debe ser un número positivo");
        return;
    }

    const usuarioId = parseInt(document.getElementById("devolucionUsuario").value);

    if (isbn.length < 10 || isbn.length > 13) {
        alert("El ISBN debe tener entre 10 y 13 caracteres");
        return;
    }

    const response = await fetch(`${API_URL}/devoluciones`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ isbn, usuarioId })
    });

    const result = await response.json();
    alert(JSON.stringify(result, null, 2));

    if (response.ok) {
        alert("Devolución registrada correctamente");
        document.getElementById("devolucionIsbn").value = "";
        document.getElementById("devolucionUsuario").value = "";
    } else {
        alert(result.mensaje || "Error al devolver el libro");
    }
}

//Usuarios

async function listarUsuarios() {
    const response = await fetch(USUARIOS_API);

    const usuarios = await response.json();
    const tabla = document.getElementById("tablaUsuarios");

    if (!tabla) return;

    tabla.innerHTML = "";

    usuarios.forEach(usuario => {
        tabla.innerHTML += `
            <tr>
                <td>${usuario.id}</td>
                <td>${usuario.nombre}</td>
            </tr>
        `;
    });
}

async function register(event) {
    event.preventDefault();

    const nombre = document.getElementById("nombre").value.trim();
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;

    if (!nombre || !username || !password) {
        alert("Todos los campos son obligatorios");
        return;
    }

    if (nombre.length < 3) {
        alert("El nombre debe tener al menos 3 caracteres");
        return;
    }

    if (username.length < 4) {
        alert("El nombre de usuario debe tener al menos 4 caracteres");
        return;
    }

    if (username.includes(" ")) {
        alert("El nombre de usuario no puede contener espacios");
        return;
    }

    if (password.length < 6) {
        alert("La contraseña debe tener al menos 6 caracteres");
        return;
    }

    const response = await fetch("/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ nombre, username, password })
    });

    if (!response.ok) {
        alert("Error al registrar usuario. El nombre de usuario puede estar en uso.");
        return;
    }

    alert("Usuario creado correctamente");
    window.location.href = "login.html";
}

async function crearValoracion() {

    const titulo = document.getElementById("tituloValoracion").value;
    const puntuacion = parseInt(document.getElementById("puntuacion").value);
    const comentario = document.getElementById("comentario").value;
    const dueñoLibro = document.getElementById("origenLibro").value;

    const response = await fetch("/valoraciones", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            titulo,
            puntuacion,
            comentario,
            dueñoLibro
        })
    });

    if (!response.ok) {
        const error = await response.text();
        alert(error);
        return;
    }

    alert("Valoración guardada");
    cargarValoraciones();
}

async function cargarValoraciones() {

    const response = await fetch("/valoraciones/mias");
    const valoraciones = await response.json();

    const contenedor = document.getElementById("listaValoraciones");
    contenedor.innerHTML = "";

    valoraciones.forEach(v => {

        contenedor.innerHTML += `
        <div class="valoracion">
            <strong>${v.titulo}</strong>
            <p>Puntuación: ${v.puntuacion}/5</p>
            <p>${v.comentario ?? ""}</p>
            <button onclick="borrarValoracion('${v.titulo}')">Eliminar</button>
        </div>
        `;
    });
}

async function borrarValoracion(titulo) {

    await fetch(`/valoraciones?titulo=${encodeURIComponent(titulo)}`, {
        method: "DELETE"
    });

    cargarValoraciones();
}

async function buscarUsuario() {
    const username = document.getElementById("usernameInput").value.trim();

    if (!username) {
        alert("Introduce un nombre de usuario");
        return;
    }

    const response = await fetch(`/valoraciones/usuario/${username}`);

    if (!response.ok) {
        alert("Usuario no encontrado");
        return;
    }

    const valoraciones = await response.json();
    const contenedor = document.getElementById("valoracionesOtroUsuario");

    contenedor.innerHTML = `<h3>Valoraciones de ${username}</h3>`;

    if (valoraciones.length === 0) {
        contenedor.innerHTML += `<p>Este usuario no tiene valoraciones</p>`;
        return;
    }

    valoraciones.forEach(v => {
        contenedor.innerHTML += `
            <div class="valoracion">
                <strong>${v.titulo}</strong>
                <p>⭐ ${v.puntuacion}/5 - ${v.dueñoLibro}</p>
                <p>${v.comentario || ''}</p>
                <small>${new Date(v.fecha).toLocaleDateString()}</small>
            </div>
        `;
    });
}

async function cargarRanking() {
    try {
        const limit = 10;
        const response = await fetch(`/stats/top-titulos?limit=${limit}`);

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const ranking = await response.json();
        const contenedor = document.getElementById("ranking");

        if (!ranking || ranking.length === 0) {
            contenedor.innerHTML = "<p>No hay suficientes valoraciones aún</p>";
            return;
        }
        const html = ranking.map((item, index) => {

            const titulo = item.titulo ?? "(Sin título)";
            const media = Number(item.media ?? item.puntuacionMedia ?? 0);
            const total = Number(item.total ?? item.numeroValoraciones ?? 0);

            return `
                <div class="ranking-item">
                    <strong>#${index + 1} - ${titulo}</strong>
                    <p>⭐ ${media.toFixed(1)}/5 (${total} valoraciones)</p>
                </div>
            `;
        }).join("");

        contenedor.innerHTML = html;
    } catch (error) {
        alert("Error al cargar el ranking");
    }


}

async function cargarMisPrestamos(soloActivos) {
    try {
        const url = `/historial/mio?activos=${soloActivos ? "true" : "false"}&page=0&size=10&sort=fechaPrestamo,desc`;
        const response = await fetch(url);

        if (!response.ok) throw new Error(`HTTP ${response.status}`);

        const page = await response.json();
        const contenedor = document.getElementById("misPrestamos");

        if (!page.content || page.content.length === 0) {
            contenedor.innerHTML = "<p>No hay préstamos para mostrar.</p>";
            return;
        }

        const html = page.content.map(p => {
            const dev = p.fechaDevolucion ? new Date(p.fechaDevolucion).toLocaleString() : "—";
            const pre = p.fechaPrestamo ? new Date(p.fechaPrestamo).toLocaleString() : "—";
            return `
                <div class="prestamo-item" style="border:1px solid #ccc; padding:10px; margin:8px 0;">
                    <strong>${p.titulo}</strong>
                    <div>ISBN: ${p.isbn}</div>
                    <div>Prestado: ${pre}</div>
                    <div>Devuelto: ${dev}</div>
                    <div>Estado: ${p.activo ? "ACTIVO" : "DEVUELTO"}</div>
                </div>
            `;
        }).join("");

        contenedor.innerHTML = html;
    } catch (e) {
        console.error(e);
        alert("Error al cargar mis préstamos");
    }
}

async function crearReserva() {
    const titulo = document.getElementById("reservaTitulo").value.trim();

    if (!titulo) {
        alert("Introduce un título para reservar");
        return;
    }

    const response = await fetch("/reservas", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ titulo })
    });

    if (response.ok) {
        alert("Reserva creada correctamente");
        document.getElementById("reservaTitulo").value = "";
        cargarMisReservas();
    } else {
        const error = await response.json();
        alert(error.message || "Error al crear la reserva");
    }
}

async function cargarMisReservas() {
    const response = await fetch("/reservas/mias");

    if (!response.ok) {
        alert("Error al cargar las reservas");
        return;
    }

    const reservas = await response.json();
    const contenedor = document.getElementById("misReservas");
    contenedor.innerHTML = "";

    if (reservas.length === 0) {
        contenedor.innerHTML = "<p>No tienes reservas activas</p>";
        return;
    }

    reservas.forEach(r => {
        const fecha = new Date(r.fechaReserva).toLocaleDateString();
        const estadoClase = r.estado === "LISTA" ? "disponible" : "prestado";

        contenedor.innerHTML += `
            <div class="reserva-item">
                <strong>${r.libro.titulo}</strong>
                <p>ISBN: ${r.libro.isbn}</p>
                <p>Fecha reserva: ${fecha}</p>
                <p>Estado: <span class="${estadoClase}">${r.estado}</span></p>
                <button onclick="cancelarReserva(${r.id})">Cancelar reserva</button>
            </div>
            <hr>
        `;
    });
}

async function cancelarReserva(id) {
    const response = await fetch(`/reservas/${id}`, {
        method: "DELETE"
    });

    if (response.ok) {
        alert("Reserva cancelada");
        cargarMisReservas();
    } else {
        alert("Error al cancelar la reserva");
    }
}

async function cargarTodasReservas() {
    const response = await fetch("/reservas");

    if (!response.ok) {
        alert("Error al cargar las reservas");
        return;
    }

    const reservas = await response.json();
    const activas = reservas.filter(r => r.estado !== "CANCELADA"); // añade esta línea
    const contenedor = document.getElementById("todasReservas");
    contenedor.innerHTML = "";

    if (activas.length === 0) {
        contenedor.innerHTML = "<p>No hay reservas activas</p>";
        return;
    }

    activas.forEach(r => {  // cambia reservas por activas aquí
        const fecha = new Date(r.fechaReserva).toLocaleDateString();
        const estadoClase = r.estado === "LISTA" ? "disponible" : "prestado";

        contenedor.innerHTML += `
            <div class="reserva-item">
                <strong>${r.libro.titulo}</strong>
                <p>Usuario: ${r.usuario.username}</p>
                <p>ISBN: ${r.libro.isbn}</p>
                <p>Fecha reserva: ${fecha}</p>
                <p>Estado: <span class="${estadoClase}">${r.estado}</span></p>
                <button onclick="cancelarReservaBibliotecario(${r.id})">Cancelar reserva</button>
            </div>
            <hr>
        `;
    });
}

async function cancelarReservaBibliotecario(id) {
    const response = await fetch(`/reservas/admin/${id}`, {
        method: "DELETE"
    });

    if (response.ok) {
        alert("Reserva cancelada");
        cargarTodasReservas();
    } else {
        alert("Error al cancelar la reserva");
    }
}

async function cargarRecomendacion() {
    const div = document.getElementById("recomendacion");
    div.textContent = "Consultando con la IA...";
    try {
        const res = await fetch("/recomendaciones/mia");

        if (res.status === 401) {
            div.textContent = "No has iniciado sesión.";
            window.location.href = "/login.html";
            return;
        }

        if (!res.ok) {
            div.textContent = "No se han podido cargar recomendaciones ahora mismo.";
            return;
        }

        const data = await res.json();
        div.textContent = data.recomendacion || "No se han encontrado recomendaciones para tu historial.";
    } catch (error) {
        div.textContent = "No se han podido cargar recomendaciones ahora mismo.";
    }
}
