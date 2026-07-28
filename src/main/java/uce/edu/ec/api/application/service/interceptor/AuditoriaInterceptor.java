package uce.edu.ec.api.application.service.interceptor;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.transaction.Transactional;
import uce.edu.ec.api.application.service.AuditoriaService;

@Auditar
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class AuditoriaInterceptor {

    @Inject
    AuditoriaService auditoriaService;

    @AroundInvoke
    public Object interceptar(InvocationContext ctx) throws Exception {

        Object resultado = ctx.proceed();

        String metodo = ctx.getMethod().getName();
        String entidad = ctx.getTarget().getClass().getSimpleName();
        auditoriaService.guardarAuditoria(metodo, entidad);
        return resultado;
    }
}