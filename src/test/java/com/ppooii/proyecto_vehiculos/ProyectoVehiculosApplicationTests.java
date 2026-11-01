package com.ppooii.proyecto_vehiculos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ppooii.proyecto_vehiculos.repository.TrayectoRepository;

@SpringBootTest
class ProyectoVehiculosApplicationTests {

	@Autowired
	private TrayectoRepository trayectoRepository;

	@Autowired
	private JdbcTemplate jdbc;

	@Test
	void contextLoads() {
	}

	@Test
	@EnabledIfEnvironmentVariable(named = "VERIFY_DATABASE_BOOTSTRAP", matches = "true")
	void excelProceduresAreCallableAndRejectInvalidRows() {
		double idCargue = -System.currentTimeMillis();
		try {
			trayectoRepository.CARGAR_DATOS_EXCEL("invalid", "invalid", "test", "test", "1", "test", idCargue);
			trayectoRepository.VALIDAR_DATOS_CARGUE(idCargue);
			trayectoRepository.PROCESAR_DATOS_EXCEL(idCargue);
			assertThat(trayectoRepository.consultarDetalleCargueMapeado(idCargue)).hasSize(1);
			assertThat(jdbc.queryForObject("SELECT estado FROM trayecto_tmp WHERE id_cargue = ?",
					String.class, idCargue)).isEqualTo("ERROR");
		} finally {
			jdbc.update("DELETE FROM trayecto_tmp WHERE id_cargue = ?", idCargue);
		}
	}

}
