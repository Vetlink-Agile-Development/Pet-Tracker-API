# Password Reset Feature - Configuration Guide

## 📧 Email Configuration

### Using Gmail

Para usar Gmail como servicio de correo, necesitas configurar los siguientes pasos:

#### 1. Habilitar "Contraseñas de aplicación" en Gmail

1. Ve a tu cuenta de Google: https://myaccount.google.com/
2. En "Seguridad", habilita la verificación en 2 pasos si no la tienes activada
3. Una vez habilitada la verificación en 2 pasos, busca "Contraseñas de aplicaciones"
4. Genera una nueva contraseña de aplicación para "Correo"
5. Copia la contraseña generada (16 caracteres)

#### 2. Actualizar application.properties

Reemplaza los valores en `src/main/resources/application.properties`:

```properties
spring.mail.username=tu-email@gmail.com
spring.mail.password=tu-contraseña-de-aplicacion-generada
```

### Usando otros proveedores de email

#### SendGrid
```properties
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=tu-sendgrid-api-key
```

#### Outlook/Hotmail
```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
spring.mail.username=tu-email@outlook.com
spring.mail.password=tu-contraseña
```

#### AWS SES
```properties
spring.mail.host=email-smtp.us-east-1.amazonaws.com
spring.mail.port=587
spring.mail.username=tu-aws-smtp-username
spring.mail.password=tu-aws-smtp-password
```

## 🌐 Frontend URL Configuration

Actualiza la URL de tu frontend en `application.properties`:

```properties
app.frontend.url=https://tu-dominio-frontend.com
```

Esta URL se usa para generar el link de reseteo de contraseña en el email.

## 📋 API Endpoints

### 1. Solicitar reseteo de contraseña

**POST** `/api/v1/password-reset/request`

**Body:**
```json
{
  "email": "user@example.com"
}
```

**Response:** 200 OK
```json
{
  "message": "If the email exists, a password reset link has been sent."
}
```

**Nota:** Por seguridad, siempre retorna el mismo mensaje sin importar si el email existe o no.

---

### 2. Resetear contraseña

**POST** `/api/v1/password-reset/reset`

**Body:**
```json
{
  "token": "token-generado-del-email",
  "newPassword": "nuevaContraseñaSegura123!"
}
```

**Response:** 200 OK
```json
{
  "message": "Password has been successfully reset."
}
```

**Response:** 400 Bad Request
```json
{
  "message": "Invalid or expired reset token"
}
```

## 🔒 Características de Seguridad

1. **Tokens únicos:** Cada solicitud genera un token UUID único
2. **Expiración:** Los tokens expiran en 30 minutos
3. **Un solo uso:** Una vez usado, el token se invalida
4. **Protección de información:** No revela si un email existe en el sistema
5. **Invalidación automática:** Los tokens anteriores se eliminan al solicitar uno nuevo
6. **Contraseñas hasheadas:** Las contraseñas se almacenan con BCrypt

## 🗄️ Base de Datos

La tabla `password_reset_tokens` se crea automáticamente con Hibernate DDL.

Estructura:
- `id`: Identificador único
- `token`: Token UUID único
- `user_id`: Referencia al usuario
- `expiry_date`: Fecha de expiración
- `used`: Estado del token (usado/no usado)
- `created_at`: Fecha de creación
- `updated_at`: Fecha de última actualización

## ✅ Testing

### Para desarrollo/testing (sin email real)

Si quieres probar sin configurar un servicio de email real, puedes:

1. **Ver logs:** El token se imprime en la consola si falla el envío del email
2. **Usar Mailtrap:** Servicio gratuito para testing de emails
   ```properties
   spring.mail.host=smtp.mailtrap.io
   spring.mail.port=2525
   spring.mail.username=tu-mailtrap-username
   spring.mail.password=tu-mailtrap-password
   ```

### Testing manual con Swagger

1. Inicia la aplicación
2. Ve a: http://localhost:8080/swagger-ui.html
3. Busca "Password Reset" en los endpoints
4. Prueba los endpoints directamente desde Swagger

## 🚀 Flujo completo

1. Usuario solicita reseteo → `POST /api/v1/password-reset/request`
2. Sistema busca el email en la base de datos
3. Si existe, genera un token y lo guarda
4. Envía email con el link: `{frontend-url}/reset-password?token={token}`
5. Usuario hace clic en el link del email
6. Frontend llama a → `POST /api/v1/password-reset/reset`
7. Sistema valida el token y actualiza la contraseña
8. Token se marca como usado

## 📝 Mejoras futuras sugeridas

- [ ] Rate limiting para prevenir spam
- [ ] Notificación por email después del cambio de contraseña
- [ ] Historial de cambios de contraseña
- [ ] Tarea programada para limpiar tokens expirados
- [ ] Templates HTML para emails más profesionales
- [ ] Opción de validar token antes de resetear
- [ ] Bloqueo temporal después de X intentos fallidos

## 🛠️ Troubleshooting

### Email no se envía
- Verifica las credenciales en `application.properties`
- Revisa los logs de la aplicación
- Asegúrate de tener internet
- Verifica que el puerto SMTP no esté bloqueado

### Token inválido o expirado
- Los tokens expiran en 30 minutos
- Los tokens solo se pueden usar una vez
- Verifica que el token no tenga espacios adicionales

### Usuario no recibe el email
- Revisa la carpeta de spam
- Verifica que el email del usuario esté correcto en la base de datos
- Revisa los logs del servidor
