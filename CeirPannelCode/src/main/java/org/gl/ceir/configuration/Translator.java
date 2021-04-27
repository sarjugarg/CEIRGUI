package org.gl.ceir.configuration;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class Translator {

	@Autowired
	ReloadableResourceBundleMessageSource messageSource;

	@Autowired
	Translator(ReloadableResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public  String toLocale(String msgCode) {
		Locale locale = LocaleContextHolder.getLocale(); 
		return messageSource.getMessage(msgCode, null, locale);
	}
}