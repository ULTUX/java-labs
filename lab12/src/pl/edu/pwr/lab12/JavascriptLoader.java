package pl.edu.pwr.lab12;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class JavascriptLoader {
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    private final Invocable invocable = (Invocable) engine;


    public JavascriptLoader(String fileName) throws FileNotFoundException, ScriptException, NoSuchMethodException {
        engine.eval(new FileReader(fileName));
        parseToArray((ScriptObjectMirror) invocable.invokeFunction("nextGeneration", (Object[]) new int[1][1]));
    }

    private int[][] parseToArray(ScriptObjectMirror objectMirror) {
        var array = new ArrayList<int[]>();
        objectMirror.forEach((s, o) -> {
            var arr = (ScriptObjectMirror) o;
            array.add(arr.values().stream().mapToInt(Integer.class::cast).toArray());
        });
        return array.toArray(new int[][]{});
    }

    public int[][] nextGeneration(int[][] state) throws ScriptException, NoSuchMethodException {
        var res = (ScriptObjectMirror) invocable.invokeFunction("nextGeneration", (Object) state);
        return parseToArray(res);
    }
}
