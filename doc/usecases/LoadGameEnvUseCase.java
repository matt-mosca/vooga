package voogasalad_duvallinthistogether;

import SaveGameEnvUseCase.MockAuthoringEngine.MockIOEngine;

/**
 * Demonstrates use-case of loading the current game env from front end
 * @author radithya
 *
 */
public class LoadGameEnvUseCase {

	private MockAuthoringEngine mockAuthoringEngine;
	
	public class MockAuthoringEngine implements AuthoringIOController {
		
		private MockIOEngine mockIOEngine;
		
		public class MockIOEngine implements AuthoringPersistence {

			/**
			 * constructor for mock io engine
			 */
			public MockIOEngine() {
			}
			
			/**
			 * Mock implementation
			 * @param gameName
			 * @param serializedRepresentation
			 */
			@Override
			public void loadGameSettings(String gameName, String serializedRepresentation) {
			}
			
		}
	
	private LoadGameEnvUseCase() {
		// TODO Auto-generated constructor stub
	}

}
