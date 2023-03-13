
### Information

- Java: **11 Amazon**
- DB: **Postgresql**
- Test: **TestNG** ( Pass 36 / 36 )
- Docs: **Swagger**

>#### Start db ( or change properties default port : 37001)
>   ```sh
>   docker-compose up -d
>   ```
> >Default username : admin, password = 123456
> 

>#### SwaggerUI URL ( Default url )
>   ```html
>   http://localhost:8080/swagger-ui/index.html
>   ```

>#### APIDocs URL ( Default url )
>   ```html
>   http://localhost:8080/api-docs
>   ```

>#### File Storage Path ( application.properties )
>   ```html
>   spring.servlet.multipart.location=${java.io.tmpdir}
>   file-record.path=${java.io.tmpdir}/file-records
>   ```