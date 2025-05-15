package com.senai.ecommerce.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.ecommerce.dto.RelatorioPedidoDTO;
import com.senai.ecommerce.entities.Pedido;
import com.senai.ecommerce.repositories.PedidoRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class RelatorioService {
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	public void gerarRelatorio(String caminho) throws JRException {
		List<Pedido> pedidos = pedidoRepository.findAll();
		
		List<RelatorioPedidoDTO> escanor = pedidos.stream()
				.map(RelatorioPedidoDTO::new).collect(Collectors.toList());
				 //convertendo minha lista de pedidos para lista de relatoriospedidosDTO
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(escanor);
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("titulo", "Relatórios de Pedidos"); //criei o titulo do meu relatorio
		
		//aqui ele vai puxar o relatorio, definino dentro da pasta relatorios
		JasperReport jasperReport = JasperCompileManager.compileReport(
				getClass().getResourceAsStream("/relatorios/relatorio_pedidos.jrxml"));
		
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
		//aqui ele vai printar
		
		JasperExportManager.exportReportToPdfFile(jasperPrint, caminho);//gera em PDF
		
	}

}
