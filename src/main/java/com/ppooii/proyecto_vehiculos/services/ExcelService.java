package com.ppooii.proyecto_vehiculos.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.RowId;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ppooii.proyecto_vehiculos.repository.TrayectoRepository;
import com.ppooii.proyecto_vehiculos.services.interfaces.IExcelService;

import io.jsonwebtoken.lang.Collections;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


@Service
public class ExcelService implements IExcelService {
	
	@Autowired
	@Qualifier("ITrayectoRepo")
	private TrayectoRepository trayectoRepository;
	
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(TrayectoService.class);
	
	public void processExcel(MultipartFile file) {
		try (InputStream inputStream = file.getInputStream()) {
			Workbook workbook = WorkbookFactory.create(inputStream);
			
			Sheet sheet = workbook.getSheetAt(0);
			LocalDateTime now = LocalDateTime.now();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
			String formattedDateTime = now.format(formatter);
			double idCargue = Double.parseDouble(formattedDateTime);
			
			for (Row row : sheet) {
				if (row.getRowNum()> 0) {
					String personaId = null;
					String vehiculoId = null;
					String codigoRuta = null;
					String ubicacion = null;
					String ordenParada = null;
					String login = null;
					
					for (Cell cell : row) {
						switch (cell.getColumnIndex()) {
							case 0: personaId = String.valueOf(cell.getNumericCellValue());
									String[] ddd = personaId.split(".0");
									personaId = ddd[0].toString();
									break;
							case 1: vehiculoId = String.valueOf(cell.getNumericCellValue());
									String[] aaa = vehiculoId.split(".0");
									vehiculoId = aaa[0].toString();
									break;
							case 2: codigoRuta = String.valueOf(cell.getNumericCellValue());
									String[] bbb = codigoRuta.split(".0");	
									codigoRuta = bbb[0].toString(); 
									break;
							case 3: ubicacion = cell.getStringCellValue(); break;
							case 4: ordenParada = String.valueOf(cell.getNumericCellValue());
									String[] ccc = ordenParada.split(".0");
									ordenParada = ccc[0].toString(); 
									break;
							case 5: login = cell.getStringCellValue(); break;
							default: break;
						}
					}
					System.out.println("Datos: " + personaId + " " + vehiculoId + " " + codigoRuta + " " + ubicacion + " " + ordenParada + " " + login);
					if (this.cargarDatosExcel(personaId, vehiculoId, codigoRuta, ubicacion, ordenParada, login, idCargue)) {
						System.out.println("Cargue inicial con exito");	
					}
				}
				
			}
			workbook.close();
			if (this.validarDatosCargue(idCargue)) {
				System.out.println("Validacion con exito");
				if (this.procesarDatosExcel(idCargue)) 
					System.out.println("Procesamiento con exito");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	

	@Override
	public boolean cargarDatosExcel(String personaId, String vehiculoId, String codigoRuta, String ubicacion,
			String ordenParada, String login, Double idCargue) {
		try {
			this.trayectoRepository.CARGAR_DATOS_EXCEL(personaId, vehiculoId, codigoRuta, ubicacion, ordenParada, login, idCargue);
			logger.info("Cargue inicial con exito");
			return true;
		} catch (Exception e) {
			logger.error("Error: Cargue inicial no fue posible", e);
			return false;
		}
	}

	@Override
	public boolean validarDatosCargue(Double idCargue) {
		try {
			this.trayectoRepository.VALIDAR_DATOS_CARGUE(idCargue);
			logger.info("Validacion con exito");
			return true;
		} catch (Exception e) {
			logger.error("Error: Validacion no fue posible", e);
			return false;
		}
	}

	@Override
	public boolean procesarDatosExcel(Double idCargue) {
		try {
			this.trayectoRepository.PROCESAR_DATOS_EXCEL(idCargue);
			logger.info("Procesamiento con exito");
			return true;
		} catch (Exception e) {
			logger.error("Error: Procesamiento no fue posible", e);
			return false;
		}
	}
	
	public List<Map<String, Object>> consultarDetalleCargue(Double idCargue) {
        try {
            List<Map<String, Object>> resultados = this.trayectoRepository.consultarDetalleCargueMapeado(idCargue);
            
            return resultados; 
            
        } catch (Exception e) {
            logger.error("Error consultando el detalle del cargue para ID {}.", idCargue, e);
            
            return new ArrayList<Map<String, Object>>();
        }
    }
	


}
