package Integration;
import gov.nih.nlm.uts.webservice.UtsFault_Exception;

public class Test {

	public static void main(String[] args) throws UtsFault_Exception {
		// TODO Auto-generated method stub

		String test = TranscodageCMI10_CIMO.findConcepts("C25.3", "code");
		System.out.println(test);
		
		String test2 = TranscodageCMI10_CIMO.getConceptAtoms("C0153461");
		System.out.println(test2);

	}

}
