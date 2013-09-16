import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class CodigoEnsamblador {
	
	public static FileWriter m_fwFitxer;
	public static BufferedWriter m_fitxer;

	public CodigoEnsamblador(String nomFitxer)
	{
		try {
			m_fwFitxer = new FileWriter(nomFitxer);
			m_fitxer   = new BufferedWriter(m_fwFitxer);
		} catch(IOException ioe) {
			ioe.printStackTrace(System.err);
		}
	}

	public static void tanca()
	{
		try {
			m_fitxer.close();
			m_fwFitxer.close();
		} catch(IOException ioe) {
			ioe.printStackTrace(System.err);
		}
	}
	
	public static void gc(String s) {
		
		try {
			m_fitxer.write(s);
			m_fitxer.newLine();
			m_fitxer.flush();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
        
	}
}
