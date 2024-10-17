package com.algaworks.algafood.api.v1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticaController {

	@Autowired
	private VendaQueryService vendaQueryService;

	@Autowired
	private VendaReportService vendaReportService;
	
	@GetMapping(path = "/vendas-diaria", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filter) {
		return vendaQueryService.consultarVendasDiarias(filter);
	}

	@GetMapping(path = "/vendas-diaria", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> consultarVendasDiariasPDF(VendaDiariaFilter filter) {
		byte[] bytesPdf = vendaReportService.emitirVendasDiarias(filter);

		var headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).headers(headers).body(bytesPdf);
	}
}
