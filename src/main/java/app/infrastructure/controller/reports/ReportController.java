package app.infrastructure.controller.reports;

import app.infrastructure.report.JasperReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reportes")
public class ReportController {

    @Autowired
    private JasperReportService jasperReportService;

    @GetMapping("/report")
    public Mono<ResponseEntity<byte[]>> generateReport() {
        return jasperReportService.generateReport()
                .map(report -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf");
                    headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
                    return new ResponseEntity<>(report, headers, HttpStatus.OK);
                })
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @GetMapping("/transactionalReport")
    public Mono<ResponseEntity<byte[]>> generateTransactionalReport() {
        return jasperReportService.generateTransactionalReport()
                .map(report -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=transactionalReport.pdf");
                    headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
                    return new ResponseEntity<>(report, headers, HttpStatus.OK);
                })
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)));
    }
}