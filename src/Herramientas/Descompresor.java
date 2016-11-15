package Herramientas;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class Descompresor {
	
	public static void generarBits(char c, StringBuffer codigo){
		
		char mascara = 1<<15;
		for(int i=0; i < 16; i++){
			if ((c & mascara)==32768)
				codigo.append("1");
			else 
				codigo.append("0");
			c = (char) (c<<1);
		}
	}

	public static Integer matchSimbolo (byte codigos, ArrayList<Color> arreglo)
	{
		byte buffer=0;
		
		for(int i=0; i < arreglo.size(); i++){
			String aux = arreglo.get(i).getCodigo();
			int max = aux.length()-1;
			int m = 0;
			buffer=0;
			while(m<=max){
				buffer = (byte) (buffer<<1);
				if(aux.charAt(m)=='1'){
					buffer =(byte) (buffer | 1);
				}
				m++;
    	    }
			
			if(buffer == codigos){
				return arreglo.get(i).getR();
			}
		}
		return -1;
	}	
	

	public static Integer devolverEntero (StringBuffer codigos, char c)//StringBuffer nos permite eliminar del string
	{
		int k=1;
		boolean encontrado=false;
		int a=-1;
		while (k <= codigos.length() && !encontrado){
			 String subst = codigos.substring(0,k);
			 if (subst.charAt(k-1) == c){
				 encontrado=true;
				 String aux= subst.substring(0, subst.length()-1);//elimino el 0 del substring
				 a=Integer.parseInt(aux);	
				 codigos.delete(0, k);
			 }
			 else
				 k++;
		}
		return a;
	}
	
	public void guardarImagen(BufferedImage imagen, String nombre){
		try {
			ImageIO.write(imagen, "bmp", new File(nombre+".bmp"));
		} catch (IOException e) {
			System.out.println("Error de escritura");
		}
	}

	public static short getBit(short cod, int posicion)
	{
	   return (short) (cod >> posicion);
	}
	
	public static int get_int(FileInputStream fr) throws IOException{
		int j=0;
		j= (j | fr.read());
		j=j<<8;
		j= (j | fr.read());
		j=j<<8;
		j= (j | fr.read());
		j=j<<8;
		j= (j | fr.read());
		return j;
	}
	
	public void descomprimirCodificacion () throws IOException{
		try
		{
			FileInputStream fr = new FileInputStream("comprimidoHuffman");
			byte byteBuffer; 
			byte aux = 0;
			byte aux2 = 0;
			int n = 0;
			int color = -1;
			
			//Leo el encabezado
			
			int height = get_int(fr);
			int width = get_int(fr);
			ArrayList<Color> arreglo = new ArrayList<>();
			ArbolHuffman arbol_D = new ArbolHuffman();
			int cant = get_int(fr);
			int aux_encabezado;
			ArrayList<Integer> repeticiones= new ArrayList<>();
			int total = 0;
			
			//Obtengo los colores y la cantidad de repeticiones
			for(int i=0; i<=cant; i++){
				Color color_aux = new Color();
				aux_encabezado = get_int(fr);
				repeticiones.add(aux_encabezado);
				aux_encabezado = get_int(fr);
				color_aux.setRGB(aux_encabezado, aux_encabezado, aux_encabezado);
				arreglo.add(color_aux);
			}
			
			//Obtengo el total de pixeles
			for(int i=0; i<repeticiones.size(); i++){
				total+=repeticiones.get(i);
			}
			
			//Obtengo la probabilidad de cada color
			for(int i=0; i<=cant; i++){
				arreglo.get(i).setProbabilidad((double)repeticiones.get(i)/total);
			}
			
			//Genero el arbol
			//Ordeno los colores por probabilidades antes de insertar en el arbol.
			Collections.sort(arreglo);
			Collections.reverse(arreglo);
			arbol_D.insertar(arreglo);
			arbol_D.set_codigos();
			
			for(int i=0; i<cant; i++){
				System.out.println(arreglo.get(i).getProbabilidad());
			}
			
			
			//Comienzo la descompresion

			BufferedImage imagen = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
			
			byteBuffer = (byte) (fr.read());
			aux = (byte) (byteBuffer & 128);		//128(decimal) == 10000000(binario)
			aux = (byte) (aux>>7);					
			byteBuffer = (byte) (byteBuffer<<1);	//Avanzo en el primer byte del archivo.
			
			color = matchSimbolo(aux, arreglo);
			n++;
			
			int j=0;
			int i=0;
			boolean br = true;
			while(aux!=-1 && br){		// No llegue al EOF.			
				while(color==-1 && n<8){	// No hay match y no se recorrio todo el byte.
					aux2 = (byte) (byteBuffer & -128);
					if(aux2 == -128)
						aux = (byte) (aux | 1);
					color = matchSimbolo(aux, arreglo);
					n++;
					byteBuffer = (byte) (byteBuffer<<1);
					aux = (byte) (aux<<1);
				}
				if(n==8){
					n=0;
					byteBuffer = 0;
					byteBuffer = (byte) (fr.read());
					//System.out.println((byte) (byteBuffer>>15));
				}
				if(color!=-1){	
					java.awt.Color rgb= new java.awt.Color(color,color,color);
					imagen.setRGB(j, i, rgb.getRGB());
					i++;
					if(i==height){	
						j++;
						i=0;
					}
					color = -1;
					aux = 0;
				}
				if(j==width)	//Se analizaron todos los pixeles. Llegue al EOF.
					br=false;
		}
		guardarImagen(imagen, "descomprimidaHuffman");
		fr.close();
		}
		catch (Exception e)
		{
		System.out.println(e.getMessage());
		   //System.out.println("Error de lectura del fichero");
		}
	}
	
	public void descomprimirRunLenght () throws IOException{
		FileReader fr = new FileReader("comprimidoRL.txt");
		BufferedReader archivo = new BufferedReader(fr);
		String cadena="";
		//Decodifico encabezado
		cadena=archivo.readLine();
		StringBuffer encabezado= new StringBuffer ();
		encabezado.append(cadena);
		int altura =devolverEntero (encabezado, '/');
		int ancho =devolverEntero (encabezado, '/');
		BufferedImage imagen = new BufferedImage(ancho, altura, BufferedImage.TYPE_BYTE_GRAY);
		StringBuffer codificacion= new StringBuffer ();
		while ((cadena = archivo.readLine())!=null)
			//Paso la codificacion a un string buffer para que no se corte con los saltos de linea y porque permite eliminar
			codificacion.append(cadena);
		int i=0;
		int j=0;
		int veces = 0;
		int simbolo=0;
			while (codificacion.length()>0){
				 simbolo = devolverEntero (codificacion, '-');
				 java.awt.Color rgb= new java.awt.Color(simbolo,simbolo,simbolo);
				 veces = devolverEntero (codificacion, '.' ); 
						 
						 for (int h=1;h<=veces;h++){
							 
								imagen.setRGB(i, j, rgb.getRGB());
								i++;
								if(i==ancho){
									j++;
									i=0;
								}
							}
							if(j==altura){
								archivo.close();
								}
					}
			archivo.close();
			guardarImagen (imagen, "descomprimidaRL");
	}
				 
			
	}
	

	
