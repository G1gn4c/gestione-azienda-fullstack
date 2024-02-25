package com.gignac.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gignac.dto.AziendaJasperDTO;
import com.gignac.mapper.DaAziendaAdAziendaJasperDTOMapper;
import com.gignac.repository.jpa.AziendaRepository;
import com.gignac.service.JasperService;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class JasperServiceImpl implements JasperService {
	
	private AziendaRepository aziendaRepository;
	
	private DaAziendaAdAziendaJasperDTOMapper daAziendaAdAziendaJasperDTOMapper;
	
	public JasperServiceImpl(
			AziendaRepository aziendaRepository,
			DaAziendaAdAziendaJasperDTOMapper daAziendaAdAziendaJasperDTOMapper
	) {
		super();
		this.aziendaRepository = aziendaRepository;
		this.daAziendaAdAziendaJasperDTOMapper = daAziendaAdAziendaJasperDTOMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public void create() throws Exception {
		List<AziendaJasperDTO> aziende = aziendaRepository.findAll()
				                                          .stream()
				                                          .map(azienda -> daAziendaAdAziendaJasperDTOMapper.apply(azienda))
				                                          .collect(Collectors.toList());
		InputStream reportStream = new ClassPathResource("jasper/aziende_con_relativi_impiegati.jrxml").getInputStream();
		JasperReport reportJasper = JasperCompileManager.compileReport(reportStream);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("aziende", aziende);
		JasperPrint reportJasperFillato = JasperFillManager.fillReport(reportJasper, parameters , new JREmptyDataSource());
		JasperExportManager.exportReportToPdfFile(reportJasperFillato, "src/main/resources/jasper/aziende_con_relativi_impiegati.pdf");
	}

}
