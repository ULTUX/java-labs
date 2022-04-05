package pl.edu.pwr.lab5.medianservice;

import pl.edu.pwr.lab5.api.AnalysisException;
import pl.edu.pwr.lab5.api.AnalysisService;
import pl.edu.pwr.lab5.api.DataSet;

import java.util.Arrays;

public class Median implements AnalysisService {
    private DataSet result;
    @Override
    public void setOptions(String[] options) throws AnalysisException {
    }

    @Override
    public String getName() {
        return "Median calculations";
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {
        result = new DataSet();
        String[] resultData = new String[ds.getHeader().length];
        String[][] calcData = ds.getData();
        for (int i = 0; i < calcData.length; i++) {
            int[] colData = new int[calcData[i].length];
            for (int j = 0; j < calcData[i].length; j++) {
                colData[j] = Integer.parseInt(calcData[i][j]);
            }
            Arrays.sort(colData);
            int median = 0;
            if (colData.length % 2 == 0) median = (colData[colData.length/2] + colData[colData.length/2-1]) / 2;
            else median = colData[colData.length/2];

            resultData[i] = String.valueOf(median);
        }


        result.setData(new String[][]{resultData});
    }

    @Override
    public DataSet retrieve(boolean clear) throws AnalysisException {
        return result;
    }
}
