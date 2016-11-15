package Herramientas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Compresor {
	
	public static void agregar_al_archivo(FileOutputStream archivo, byte buffer) {

		try {
			archivo.write((byte)buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void agregar_al_archivo_int(FileOutputStream archivo, int buffer) {

		try {
			for(int i=1;i<=4;i++)
				archivo.write((byte) ((buffer>>32-(8*i)) & -1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String matchCodigo(ArrayList<Color> arreglo, int r){
		boolean encontrado = false;
		int i=0;
		String codigo="";
		
			while(i<arreglo.size() && !encontrado){
				if(arreglo.get(i).getR()==r){
					encontrado = true;
					codigo = arreglo.get(i).getCodigo();
				}
				i++;
			}
			return codigo;
	}
	
	//El encabezado tiene la siguiente forma
	//	--------------------------------
	//	int height | int width	| int  n | int r | int cant | int r | int cant |... 		
	//  --------------------------------
	//	Donde n = cantidad de colores
	//  	  r = color
	//		  cant = cantidad de repeticiones de ese color

	public void agregar_encabezado(FileOutputStream archivo, ArrayList<Color> arreglo,BufferedImage imagen){
		int n = arreglo.size()-1;

		agregar_al_archivo_int(archivo,imagen.getHeight());
		agregar_al_archivo_int(archivo,imagen.getWidth());
		agregar_al_archivo_int(archivo,n);
		while(n>=0){
			agregar_al_archivo_int(archivo,arreglo.get(n).getCantidad());
			agregar_al_archivo_int(archivo,arreglo.get(n).getR());
			n--;
		}
	}
	
	public void generarCodificacion(ArrayList<Color> arreglo){
		byte buffer=0;
		int cant_digitos=0;
		FileOutputStream archivo;
		try {
			archivo = new FileOutputStream("comprimidoHuffman");
			int R=0; 
			BufferedImage buffer_img = null;
			try {
			   buffer_img = ImageIO.read(new File("stars_8.bmp"));
			   agregar_encabezado(archivo,arreglo,buffer_img);
			   //Recorro la imagen y voy mirando cada pixel
			   for(int i=0;i<buffer_img.getWidth();i++){ 
			       for(int j=0;j<buffer_img.getHeight();j++){
			    	   int rgb = buffer_img.getRGB(i, j);
			    	   java.awt.Color color_aux = new java.awt.Color(rgb, true);
			    	   R = color_aux.getRed(); 
		           	   String codigo = matchCodigo(arreglo,R);
		           	   int max = codigo.length()-1;
		           	   int m = 0;
		           	   while(m<=max){
							buffer =(byte) (buffer<<1);
							if(codigo.charAt(m)=='1'){
								buffer = (byte) (buffer|1);
							}
							cant_digitos++;
							if(cant_digitos==8){
								agregar_al_archivo(archivo,buffer);
								buffer=0;
								cant_digitos=0; 	
							}
							m++;
						}
			       } 
			   }
			   	//Los que sobran.
				if ((cant_digitos<8)&&(cant_digitos>0)){
					buffer=(byte) (buffer<<(8-cant_digitos));
					agregar_al_archivo(archivo,buffer);
				}
				archivo.close();
			} catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void comprimirRunLeght () throws IOException{
		
		FileWriter archivo = new FileWriter("comprimidoRL.txt");
		int suma=0;
		int cantidad=0;
		BufferedImage buffer_img = null;
		buffer_img = ImageIO.read(new File("stars_8.bmp"));
		//Creo encabezado
		Integer altura = buffer_img.getHeight();
		Integer ancho = buffer_img.getWidth();
		String h = altura.toString();
		String w = ancho.toString();
		archivo.write(h);
		archivo.write ("/");
		archivo.write(w);
		archivo.write("/");
		archivo.write("\n");
		
		int rgb = buffer_img.getRGB(0, 0);
 	   java.awt.Color color_aux = new java.awt.Color(rgb, true);
 	   int R1 = color_aux.getRed();
		int R=0;
			//Recorro la imagen y voy mirando cada pixel
		   for(int i=0;i<buffer_img.getHeight();i++){ 
		       for(int j=0;j<buffer_img.getWidth();j++){
		    	    rgb = buffer_img.getRGB(j, i);
		    	    color_aux = new java.awt.Color(rgb, true);
		    	   R = color_aux.getRed();
		    	   if (R==R1)	    	   
		    		   			suma++;
		    	   else
		   
		    	   {  //Cada vez que encuentro un simbolo distinto en la imagen escribo el archivo separando por puntos
		    		   cantidad=cantidad+suma;
		    		   String codif ="";
		    		   codif+=R1;
		    		   codif+="-";
		    		   codif+=suma;
		    		   codif+=".";
		    		   archivo.write(codif);
		    		   suma=1;
		    		   R1=R;
		    		   
		    	   }
		       }   	   	   
		
	}
		   cantidad=cantidad+suma;
		   String codif ="";
		   codif+=R1;
		   codif+="-";
		   codif+=suma;
		   codif+=".";
		   archivo.write(codif);
		
	archivo.close();	           

}

}

