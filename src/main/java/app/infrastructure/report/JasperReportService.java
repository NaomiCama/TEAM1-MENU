package app.infrastructure.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DatabaseClient databaseClient;

    public Mono<byte[]> generateReport() {
        return fetchReportData().flatMap(data -> {
            try {
                Resource resource = resourceLoader.getResource("classpath:ReporteVenta1.jrxml");
                InputStream inputStream = resource.getInputStream();

                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
                return Mono.just(JasperExportManager.exportReportToPdf(jasperPrint));
            } catch (Exception e) {
                return Mono.error(e);
            }
        });
    }

    private Mono<List<Map<String, Object>>> fetchReportData() {
        return databaseClient.sql("select categoria, nombrecomida, precio from Vista_Menu_Comida")
                .fetch()
                .all()
                .collectList();
    }

    public Mono<byte[]> generateTransactionalReport() {
        return fetchTransactionalReportData().flatMap(data -> {
            try {
                Resource resource = resourceLoader.getResource("classpath:vista.jrxml");
                InputStream inputStream = resource.getInputStream();

                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
                return Mono.just(JasperExportManager.exportReportToPdf(jasperPrint));
            } catch (Exception e) {
                return Mono.error(e);
            }
        });
    }

    private Mono<List<Map<String, Object>>> fetchTransactionalReportData() {
        return databaseClient.sql("SELECT vista_menu_comida_historial.menuid,\r\n" + //
                        "\tcomidas.menuid,\r\n" + //
                        "\tvista_menu_comida_historial.nombremenu,\r\n" + //
                        "\tvista_menu_comida_historial.id,\r\n" + //
                        "\tvista_menu_comida_historial.nombrecomida,\r\n" + //
                        "\tvista_menu_comida_historial.precio,\r\n" + //
                        "\tvista_menu_comida_historial.categoria\r\n" + //
                        "FROM comidas\r\n" + //
                        "\tINNER JOIN menus ON \r\n" + //
                        "\t comidas.menuid = menus.menuid ,\r\n" + //
                        "\tvista_menu_comida_historial")
                .fetch()
                .all()
                .collectList();
    }


}