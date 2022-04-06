package pl.edu.pwr.lab5.stddev;

import pl.edu.pwr.lab5.api.AnalysisException;
import pl.edu.pwr.lab5.api.AnalysisService;
import pl.edu.pwr.lab5.api.DataSet;

public class StandardDeviation implements AnalysisService {
    private DataSet result;
    @Override
    public void setOptions(String[] options) throws AnalysisException {

    }

    @Override
    public String getName() {
        return "Standard deviation";
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {
        result = new DataSet();
        result.setHeader(ds.getHeader());


        String[] resultData = new String[ds.getHeader().length];
        String[][] calcData = ds.getData();
        for (int i = 0; i < calcData[0].length; i++) {
            int[] colData = new int[calcData.length];
            for (int j = 0; j < calcData.length; j++) {
                colData[j] = Integer.parseInt(calcData[j][i]);
            }
            int avg = 0;
            for (int datum : colData) {
                avg += datum;
            }
            avg /= colData.length;
            double stdDev = 0;
            for (int colDatum : colData) {
                stdDev += Math.pow(colDatum - avg, 2);
            }
            stdDev /= colData.length;
            stdDev = Math.sqrt(stdDev);
            resultData[i] = String.valueOf(stdDev);
        }
        result.setData(new String[][]{resultData});
    }

    @Override
    public DataSet retrieve(boolean clear) throws AnalysisException {
        return result;
    }
}
