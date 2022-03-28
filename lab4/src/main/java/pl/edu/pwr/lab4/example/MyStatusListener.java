package pl.edu.pwr.lab4.example;

import pl.edu.pwr.lab4.processing.Status;
import pl.edu.pwr.lab4.processing.StatusListener;

public class MyStatusListener implements StatusListener {
    // listener jest znany podczas kompilacji projektu
	// mo?na wi?c wi?za? si? w nim z interfejsem u?ytkownika
	@Override
	public void statusChanged(Status s) {
			System.out.println("Progress:"+s.getProgress()+" TaskId:" +s.getTaskId());
	}

}
