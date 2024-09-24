package com.algaworks.algafood.infrastructure.service.report;

import java.util.Locale;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PDFVendaReportService implements VendaReportService{

	@Autowired
	private VendaQueryService vendaQueryService;
	
	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filter) {
		try {
			var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");
			
			var parametros = new HashedMap<String, Object>();
			parametros.put("REPORT_LOCALE", new Locale("pt","BR"));
			
			var vendasDiarias = vendaQueryService.consultarVendasDiarias(filter);
			var dataSource = new JRBeanCollectionDataSource(vendasDiarias);
			
			var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);
			
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
		}
		
	}

}
