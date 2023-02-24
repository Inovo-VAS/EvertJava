package inovo.automated.work;

import java.util.HashMap;

public class AutomatedWorkerStep implements Runnable{
	private AutomatedWorker _automatedWorker=null;
	private HashMap<String, String> _stepProperties=new HashMap<String,String>();
	
	public HashMap<String,String> stepProperties(){
		return this._stepProperties;
	}
	
	public AutomatedWorkerStep(AutomatedWorker automatedWorker,HashMap<String,String> stepProperties){
		this._automatedWorker=automatedWorker;
		this._stepProperties.putAll(stepProperties);
	}
	@Override
	public void run() {
		this.performAutomationStep();
	}
	
	public void performAutomationStep() {
	}
}
