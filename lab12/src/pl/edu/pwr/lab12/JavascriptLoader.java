package pl.edu.pwr.lab12;


import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.js.scriptengine.GraalJSEngineFactory;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import com.oracle.truffle.regex.util.TruffleReadOnlyMap;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavascriptLoader {
    private final ScriptEngine engine = GraalJSScriptEngine.create(
            Engine.newBuilder()
//                    .option("engine.WarnInterpreterOnly", "false")
                    .build(),
            Context.newBuilder("js")
                    .allowHostAccess(HostAccess.ALL)
                    .allowHostClassLookup(s -> true)
//                    .option("engine.WarnInterpreterOnly", "false")
                    .option("js.ecmascript-version", "2020"));
    private final Invocable invocable = (Invocable) engine;


    public JavascriptLoader(String fileName) throws FileNotFoundException, ScriptException, NoSuchMethodException {
        engine.eval(new FileReader(fileName));
        parseToArray((List<List<Integer>>) invocable.invokeFunction("nextGeneration", (Object[]) new int[1][1]));
    }

    private int[][] parseToArray(List<List<Integer>> objectMirror) {
        var array = new ArrayList<int[]>();
        objectMirror.forEach((a) -> {
            array.add(a.stream().mapToInt(Integer.class::cast).toArray());
        });
        return array.toArray(new int[][]{});
    }

    public int[][] nextGeneration(int[][] state) throws ScriptException, NoSuchMethodException {
        var res = (List<List<Integer>>) invocable.invokeFunction("nextGeneration", (Object) state);
        return parseToArray(res);
    }
}
