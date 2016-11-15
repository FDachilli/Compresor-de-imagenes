import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import Herramientas.ArbolHuffman;
import Herramientas.Calculos;
import Herramientas.Color;
import Herramientas.Compresor;
import Herramientas.Descompresor;

public class TeoriaTp4 {
/* Se modifico que solo los dos ultimos de la matriz si esta es impar, compare con el siguiente. */

	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<Color> arreglo = new ArrayList<>();
		ArbolHuffman arbol_H = new ArbolHuffman();
		Calculos calculo = new Calculos();
		Descompresor desc = new Descompresor ();
		Compresor comp = new Compresor ();

		//Obtengo las probabilidades de los colores, sin memoria
		calculo.calcular_probabilidad (arreglo);
		
		//Ordeno los colores por probabilidades antes de insertar en el arbol.
		Collections.sort(arreglo);
		Collections.reverse(arreglo);
		arbol_H.insertar(arreglo);
		arbol_H.set_codigos();
		arbol_H.set_longitudes();

		
		//Muestro el color con su respectivo codigo y longitud
		for(int i = 0; i<arreglo.size(); i++){
			System.out.println(arreglo.get(i).getR() +"  Codigo: " +arreglo.get(i).getCodigo() +"  Longitud:" +arreglo.get(i).getLongitud());
		}
		
		comp.generarCodificacion(arreglo);
		desc.descomprimirCodificacion();
		comp.comprimirRunLeght();
		desc.descomprimirRunLenght();
	}
}
