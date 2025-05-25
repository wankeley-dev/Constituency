package com.example.Learn.LearnOne.Controllers;

import com.example.Learn.LearnOne.Services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/report")
    public String viewReport(Model model) {
        // Default report with all data
        Map<String, Object> summaryReport = reportService.getSummaryReport(
                LocalDate.now().minusYears(1), LocalDate.now(), null);
        model.addAttribute("summaryReport", summaryReport);
        model.addAttribute("startDate", LocalDate.now().minusYears(1));
        model.addAttribute("endDate", LocalDate.now());
        model.addAttribute("ward", "");
        return "Report/report";
    }

    @PostMapping("/report/filter")
    public String filterReport(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "ward", required = false) String ward,
            Model model) {
        LocalDate start = startDate.isEmpty() ? LocalDate.now().minusYears(1) : LocalDate.parse(startDate);
        LocalDate end = endDate.isEmpty() ? LocalDate.now() : LocalDate.parse(endDate);
        Map<String, Object> summaryReport = reportService.getSummaryReport(start, end, ward);
        model.addAttribute("summaryReport", summaryReport);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("ward", ward != null ? ward : "");
        return "Report/report";
    }

    @GetMapping("/report/print")
    public String printReport(Model model) {
        // Default print view with all data
        Map<String, Object> summaryReport = reportService.getSummaryReport(
                LocalDate.now().minusYears(1), LocalDate.now(), null);
        model.addAttribute("summaryReport", summaryReport);
        model.addAttribute("startDate", LocalDate.now().minusYears(1));
        model.addAttribute("endDate", LocalDate.now());
        return "Report/reportPrint";
    }

    @PostMapping("/report/print/filter")
    public String printFilteredReport(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "ward", required = false) String ward,
            Model model) {
        LocalDate start = startDate.isEmpty() ? LocalDate.now().minusYears(1) : LocalDate.parse(startDate);
        LocalDate end = endDate.isEmpty() ? LocalDate.now() : LocalDate.parse(endDate);
        Map<String, Object> summaryReport = reportService.getSummaryReport(start, end, ward);
        model.addAttribute("summaryReport", summaryReport);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("ward", ward != null ? ward : "");
        return "Report/reportPrint";
    }

    @GetMapping("/report/filter")
    public String filterReport(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String pollingStation,
            Model model) {
        // Fetch data based on filters
        Map<String, Object> summaryReport = reportService.getSummaryReport(startDate, endDate, pollingStation);
        model.addAttribute("summaryReport", summaryReport);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("pollingStation", pollingStation);
        return "Report/report"; // Ensure this matches your Thymeleaf template name
    }
}
