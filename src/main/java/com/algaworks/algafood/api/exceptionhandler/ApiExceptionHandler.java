package com.algaworks.algafood.api.exceptionhandler;

import java.util.stream.Collectors;

import org.flywaydb.core.internal.util.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
                                                                    HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

        String detail = String.format("O recurso %s que você quer acessar e inexistente.",
                ex.getResourcePath());

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpHeaders headers,
                                                                             HttpStatusCode status, WebRequest request) {


        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                e.getName(), e.getValue(), e.getRequiredType().getSimpleName());

        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(e, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        System.out.println(rootCause.getCause());
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException e, HttpHeaders headers,
                                                                HttpStatusCode status, WebRequest request) {

        String path = e.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
        String detail = String.format(
                "A propriedade '%s' recebeu o valor '%s', que é do tipo inválido. "
                        + "Corrija e informe um valor compatível com o tipo %s",
                path, e.getValue(), e.getTargetType().getSimpleName());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;

        Problem problem = createProblemBuilder(status, problemType, detail).build();
        ;

        return handleExceptionInternal(e, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException e, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        String path = e.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
        String detail = null;

        if (rootCause instanceof IgnoredPropertyException) {
            detail = String.format(
                    "A propriedade '%s' não está habilitada neste momento. ", path,
                    e.getKnownPropertyIds());
        } else if (rootCause instanceof UnrecognizedPropertyException) {
            detail = String.format(
                    "A propriedade '%s' não existe para o corpo da mensagem. ", path, e.getKnownPropertyIds());
        }


        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(e, problem, headers, status, request);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e,
                                                                  WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = e.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException e, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.ERRO_NEGOCIO;
        String detail = e.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail).build();
        return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = e.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail).build();
        return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception e, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
        String detail = "Ocorreu um erro interno inesperado no sistema. \"\n"
                + "            + \"Tente novamente e se o problema persistir, entre em contato \"\n"
                + "            + \"com o administrador do sistema.";
        e.printStackTrace();
        Problem problem = createProblemBuilder(status, problemType, detail).build();
        return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        if (body == null) {
            body = Problem.builder().title(statusCode.toString()).status(statusCode.value()).build();
        } else if (body instanceof String) {
            body = Problem.builder().title(statusCode.toString()).status(statusCode.value()).build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status, ProblemType problemType, String detail) {

        return Problem.builder().status(status.value()).type(problemType.getUri()).title(problemType.getTitle())
                .detail(detail);
    }

}
