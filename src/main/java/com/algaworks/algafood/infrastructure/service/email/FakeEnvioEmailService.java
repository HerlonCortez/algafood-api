package com.algaworks.algafood.infrastructure.service.email;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.service.EnvioEmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

//@Service
@Slf4j
public class FakeEnvioEmailService implements EnvioEmailService{

	@Autowired
	private EmailProperties emailProperties;
	
	@Autowired
	private Configuration freemarkerConfig;
	
	@Override
	public void enviar(Mensagem mensagem) {
		int i=1;
	    Set<String> destinatarios = mensagem.getDestinatarios();
		String corpo = processarTemplate(mensagem);
		log.info("************ DISPARO DE EMAIL ************ ");
		log.info("Remetente...........: " + emailProperties.getRemetente());
		log.info("Destinatário(s).....: ");
		for (String destinatario : destinatarios) {
			log.info(String.valueOf(i++)+". " + destinatario );
		}
		log.info("Assunto.............: " + mensagem.getAssunto());	 
		log.info("Corpo...............: " + corpo);
	}
	
	private String processarTemplate(Mensagem mensagem) {
		try {
			Template template = freemarkerConfig.getTemplate(mensagem.getCorpo());
			
			return FreeMarkerTemplateUtils.processTemplateIntoString(
					template, mensagem.getVariaveis());
		} catch (Exception e) {
			throw new EmailException("Não foi possível montar o template do e-mail", e);
		}
	}
}
