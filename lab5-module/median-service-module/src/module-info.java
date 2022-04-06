module median.service.module {
    requires api.module;
    provides pl.edu.pwr.lab5.api.AnalysisService with pl.edu.pwr.lab5.medianservice.Median;
}