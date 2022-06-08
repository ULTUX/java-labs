package pl.edu.pwr.lab14;

import java.util.Arrays;
import java.util.List;

public class LightSequence {
    List<Integer> sequence;
    int sequencePos = 0;

    public LightSequence(Integer[] sequence) {
        this.sequence = Arrays.asList(sequence);
    }

    public int getNextInSequence() {
        if (sequencePos == sequence.size()) sequencePos = 0;
        return sequence.get(sequencePos++);
    }

    public int getSequencePos() {
        return sequencePos;
    }

    public void resetSequence() {
        this.sequencePos = 0;
    }
}
