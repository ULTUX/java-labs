package pl.edu.pwr.lab5.client;

import pl.edu.pwr.lab5.api.AnalysisService;
import pl.edu.pwr.lab5.client.ui.MainFrame;

import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) {
        ServiceLoader<AnalysisService> analysisServices = ServiceLoader.load(AnalysisService.class);
        analysisServices.stream().forEach(analysisServiceProvider -> {
            System.out.println(analysisServiceProvider.get().getName());
        });
        new MainFrame();
    }
}
