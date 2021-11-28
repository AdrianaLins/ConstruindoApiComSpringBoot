package br.com.alura.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Classe que vai fazer tratamento de erros
@RestControllerAdvice
public class ErroDeValidacoHandler {
	
	@Autowired
	private MessageSource messageSource; //Classe do spring que ajuda a pegar mensagens de erro
	
	//Anotação que informa que esse método deve ser chamado quando houver alguma excessão dentro de algum controller
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) //devolver o status 400
	@ExceptionHandler(MethodArgumentNotValidException.class) //erro de formulário usando beanValidation
	public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {
		List<ErroDeFormularioDto> dto = new ArrayList<>();
		
		List<FieldError> fieldErros = exception.getBindingResult().getFieldErrors();
		fieldErros.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
			dto.add(erro);
		});
		
		
		return dto;
	}
	
	
	
	

}
